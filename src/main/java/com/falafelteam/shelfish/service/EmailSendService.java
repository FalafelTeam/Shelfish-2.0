package com.falafelteam.shelfish.service;

import com.falafelteam.shelfish.model.users.User;
import lombok.Getter;
import lombok.extern.java.Log;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import java.io.IOException;

/**
 * Service class that sends emails
 */
@Log
@Getter
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
    public void sendEmail(User user, String subject, String message) throws IOException {
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
            LoggingService loggingService = new LoggingService();
            loggingService.failedToSendEmailLog(user, subject);
        }
    }

    private final String BOOKED_SUBJ = "Document booking";
    private final String BOOKED_MESSAGE = "Dear customer, \n\nYou've successfully booked a book in Shelfish library! \n" +
            "When you will be able to take the document, we will send you a message." +
            "you will have only 1 day to pick it up. \n\n Kind regards,\n\nShelfish Team";

    private final String AVAIL_SUBJ = "Time to take your book!";
    private final String AVAIL_MESSAGE = "Document that you booked is available! So come take it :) \n\nNote: You have only one day, after" +
            "which your booking will expire. Hurry up!\n\nKinds regards,\n\nShelfish Team";

    private final String CHECKOUT_SUBJ = "You have successfully checked out document!";
    private final String CHECKOUT_MESSAGE = "Have a nice read! \n\nKinds regards,\n\nShelfish Team";

    private final String DAYTILLRETURN_SUBJ = "You have only 1 day left of reading!";
    private final String DAYTILLRETURN_MESSAGE = "Please come to return or renew your document tomorrow.\"+\n" +
            "\n\nNote: In case you don't return or renew tomorrow, we will apply a fine of 100 rub" +
            "per day\nNote: Maximum fine is the price of the book." +
            "\n\nKinds regards,\n\nShelfish Team";

    private final String FIRSTFINE_SUBJ = "Your first day of fine!";
    private final String FIRSTFINE_MESSAGE = "Please return or renew your document as soon as possible.\n\nKinds regards,\n\nShelfish Team\"";

    private final String RETURNED_SUBJ = "You have successfully returned your document to the library!";
    private final String RETURNED_MESSAGE = "Thank you for timing. Feel free to order new books soon!" +
            "\n\nKinds regards,\n\nShelfish Team";

    private final String RENEW_SUBJ = "You have successfully renewed your document!";
    private final String RENEW_MESSAGE = "Thank you for renewing your document! You now have more time to enjoy your document!" +
            "\n\nKinds regards,\n\nShelfish Team";

    private final String OUTSTANDING_SUBJ = "Outstanding request :( You have been deleted from the queue";
    private final String OUTSTANDING_MESSAGE = "An outstanding request has been sent, so everyone has been deleted from the queue" +
            "\n\n Don't worry! You will be able to book your document again soon." +
            "\nSorry for the trouble.\n\nKinds regards,\n\nShelfish Team";
    private final String OUTSTAND_RETURN_REQUEST_SUBJ = "Outstanding request :( Please return the document immediately";
    private final String OUTSTAND_RETURN_REQUEST_MESSAGE = "The document that you have on hands is not going to be available anymore." +
            " Please return it as soon as possible. \nSorry for the trouble.\n\nKinds regards,\n\nShelfish Team";
}