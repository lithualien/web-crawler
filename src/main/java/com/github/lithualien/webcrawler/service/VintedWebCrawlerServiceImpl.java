package com.github.lithualien.webcrawler.service;

import com.github.lithualien.webcrawler.Main;
import com.github.lithualien.webcrawler.converter.vinted.VintedConvertAdvertToLinks;
import com.github.lithualien.webcrawler.converter.vinted.VintedStringSetToAdvertisementSet;
import com.github.lithualien.webcrawler.crawler.VintedCrawler;
import com.github.lithualien.webcrawler.io.IORepository;
import com.github.lithualien.webcrawler.io.IORepositoryImpl;
import com.github.lithualien.webcrawler.models.Advertisement;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class VintedWebCrawlerServiceImpl implements WebCrawlerService {

    private final IORepository ioRepository;
    private final VintedCrawler vintedCrawler = new VintedCrawler();

    public VintedWebCrawlerServiceImpl(IORepository ioRepository) {
        this.ioRepository = ioRepository;
    }

    @Override
    public void checkIfNew(Set<String> urls) {
        Set<Advertisement> advertisements = VintedStringSetToAdvertisementSet.convert(urls);
        String FILE_NAME = "/vinted.csv";
        Set<Advertisement> oldAdvertisements = ioRepository.fetchData(FILE_NAME);
        Set<Advertisement> newAdvertisements = getNewAdvertisements(advertisements, oldAdvertisements);

        if(!newAdvertisements.isEmpty()) {
            ioRepository.addData(newAdvertisements, FILE_NAME);
            setMessageVinted(newAdvertisements);
        } else {
            log.info("No new advertisements were found.");
        }

    }

    private void setMessageVinted(Set<Advertisement> advertisements) {
        String message = VintedConvertAdvertToLinks.htmlMessage(advertisements);
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

    public Set<String> getDocUrls(Document doc) {
        Set<String> urls = new HashSet<>();


        Elements advertisements = doc.select("a");
        for (Element advertisement : advertisements) {
            String url = advertisement.absUrl("href");

            if(vintedCrawler.shouldVisit(url)) {
                urls.add(url);
            }
        }
        log.info("Added vinted HTML links.");
        return urls;
    }

    public Set<String> getJsonUrls(Document doc) {

        Set<String> urls = new HashSet<>();

        IORepositoryImpl ioRepository = new IORepositoryImpl();

        Element scriptElement = doc.select("script").last();
        JSONObject jsonObject = new JSONObject(scriptElement.html());
        JSONObject items = jsonObject.getJSONObject("items");
        JSONObject byId = items.getJSONObject("byId");

//        ioRepository.writevintedHtml(doc.html());

        for (String key : byId.keySet()) {
            JSONObject jsonAdvertisement = byId.getJSONObject(key);

            String url = jsonAdvertisement.optString("url");

//            ioRepository.writeVintedJson(jsonAdvertisement.toString());

            if( (jsonAdvertisement.has("url")) && (vintedCrawler.shouldVisit(url)) ) {
                urls.add(url);
            }
        }

//        Main.addOne();

        log.info("Added vinted JSON links.");
        return urls;
    }
}
