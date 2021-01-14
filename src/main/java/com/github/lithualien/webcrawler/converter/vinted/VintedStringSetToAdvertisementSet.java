package com.github.lithualien.webcrawler.converter.vinted;

import com.github.lithualien.webcrawler.models.Advertisement;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public final class VintedStringSetToAdvertisementSet {

    public static Set<Advertisement> convert(Set<String> urls) {
        Set<Advertisement> advertisements = new HashSet<>();
        Pattern pattern = Pattern.compile("/(\\d+)(?!.*\\d)");

        urls.removeAll(getRemoveCollection(urls, "help"));
        urls.removeAll(getRemoveCollection(urls, "member"));

        urls
                .forEach(url -> {
                    Matcher matcher = pattern.matcher(url);

                    if(matcher.find()) {
                        String result = matcher.group()
                                .replace("/", "");

                        String[] resultArray = result.split("-");
                        Integer resultId = Integer.parseInt(resultArray[0]);

                        Advertisement advertisement = new Advertisement(resultId, url);
                        advertisements.add(advertisement);
                    }
                });
        log.info("Refactored vinted urls to advertisement objects.");
        return advertisements;
    }

    private static Collection<String> getRemoveCollection(Set<String> urls, String keyword) {
        List<String> operatedList = new ArrayList<>();

        urls.stream()
                .filter(item -> item.contains(keyword))
                .forEach(operatedList::add);
        log.info("Removed non-advertisement links.");
        return operatedList;
    }

}
