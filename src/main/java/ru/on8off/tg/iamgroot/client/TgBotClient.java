package ru.on8off.tg.iamgroot.client;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.on8off.tg.iamgroot.configuration.TgBotClientConfiguration;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class TgBotClient extends TelegramLongPollingBot {
    private TgBotClientConfiguration config;
    private TgMessageReceiver messageReceiver;
    private ExecutorService executor;
    private long startUpStamp;

    public TgBotClient(TgBotClientConfiguration tgBotClientConfiguration) {
        this.config = tgBotClientConfiguration;
    }

    @PostConstruct
    public void init() throws TelegramApiException {
        if(config.getThreadCount() > 1){
            executor = initThreadPoolExecutor(config.getThreadCount());
        }
        if(Boolean.TRUE.equals(config.getProductionMode())) {
             TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
             telegramBotsApi.registerBot(this);
        }
        startUpStamp = System.currentTimeMillis();
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("onUpdateReceived: {}", update);
        if(update.getMessage() != null){
            if(isExpiredMessage(update.getMessage().getDate())){
                log.info("Skip expired message: {}", update.getMessage());
            } else {
                Runnable r = ()->{
                    try {
                        messageReceiver.receive(update.getMessage());
                    } catch (Exception e) {
                        log.info("Unhandled exception from receiver", e);
                    }
                };
                if(executor != null){
                    executor.submit(r);
                } else {
                    r.run();
                }
            }
        }
    }

    public void sendMessage(SendMessage message){
        log.info("sendMessage: {}", message);
        if(Boolean.TRUE.equals(config.getProductionMode())) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public void sendAnimation(SendAnimation animation){
        log.info("sendAnimation: {}", animation);
        if(Boolean.TRUE.equals(config.getProductionMode())) {
            try {
                execute(animation);
            } catch (TelegramApiException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotUserName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    public void setMessageReceiver(TgMessageReceiver tgMessageReceiver){
        this.messageReceiver = Objects.requireNonNull(tgMessageReceiver, "TgMessageReceiver must not be null");
    }

    private ThreadPoolExecutor initThreadPoolExecutor(int size){
        var threadFactory = new ThreadFactoryBuilder().setNameFormat("TgBotClient Executor-%d").build();
        return new ThreadPoolExecutor(
                size, size,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(size),
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    private boolean isExpiredMessage(Integer stamp){
        var messageStamp = ((long)stamp) * 1000;
        return messageStamp < startUpStamp;
    }
}
