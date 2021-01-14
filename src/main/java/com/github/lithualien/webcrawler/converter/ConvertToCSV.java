package com.github.lithualien.webcrawler.converter;

import com.github.lithualien.webcrawler.models.Advertisement;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public final class ConvertToCSV {

    public static List<String[]> convertAdvertisementsToCSV(Set<Advertisement> advertisements) {
        List<String[]> stringArrayList = new ArrayList<>();
        advertisements
                .forEach(advertisement -> {
                    String[] stringArray = new String[1];
                    stringArray[0] = advertisement.getId() + "," + advertisement.getName();
                    stringArrayList.add(stringArray);
                });
        log.info("Converted advertisement objects to csv compatible list.");
        return stringArrayList;
    }
}
