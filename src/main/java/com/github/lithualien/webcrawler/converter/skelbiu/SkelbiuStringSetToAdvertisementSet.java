package com.github.lithualien.webcrawler.converter.skelbiu;

import com.github.lithualien.webcrawler.models.Advertisement;
import lombok.extern.slf4j.Slf4j;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public final class SkelbiuStringSetToAdvertisementSet {

    public static Set<Advertisement> getIds(Set<String> urls) {
        Set<Advertisement> advertisements = new HashSet<>();
        Pattern patternNumber = Pattern.compile("(\\d+)(?!.*\\d)");

        urls.forEach( url -> {
            Matcher matcherNumber = patternNumber.matcher(url);

            Integer resultInteger = null;

            if (matcherNumber.find()) {
                resultInteger = Integer.parseInt(matcherNumber.group());
            }

            Advertisement advertisement = new Advertisement(resultInteger, url);
            advertisements.add(advertisement);
        });

        log.info("Refactored skelbiu urls to advertisement objects.");
        return advertisements;
    }

}
