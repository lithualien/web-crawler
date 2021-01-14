package com.github.lithualien.webcrawler;

import com.github.lithualien.webcrawler.jobs.AdvertisementJob;
import com.github.lithualien.webcrawler.models.Advertisement;
import com.github.lithualien.webcrawler.service.MailServiceError;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class Main {

    @Getter
    private static Set<Advertisement> urls = new HashSet<>();

    @Getter
    private static Integer counter = 0;

    @Getter
    private static String messageToSend = "";

    public static void main( String[] args ) {

        JobDetail job = JobBuilder
                .newJob(AdvertisementJob.class)
                .build();


        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("skelbiuTrigger")
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .withIntervalInMinutes(2)
                        .repeatForever())
                .build();

        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException schedulerException) {
            String errorMessage = "Error occurred starting scheduler, error message: \n"
                    + ExceptionUtils.getStackTrace(schedulerException);
            MailServiceError.sendEmail(errorMessage);
            log.error(errorMessage);
        }

    }

    public static void resetUrls() {
        urls = new HashSet<>();
    }

    public static void setUrls(Set<Advertisement> addUrls) {
        if(!addUrls.isEmpty()) {
            urls.addAll(addUrls);
        }
    }

    public static void resetMessageToSend() {
        messageToSend = "";
    }

    public static void setMessageToSend(String message) {
        if(!message.isEmpty()) {
            messageToSend += message;
        }
    }

    public static void addOne() {
        counter++;
    }

}
