package ru.on8off.tg.iamgroot.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Slf4j
public class PropertiesUtils {

    public static Properties loadFromClassPath(String fileName) throws IOException {
        var properties = new Properties();
        var resource = new ClassPathResource(fileName);
        try( final var in = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8 )){
            properties.load( in );
        }
        return properties;
    }
}
