package ru.on8off.tg.iamgroot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.on8off.tg.iamgroot.client.TgBotClient;

import java.util.Optional;

@Service
@Slf4j
public class IamGrootBotService {
    private TgBotClient tgBotClient;
    private GifService gifService;
    private I18nService i18nService;
    private StatisticService statisticService;

    public IamGrootBotService(TgBotClient tgBotClient, GifService gifService, I18nService i18nService, StatisticService statisticService) {
        this.tgBotClient = tgBotClient;
        this.gifService = gifService;
        this.i18nService = i18nService;
        this.statisticService = statisticService;
        this.tgBotClient.setMessageReceiver(this::receive);
    }

    public void receive(Message inMessage){
        if(isCommand(inMessage, "/start") || isCommand(inMessage, "/help")) {
            sendStatistic(inMessage);
            sendHelpMessage(inMessage);
        } else  if(inMessage.isUserMessage() || (inMessage.isGroupMessage() && isBotMention(inMessage))) {
            sendStatistic(inMessage);
            sendResponseMessage(inMessage);
        }
    }

    private void sendHelpMessage(Message inMessage) {
        var lang = getLangCode(inMessage);
        var text = i18nService.getHelpText(lang, inMessage.isGroupMessage());
        var messageBuilder = SendMessage.builder();
        messageBuilder.chatId(String.valueOf(inMessage.getChatId()));
        messageBuilder.text(text);
        tgBotClient.sendMessage(messageBuilder.build());
    }

    private void sendResponseMessage(Message inMessage) {
        var lang = getLangCode(inMessage);
        var text = i18nService.getResponseText(lang);
        var messageBuilder = SendMessage.builder();
        messageBuilder.chatId(String.valueOf(inMessage.getChatId()));
        messageBuilder.text(text);
        tgBotClient.sendMessage(messageBuilder.build());

        var gif = gifService.getRandomGif();
        if(gif != null) {
            var animationBuilder = SendAnimation.builder();
            animationBuilder.chatId(String.valueOf(inMessage.getChatId()));
            animationBuilder.animation(new InputFile(gif));
            tgBotClient.sendAnimation(animationBuilder.build());
        }
    }

    private void sendStatistic(Message inMessage){
        var chatId = String.valueOf(inMessage.getChatId());
        var chatName = getChatName(inMessage);
        var chatType = inMessage.getChat().getType();
        statisticService.addMessageStatistic(chatId,chatName, chatType);
    }

    private boolean isCommand(Message inMessage, String command) {
        return inMessage.isCommand() && command.equals(inMessage.getText());
    }

    private boolean isBotMention(Message inMessage){
        if(inMessage.hasEntities()) {
            for (MessageEntity entity : inMessage.getEntities()) {
                if("mention".equals(entity.getType()) &&
                        entity.getText().contains("@" + tgBotClient.getBotUsername())){
                    return true;
                }
            }
        }
        return false;
    }

    private String getLangCode(Message inMessage){
        return Optional.ofNullable(inMessage.getFrom()).map(User::getLanguageCode).orElse(null);
    }

    private String getChatName(Message inMessage){
        var chat = inMessage.getChat();
        var chatName = "-";
        if(inMessage.isGroupMessage() && hasLength(chat.getTitle())) {
            chatName = inMessage.getChat().getTitle();
        } else if(inMessage.isUserMessage()){
            if(hasLength(chat.getFirstName()) && hasLength(chat.getLastName())) {
                chatName = chat.getFirstName() + " " + chat.getLastName();
            } else if (hasLength(chat.getFirstName())) {
                chatName = chat.getFirstName();
            } else if (hasLength(chat.getLastName())) {
                chatName = chat.getLastName();
            }
        }
        return chatName;
    }

    private boolean hasLength(String str){
        return str != null && !str.isEmpty();
    }
}
