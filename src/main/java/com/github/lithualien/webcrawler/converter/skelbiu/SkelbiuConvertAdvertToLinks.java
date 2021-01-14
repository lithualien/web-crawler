package com.github.lithualien.webcrawler.converter.skelbiu;

import com.github.lithualien.webcrawler.models.Advertisement;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public final class SkelbiuConvertAdvertToLinks {

    public static String htmlMessage(Set<Advertisement> advertisements) {

        StringBuffer stringBuffer = new StringBuffer();
        advertisements.forEach(advertisement -> stringBuffer.append("<p><a href=\"https://www.skelbiu.lt/")
                .append(advertisement.getId())
                .append(".html\">")
                .append(advertisement.getName())
                .append("</a>")
                .append("</p>"));
        log.info("Formatted skelbiu advertisements to send via email.");
        return stringBuffer.toString();
    }

}
