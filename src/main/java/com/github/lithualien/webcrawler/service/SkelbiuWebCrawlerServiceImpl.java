package com.github.lithualien.webcrawler.service;

import com.github.lithualien.webcrawler.Main;
import com.github.lithualien.webcrawler.converter.skelbiu.SkelbiuConvertAdvertToLinks;
import com.github.lithualien.webcrawler.converter.skelbiu.SkelbiuStringSetToAdvertisementSet;
import com.github.lithualien.webcrawler.crawler.SkelbiuCrawler;
import com.github.lithualien.webcrawler.io.IORepository;
import com.github.lithualien.webcrawler.models.Advertisement;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class SkelbiuWebCrawlerServiceImpl implements WebCrawlerService {

    private final IORepository ioRepository;
    private final String FILE_NAME = "/skelbiu.csv";
    private final SkelbiuCrawler skelbiuCrawler = new SkelbiuCrawler();

    public SkelbiuWebCrawlerServiceImpl(IORepository ioRepository) {
        this.ioRepository = ioRepository;
    }

    @Override
    public void checkIfNew(Set<String> urls) {
        Set<Advertisement> advertisements = SkelbiuStringSetToAdvertisementSet.getIds(urls);
        Set<Advertisement> oldAdvertisements = ioRepository.fetchData(FILE_NAME);
        Set<Advertisement> newAdvertisements = getNewAdvertisements(advertisements, oldAdvertisements);

        if(!newAdvertisements.isEmpty()) {
            ioRepository.addData(newAdvertisements, FILE_NAME);
            sendEmail(newAdvertisements);
        }
    }

    public Set<String> getUrls(Document doc) {
        Set<String> urls = new HashSet<>();

        Elements advertisements = doc.select("a");

        for(Element advertisement : advertisements) {
            String url = advertisement.absUrl("href");

            if(skelbiuCrawler.shouldVisit(url)) {
                urls.add(url);
            }
        }
        log.info("Added skelbiu HTML links.");
        return urls;
    }

    private void sendEmail(Set<Advertisement> advertisements) {
        String message = SkelbiuConvertAdvertToLinks.htmlMessage(advertisements);
        Main.setMessageToSend(message);
    }

    private Set<Advertisement> getNewAdvertisements(Set<Advertisement> advertisements,
                                                    Set<Advertisement> oldAdvertisements) {
        Set<Advertisement> newAdvertisements = new HashSet<>();

        if (advertisements != null) {
            advertisements.forEach(advertisement -> {
                if(!oldAdvertisements.contains(advertisement)) {
                    newAdvertisements.add(advertisement);
                }
            });
        }

        log.info("Added new advertisements to set.");
        return newAdvertisements;
    }
}
