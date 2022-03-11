package ru.on8off.tg.iamgroot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GifServiceTest {
    @Autowired
    GifService gifService;

    @Test
    void getRandomGif() {
        var gif = gifService.getRandomGif();
        Assertions.assertNotNull(gif);
        Assertions.assertTrue(gif.startsWith("http"));
    }
}