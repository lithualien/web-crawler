package com.github.lithualien.webcrawler.crawler;

import lombok.extern.slf4j.Slf4j;
import java.util.regex.Pattern;

@Slf4j
public class SkelbiuCrawler {

    private final String DOMAIN = "https://www.skelbiu.lt/";

    public boolean shouldVisit(String url) {
        Pattern pattern = Pattern.compile(".*-*[1-9]\\.html$", Pattern.CASE_INSENSITIVE);

        if ( (!url.startsWith(DOMAIN)) || (!pattern.matcher(url).find()) ) {
            return false;
        }
        return true;
    }

}