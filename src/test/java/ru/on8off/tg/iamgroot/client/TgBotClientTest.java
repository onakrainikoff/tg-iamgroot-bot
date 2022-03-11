package ru.on8off.tg.iamgroot.client;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.on8off.tg.iamgroot.fixture.MessageFixture;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TgBotClientTest {
    @Autowired
    TgBotClient tgBotClient;

    @Test
    void onUpdateReceivedPositiveCases() {
        var receiver = Mockito.mock(TgMessageReceiver.class);
        tgBotClient.setMessageReceiver(receiver);

        var privateMessage = MessageFixture.privateMessage();
        var update = new Update();
        update.setMessage(privateMessage);
        tgBotClient.onUpdateReceived(update);
        Mockito.verify(receiver, Mockito.times(1)).receive(privateMessage);

        var groupMessage = MessageFixture.groupMessage(tgBotClient.getBotUsername());
        update = new Update();
        update.setMessage(groupMessage);
        tgBotClient.onUpdateReceived(update);
        Mockito.verify(receiver, Mockito.times(1)).receive(groupMessage);

        var command = MessageFixture.command("/start");
        update = new Update();
        update.setMessage(command);
        tgBotClient.onUpdateReceived(update);
        Mockito.verify(receiver, Mockito.times(1)).receive(command);
    }

    @Test
    void onUpdateReceivedNegativeCases() {
        var receiver = Mockito.mock(TgMessageReceiver.class);
        tgBotClient.setMessageReceiver(receiver);

        var message = MessageFixture.privateMessage();
        message.setDate(message.getDate() - 3600);
        var update = new Update();
        update.setMessage(message);
        tgBotClient.onUpdateReceived(update);
        Mockito.verify(receiver, Mockito.times(0)).receive(message);
    }
}