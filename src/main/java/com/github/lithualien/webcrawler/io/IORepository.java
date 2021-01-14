package com.github.lithualien.webcrawler.io;

import com.github.lithualien.webcrawler.models.Advertisement;

import java.util.Set;

public interface IORepository {

    void addData(Set<Advertisement> advertisements, String fileName);
    Set<Advertisement> fetchData(String fileName);

}
