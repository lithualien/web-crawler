package com.github.lithualien.webcrawler.service;

import com.github.lithualien.webcrawler.Main;
import com.github.lithualien.webcrawler.config.MailConfig;
import com.github.lithualien.webcrawler.config.PropertiesConfig;
import com.github.lithualien.webcrawler.models.Advertisement;
import com.github.lithualien.webcrawler.notification.DesktopNotification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.util.Properties;
import java.util.Set;

@Slf4j
public class MailServiceImpl implements MailService {

    private final MailConfig mailConfig = new MailConfig();
    private final PropertiesConfig propertiesConfig = new PropertiesConfig();
    private final Properties properties = propertiesConfig.loadProperties();

    public void sendEmail(String messageToSend) {
        try {
            InternetAddress recipient = new InternetAddress(properties.getProperty("mail.smtp.receiver"));
            InternetAddress recipientRembo = new InternetAddress("sestadas@gmail.com");

            Message message = getMessage("Nauji skelbimai pasirinkomis kategorijomis", recipient);
            Message message1 = getMessage("Nauji skelbimai pasirinkomis kategorijomis", recipientRembo);

            sendEmail(message, messageToSend);
            sendEmail(message1, messageToSend);

            Main.resetUrls();
            Main.resetMessageToSend();
            log.info("Urls and message object were reset.");
        } catch (MessagingException messagingException) {
            String errorMessage = "Failed to send email, error message:\n"
                    + ExceptionUtils.getStackTrace(messagingException);
            MailServiceError.sendEmail(errorMessage);
            log.error(errorMessage);
        }
    }

    private void sendDesktopNotification(int numberOfEmails) {
        if(SystemTray.isSupported()) {
            DesktopNotification desktopNotification = new DesktopNotification();
            desktopNotification.displayNotification(numberOfEmails);
        }
    }

    private MimeMessage getMessage(String subject, InternetAddress internetAddress) {
        try {
            MimeMessage message = new MimeMessage(mailConfig.getSession());
            message.setFrom(new InternetAddress(properties.getProperty("mail.smtp.email")));
            message.addRecipient(Message.RecipientType.TO, internetAddress);
            message.setSubject(subject);
            return message;
        } catch (MessagingException messagingException) {
            String errorMessage = "Failed to get email, error message:\n"
                    + ExceptionUtils.getStackTrace(messagingException);
            MailServiceError.sendEmail(errorMessage);
            log.error(errorMessage);
        }
        return null;
    }

    private void sendEmail(Message message, String messageToSend) {
        try {
            if(message != null) {
                String CONTENT_TYPE = "text/html";
                message.setContent(messageToSend, CONTENT_TYPE);
                Transport.send(message);
                log.info("Email message was sent.");
            }
        } catch (MessagingException messagingException) {
            String errorMessage = "Failed to get email, error message:\n"
                    + ExceptionUtils.getStackTrace(messagingException);
            MailServiceError.sendEmail(errorMessage);
            log.error(errorMessage);
        }
    }
}