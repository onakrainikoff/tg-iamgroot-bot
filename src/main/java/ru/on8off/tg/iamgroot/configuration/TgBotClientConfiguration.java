package ru.on8off.tg.iamgroot.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TgBotClientConfiguration {
    @Value("${tgBotClient.userName}")
    private String botUserName;

    @Value("${tgBotClient.token}")
    private String botToken;

    @Value("${tgBotClient.productionMode}")
    private Boolean productionMode;

    @Value("${tgBotClient.threadCount}")
    private Integer threadCount;
}

