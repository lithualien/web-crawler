package com.github.lithualien.webcrawler.converter.vinted;

import com.github.lithualien.webcrawler.models.Advertisement;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class VintedConvertAdvertToLinks {

    public static String htmlMessage(Set<Advertisement> advertisements) {
        StringBuffer stringBuffer = new StringBuffer();

        advertisements.forEach(advertisement -> stringBuffer
                .append("<p><a href=\"")
                .append(advertisement.getName())
                .append("\">")
                .append(advertisement.getName())
                .append("</a></p>"));
        log.info("Formatted vinted advertisements to send via email.");
        return stringBuffer.toString();
    }
}
