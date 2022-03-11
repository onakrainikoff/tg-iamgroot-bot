package ru.on8off.tg.iamgroot.service;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StatisticServiceTest {
    @Autowired
    StatisticService statisticService;
    @Autowired
    MeterRegistry meterRegistry;

    @Test
    void addMessage() {
        statisticService.addMessageStatistic("1", "Test", "group");
        statisticService.addMessageStatistic("2", "Test2", "private");
        var count = meterRegistry.find(StatisticService.MESSAGES_RECEIVED);
        Assertions.assertEquals(2, count.counters().size());
    }
}