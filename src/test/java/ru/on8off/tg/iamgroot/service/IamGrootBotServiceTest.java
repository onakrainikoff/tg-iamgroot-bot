package ru.on8off.tg.iamgroot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.on8off.tg.iamgroot.client.TgBotClient;
import ru.on8off.tg.iamgroot.configuration.TgBotClientConfiguration;
import ru.on8off.tg.iamgroot.fixture.MessageFixture;

@SpringBootTest
class IamGrootBotServiceTest {
    @MockBean
    StatisticService mockStatisticService;
    @MockBean
    TgBotClient mockTgBotClient;
    @Autowired
    IamGrootBotService iamGrootBotService;
    @Autowired
    TgBotClientConfiguration config;

    @Test
    void receivePrivateMessage() {
        var msg = MessageFixture.privateMessage();
        iamGrootBotService.receive(msg);
        Mockito.verify(mockTgBotClient, Mockito.times(1)).sendMessage(
                Mockito.argThat( response -> {
                    Assertions.assertNotNull(response.getText());
                    return true;
                })
        );
        Mockito.verify(mockTgBotClient, Mockito.times(1)).sendAnimation(
                Mockito.argThat( response -> {
                    Assertions.assertNotNull(response.getAnimation());
                    return true;
                })
        );
        Mockito.verify(mockStatisticService, Mockito.times(1))
                .addMessageStatistic(
                        String.valueOf(msg.getChatId()),
                        msg.getChat().getFirstName() + " " + msg.getChat().getLastName(),
                        msg.getChat().getType()
                );
    }

    @Test
    void receiveGroupMessage() {
        Mockito.when(mockTgBotClient.getBotUsername()).thenReturn(config.getBotUserName());
        var msg = MessageFixture.groupMessage(config.getBotUserName());
        iamGrootBotService.receive(msg);
        Mockito.verify(mockTgBotClient, Mockito.times(1)).sendMessage(
                Mockito.argThat( response -> {
                    Assertions.assertNotNull(response.getText());
                    return true;
                })
        );
        Mockito.verify(mockTgBotClient, Mockito.times(1)).sendAnimation(
                Mockito.argThat( response -> {
                    Assertions.assertNotNull(response.getAnimation());
                    return true;
                })
        );
        Mockito.verify(mockStatisticService, Mockito.times(1))
                .addMessageStatistic(
                        String.valueOf(msg.getChatId()),
                        msg.getChat().getTitle(),
                        msg.getChat().getType()
                );
    }

    @Test
    void receiveCommand() {
        var msg = MessageFixture.command("/start");
        iamGrootBotService.receive(msg);

        msg = MessageFixture.command("/help");
        iamGrootBotService.receive(msg);
        Mockito.verify(mockTgBotClient, Mockito.times(2)).sendMessage(
                Mockito.argThat( response -> {
                    Assertions.assertNotNull(response.getText());
                    return true;
                })
        );
        Mockito.verify(mockTgBotClient, Mockito.times(0)).sendAnimation(Mockito.any());
        Mockito.verify(mockStatisticService, Mockito.times(2))
                .addMessageStatistic(
                        String.valueOf(msg.getChatId()),
                        msg.getChat().getTitle(),
                        msg.getChat().getType()
                );
    }

    @Test
    void receiveAndSkip(){
        var msg = MessageFixture.command("/test");
        iamGrootBotService.receive(msg);

        msg = MessageFixture.groupMessage(null);
        iamGrootBotService.receive(msg);

        Mockito.verify(mockTgBotClient, Mockito.times(0)).sendMessage(Mockito.any());
        Mockito.verify(mockTgBotClient, Mockito.times(0)).sendAnimation(Mockito.any());
        Mockito.verify(mockStatisticService, Mockito.times(0))
                .addMessageStatistic(
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any()
                );
    }
}