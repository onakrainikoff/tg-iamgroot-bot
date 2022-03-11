package ru.on8off.tg.iamgroot.service;

import org.springframework.stereotype.Service;
import ru.on8off.tg.iamgroot.util.PropertiesUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

@Service
public class GifService {
    private static final String GIFS_FILE = "gifs.properties";
    private final Random random = new Random();
    private Properties gifs;

    @PostConstruct
    public void init() throws IOException {
        gifs = PropertiesUtils.loadFromClassPath(GIFS_FILE);
        if(gifs.isEmpty()){
            throw new IllegalArgumentException(GIFS_FILE + " can`t be empty");
        }
    }

    public String getRandomGif(){
        if(!gifs.isEmpty()) {
            var index =  random.nextInt(gifs.size());
            var gif = gifs.getProperty("gif."+ index, "").trim();
            if(!gif.isEmpty()){
                return gif;
            }
        }
        return null;
    }
}
