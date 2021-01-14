package com.github.lithualien.webcrawler.controller;

import com.github.lithualien.webcrawler.io.IORepositoryImpl;
import com.github.lithualien.webcrawler.service.MailServiceError;
import com.github.lithualien.webcrawler.service.SkelbiuWebCrawlerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class SkelbiuController {

    private final SkelbiuWebCrawlerServiceImpl webCrawlerService = new SkelbiuWebCrawlerServiceImpl(new IORepositoryImpl());

    List<String> urls = Arrays.asList(
            "https://www.skelbiu.lt/skelbimai/?autocompleted=1&keywords=lego&submit_bn=&cost_min=&cost_max=&condition=&" +
                    "cities=0&distance=0&mainCity=0&search=1&category_id=401&type=0&user_type=0&ad_since_min=0" +
                    "&ad_since_max=0&visited_page=1&orderBy=1&detailsSearch=0",
            "https://www.skelbiu.lt/skelbimai/?autocompleted=1&keywords=&submit_bn=&cost_min=&cost_max=&condition=" +
                    "&cities=0&distance=0&mainCity=0&search=1&category_id=4228&type=0&user_type=0&ad_since_min=0" +
                    "&ad_since_max=0&visited_page=1&orderBy=1&detailsSearch=0'",
            "https://www.skelbiu.lt/skelbimai/?autocompleted=1&keywords=&submit_bn=&cost_min=&" +
                    "cost_max=&condition=&cities=0&distance=0&mainCity=0&search=1&category_id=11737&type=1" +
                    "&user_type=0&ad_since_min=0&ad_since_max=0&visited_page=1&orderBy=1&detailsSearch=0"
    );

    public void skelbiuUrlFetch() {

        Set<String> allUrls = new HashSet<>();

        for(String url : urls) {
            try {
                Document doc = Jsoup.connect(url).get();
                allUrls.addAll(webCrawlerService.getUrls(doc));
            } catch (IOException ioException) {
                String errorMessage = "Failed to create handshake, error message: \n"
                        + ExceptionUtils.getStackTrace(ioException);
                MailServiceError.sendEmail(errorMessage);
                log.error(errorMessage);
            }
        }

        if(!allUrls.isEmpty()) {
            log.info("All skelbiu emails were fetched.");
            webCrawlerService.checkIfNew(allUrls);
        } else {
            log.info("No links were found.");
        }
    }
}
