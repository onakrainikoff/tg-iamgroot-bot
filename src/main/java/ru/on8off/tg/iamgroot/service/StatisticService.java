package ru.on8off.tg.iamgroot.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class StatisticService {
    public static final String MESSAGES_RECEIVED = "messages.received";
    private MeterRegistry meterRegistry;

    public StatisticService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void addMessageStatistic(String chatId, String chatName, String chatType){
        var counter = Counter.builder(MESSAGES_RECEIVED)
                   .tag("chatType", chatType)
                   .tag("chatName", chatName)
                   .tag("chatId", chatId)
                   .register(meterRegistry);
        counter.increment();
    }
}
