package com.github.lithualien.webcrawler.io;

import com.github.lithualien.webcrawler.Main;
import com.github.lithualien.webcrawler.converter.ConvertToCSV;
import com.github.lithualien.webcrawler.converter.ConvertToAdvertisementSet;
import com.github.lithualien.webcrawler.models.Advertisement;
import com.github.lithualien.webcrawler.service.MailServiceError;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Slf4j
public class IORepositoryImpl implements IORepository {

    private final Path FOLDER_LOCATION = Paths.get(System.getProperty("java.io.tmpdir"), "crawler");

    private void createFolder() {
        try {
            if(!Files.exists(FOLDER_LOCATION)) {
                Files.createDirectory(FOLDER_LOCATION);
                log.debug("Folder was created successfully.");
            }
        } catch (IOException ioException) {
            String errorMessage = "Failed to create folder, error message: \n"
                    + ExceptionUtils.getStackTrace(ioException);
            MailServiceError.sendEmail(errorMessage);
            log.error(errorMessage);
        }
    }

    private void createFile(String fileName) {
        try {
            Path fileLocation = Paths.get(FOLDER_LOCATION.toString(), fileName);
            if(!Files.exists(fileLocation)) {
                Files.createFile(fileLocation);
                log.debug("File was created successfully.");
            }
        } catch (IOException ioException) {
            String errorMessage = "Failed to create file, error message: \n"
                    + ExceptionUtils.getStackTrace(ioException);
            MailServiceError.sendEmail(errorMessage);
            log.error(errorMessage);
        }
    }

    @Override
    public void addData(Set<Advertisement> advertisements, String fileName) {
        Path filePath = Paths.get(FOLDER_LOCATION.toString(), fileName);
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath.toString(), true))) {
            csvWriter.writeAll(ConvertToCSV.convertAdvertisementsToCSV(advertisements));
            Main.setUrls(advertisements);
            log.info("Added data to csv file.");
        } catch (IOException ioException) {
            String errorMessage = "Failed to write to file, error message: \n"
                    + ExceptionUtils.getStackTrace(ioException);
            MailServiceError.sendEmail(errorMessage);
            log.error(errorMessage);
        }
    }

    @Override
    public Set<Advertisement> fetchData(String fileName) {
        createFolder();
        createFile(fileName);
        log.info("Taking data from csv file.");

        try(Reader reader = Files.newBufferedReader(Paths.get(FOLDER_LOCATION.toString(), fileName));
            CSVReader csvReader = new CSVReader(reader)) {
            List<String[]> records = csvReader.readAll();
            return ConvertToAdvertisementSet.fromCsvToIntegerSet(records);
        } catch (IOException ioException) {
            String errorMessage = "Failed to read the file, error message: \n"
                    + ExceptionUtils.getStackTrace(ioException);
            MailServiceError.sendEmail(errorMessage);
            log.error(errorMessage);
        } catch (CsvException csvException) {
            String errorMessage = "Error occurred while reading the file, error message:\n"
                    + ExceptionUtils.getStackTrace(csvException);
            MailServiceError.sendEmail(errorMessage);
            log.error(errorMessage);
        }
        return null;
    }

    public void writeVintedJson(String text) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateTime = localDateTime.format(formatter);

        String fileName = "vinted-" + Main.getCounter() +
                "-" + dateTime + ".json";

        Path filePath = Paths.get(FOLDER_LOCATION.toString(), "json", fileName);

        try (FileWriter fileWriter =
                     new FileWriter(filePath.toString(), true);) {
            fileWriter.write(text);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void writevintedHtml(String html) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateTime = localDateTime.format(formatter);

        String fileName = "vinted-" + Main.getCounter() +
        "-" + dateTime + ".txt";

        Path filePath = Paths.get(FOLDER_LOCATION.toString(), "html", fileName);

        try (FileWriter fileWriter =
                     new FileWriter(filePath.toString())) {
            fileWriter.write(html);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
