package com.github.lithualien.webcrawler.crawler;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class VintedCrawler {

    private final String DOMAIN = "https://new.vinted.";
    private final String DOMAIN1 = "https://www.vinted.";
    private final String DOMAIN2 = "https://www.new.vinted.";


    public boolean shouldVisit(String url) {
        Pattern pattern = Pattern.compile("/(\\d+)(?!.*\\d)", Pattern.CASE_INSENSITIVE);

        return ((url.startsWith(DOMAIN)) || (url.startsWith(DOMAIN1)) || (url.startsWith(DOMAIN2)))
                && (pattern.matcher(url).find());
    }
}
