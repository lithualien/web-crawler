package com.github.lithualien.webcrawler.service;

import com.github.lithualien.webcrawler.config.MailConfig;
import lombok.extern.slf4j.Slf4j;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Slf4j
public class MailServiceError {

    private static final MailConfig mailConfig = new MailConfig();
    private static final String CONTENT_TYPE = "text/html";
    private static final String EMAIL_ADDRESS = "dominauskas.tomas@gmail.com";

    public static void sendEmail(String messageToSend) {
        try {
            InternetAddress recipient = new InternetAddress(EMAIL_ADDRESS);
            MimeMessage message = getMessage(recipient);

            if(message != null) {
                message.setContent(messageToSend, CONTENT_TYPE);
                Transport.send(message);
                log.info("Email message was sent.");
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static MimeMessage getMessage(InternetAddress internetAddress) {
        try {
            MimeMessage message = new MimeMessage(mailConfig.getSession());
            message.setFrom(new InternetAddress(EMAIL_ADDRESS));
            message.addRecipient(Message.RecipientType.TO, internetAddress);
            message.setSubject("Įvykusi klaida");
            return message;
        } catch (MessagingException messagingException) {
            log.error("Failed to send email.");
            messagingException.printStackTrace();
        }
        return null;
    }
}
