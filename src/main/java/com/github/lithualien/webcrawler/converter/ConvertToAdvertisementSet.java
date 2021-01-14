package com.github.lithualien.webcrawler.converter;

import com.github.lithualien.webcrawler.models.Advertisement;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public final class ConvertToAdvertisementSet {

    public static Set<Advertisement> fromCsvToIntegerSet(List<String[]> data) {
        Set<Advertisement> advertisements = new HashSet<>();

        data.forEach(stringArray -> {
            if(stringArray != null) {
                String[] convertedString = stringArray[0].split(",");
                Advertisement advertisement = new Advertisement(Integer.parseInt(convertedString[0]), convertedString[1]);
                advertisements.add(advertisement);
            }
        });
        log.info("Refactored csv data to advertisement objects.");
        return advertisements;
    }

}
