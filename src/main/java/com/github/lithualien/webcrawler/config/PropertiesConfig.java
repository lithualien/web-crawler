package com.github.lithualien.webcrawler.config;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import java.util.Properties;

@Slf4j
public class PropertiesConfig {

    @NonNull
    public Properties loadProperties() {
        
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.email", "dominauskas.tomas@gmail.com");
        properties.put("mail.smtp.password", "hjeodhrowuxcepuh");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.receiver", "dominauskas.tomas@gmail.com");

        return properties;
    }


}
