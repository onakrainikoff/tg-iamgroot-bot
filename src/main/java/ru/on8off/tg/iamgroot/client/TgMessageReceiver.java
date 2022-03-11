package ru.on8off.tg.iamgroot.client;

import org.telegram.telegrambots.meta.api.objects.Message;

@FunctionalInterface
public interface TgMessageReceiver {
    void receive(Message message);
}
