package com.falafelteam.shelfish.service;

import com.falafelteam.shelfish.model.users.User;
import lombok.extern.java.Log;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

/**
 * Service class that sends emails
 */
@Log
public class EmailSendService {
    private String EMAIL = "shelfishlibrary@gmail.com";
    private String PASSWORD = "shelfish1";
    private int PORT = 465;
    private String HOST = "smtp.gmail.com";

    /**
     * method that sends emails
     *
     * @param user    - user the email is to be sent to
     * @param subject - subject of the email that is to be sent
     * @param message - message of the emil that is to be sent
     */
    public void sendEmail(User user, String subject, String message) {
        try {
            Email email = new SimpleEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setAuthenticator(new DefaultAuthenticator(EMAIL, PASSWORD));
            email.setSSLOnConnect(true);
            email.setFrom(EMAIL);
            email.setSubject(subject);
            email.setMsg(message);
            email.addTo(user.getLogin());
            email.send();
        } catch (Exception e) {
            log.warning("Unable to send email");
        }
    }
}