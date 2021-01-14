package com.github.lithualien.webcrawler.controller;

import com.github.lithualien.webcrawler.io.IORepositoryImpl;
import com.github.lithualien.webcrawler.service.MailServiceError;
import com.github.lithualien.webcrawler.service.VintedWebCrawlerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class VintedController {

    private final VintedWebCrawlerServiceImpl webCrawlerService = new VintedWebCrawlerServiceImpl(new IORepositoryImpl());

    List<String> urls = Arrays.asList(
            "https://new.vinted.lt/vetements?search_text=Lego&catalog[]=1499&search_id=1750976737&order=newest_first",
            "https://new.vinted.lt/vetements?catalog[]=97&order=newest_first"
    );

    public void ventedUrlFetch() {

        Pattern pattern = Pattern.compile("/(\\d+)");
        Set<String> allUrls = new HashSet<>();

        for(String url : urls) {
            try {
                Matcher matcher = pattern.matcher(url);
                Document doc = Jsoup.connect(url).get();

                if(matcher.find()) {
                    allUrls.addAll(webCrawlerService.getDocUrls(doc));
                } else {
                    allUrls.addAll(webCrawlerService.getJsonUrls(doc));
                }
            } catch (IOException ioException) {
                String errorMessage = "Failed to create handshake, error message: \n"
                        + ExceptionUtils.getStackTrace(ioException);
                MailServiceError.sendEmail(errorMessage);
                log.error(errorMessage);
            }
        }

        if(!allUrls.isEmpty()) {
            log.info("All vinted emails were fetched.");
            webCrawlerService.checkIfNew(allUrls);
        } else {
            log.info("No links were found.");
        }
    }
}
