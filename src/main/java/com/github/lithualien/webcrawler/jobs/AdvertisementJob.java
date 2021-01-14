package com.github.lithualien.webcrawler.jobs;

import com.github.lithualien.webcrawler.Main;
import com.github.lithualien.webcrawler.controller.SkelbiuController;
import com.github.lithualien.webcrawler.controller.VintedController;
import com.github.lithualien.webcrawler.service.MailService;
import com.github.lithualien.webcrawler.service.MailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class AdvertisementJob implements Job {

    private final SkelbiuController skelbiuController = new SkelbiuController();
    private final VintedController vintedController = new VintedController();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        skelbiuController.skelbiuUrlFetch();
        vintedController.ventedUrlFetch();

        String messageToSend = Main.getMessageToSend();

        MailService mailService = new MailServiceImpl();

        if(!messageToSend.isEmpty()) {
            mailService.sendEmail(messageToSend);
        } else {
            log.info("Message was empty, did not send email.");
        }
    }


}
