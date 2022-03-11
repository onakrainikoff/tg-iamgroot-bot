package ru.on8off.tg.iamgroot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class I18nServiceTest {
    @Autowired
    I18nService i18nService;

    @Test
    void getHelpText() {
        var resultGroup = i18nService.getHelpText(I18nService.DEFAULT_LANG, true);
        Assertions.assertNotNull(resultGroup);
        Assertions.assertFalse(resultGroup.isEmpty());
        var resultPrivate = i18nService.getHelpText(I18nService.DEFAULT_LANG, false);
        Assertions.assertNotNull(resultPrivate);
        Assertions.assertFalse(resultPrivate.isEmpty());
    }

    @Test
    void getResponseText() {
        var result1 =  i18nService.getResponseText(I18nService.DEFAULT_LANG);
        Assertions.assertNotNull(result1);
        Assertions.assertFalse(result1.isEmpty());
        var result2 =  i18nService.getResponseText("abcd");
        Assertions.assertNotNull(result2);
        Assertions.assertFalse(result2.isEmpty());
        Assertions.assertEquals(result1, result2);
    }
}