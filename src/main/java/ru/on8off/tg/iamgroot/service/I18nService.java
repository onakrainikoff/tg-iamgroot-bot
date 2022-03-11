package ru.on8off.tg.iamgroot.service;

import org.springframework.stereotype.Service;
import ru.on8off.tg.iamgroot.util.PropertiesUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

@Service
public class I18nService {
    public static final String DEFAULT_LANG = "en";
    private static final String I18N_FILE = "i18n.properties";
    private Properties i18n;

    @PostConstruct
    public void init() throws URISyntaxException, IOException {
        i18n = PropertiesUtils.loadFromClassPath(I18N_FILE);
        if(i18n.isEmpty()){
            throw new IllegalArgumentException(I18N_FILE + " can`t be empty");
        }
    }

    public String getHelpText(String lang, boolean isGroup){
        lang = checkLang(lang);
        String key = lang + ".help." + (isGroup ? "group" : "private");
        String defaultKey = DEFAULT_LANG + ".help." + (isGroup ? "group" : "private");
        return getOrDefault(key, defaultKey);
    }

    public String getResponseText(String lang){
        lang = checkLang(lang);
        String key = lang + ".response";
        String defaultKey = DEFAULT_LANG + ".response";
        return getOrDefault(key, defaultKey);
    }

    private String getOrDefault(String key, String defaultKey){
        var text = i18n.getProperty(key, null);
        if(text == null) {
            text = i18n.getProperty(defaultKey, null);
        }
        return text;
    }

    private String checkLang(String lang) {
        if(lang == null || lang.isEmpty()){
            return DEFAULT_LANG;
        } else {
            return lang;
        }
    }
}
