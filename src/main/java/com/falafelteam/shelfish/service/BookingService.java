package com.falafelteam.shelfish.service;

import com.falafelteam.shelfish.model.DocumentUser;
import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.model.service.EmailSendService;
import com.falafelteam.shelfish.model.users.User;
import com.falafelteam.shelfish.repository.DocumentRepository;
import com.falafelteam.shelfish.repository.DocumentUserRepository;
import com.falafelteam.shelfish.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class BookingService {

    private final DocumentUserRepository documentUserRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final EmailSendService emailSendService;
    private final String BOOKED_SUBJ = "Document booking";
    private final String BOOKED_MESSAGE = "Dear customer, \n\nYou've successfully booked a book in Shelfish library! \n"+
                                            "When you will be able to take the document, we will send you a message."+
                                                "you will have only 1 day to pick it up. \n\n Kind regards,\n\nShelfish Team";

    private final String AVAIL_SUBJ = "Time to take your book!";
    private final String AVAIL_MESSAGE ="Document that you booked is available! So come take it :) \n\nNote: You have only one day, after"+
                                            "which your booking will expire. Hurry up!\n\nKinds regards,\n\nShelfish Team";

    private final String CHECKOUT_SUBJ ="You have successfully checked out document!";
    private final String CHECKOUT_MESSAGE="Have a nice read! \\\\n\\nKinds regards,\\n\\nShelfish Team\"";

    private final String DAYTILLRETURN_SUBJ="You have only 1 day left of reading!";
    private final String DAYTILLRETURN_MESSAGE="Please come to return or renew your document tomorrow.\"+\n" +
                                            "\\n\\nNote: In case you don't return or renew tomorrow, we will apply a fine of 100 rub" +
                                            "per day\nNote: Maximum fine is the price of the book."+
                                            "\\n\\nKinds regards,\\n\\nShelfish Team\"";
    private final String FIRSTFINE_SUBJ="";
    private final String FIRSTFINE_MESSAGE="\\n\\nKinds regards,\\n\\nShelfish Team\"";
    private final String RETURNED_SUBJ="";
    private final String RETURNED_MESSAGE="\\n\\nKinds regards,\\n\\nShelfish Team\"";
    private final String RENEW_SUBJ="";
    private final String RENEW_MESSAGE="\\n\\nKinds regards,\\n\\nShelfish Team\"";

    @Autowired
    public BookingService(DocumentUserRepository documentUserRepository, DocumentRepository documentRepository, UserRepository userRepository) {
        this.documentUserRepository = documentUserRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.emailSendService = new EmailSendService();
    }

    public void book(Document document, User user, Integer preferredWeeksNum) throws Exception {
        if (user.getRole().getPriority() == 0) {
            outstandingRequest(document);
            return;
        }
        if (document.isReference()) {
            throw new Exception("The document is reference material");
        }
        if (documentUserRepository.findByDocumentAndUser(document, user) != null) {
            throw new Exception("Cannot book several copies of the document");
        }
        int maxWeeksNum = maxWeeksNum(document, user);
        if (preferredWeeksNum > maxWeeksNum) {
            throw new Exception("The preferred number of weeks is too big");
        }
        DocumentUser documentUser = new DocumentUser(document, user, preferredWeeksNum);
        documentUserRepository.save(documentUser);
        document.getUsers().add(documentUser);
        documentRepository.save(document);

        emailSendService.sendEmail(user, BOOKED_SUBJ, BOOKED_MESSAGE);
    }

    /**
     * supporting method that adds weeks to the date
     * @param date the Date which has to be increased
     * @param weekNum Integer number of weeks by which the date should be increased
     * @return the increased Date
     */
    private Date addWeeks(Date date, Integer weekNum) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 7 * weekNum);
        return cal.getTime();
    }

    /**
     * method that finds the maximum number of weeks the user can check out the document for
     * works properly only for documents and users for whom it is possible to count maxWeeksNum
     * @param document Document that is being checked out
     * @param user User who checks out the document
     * @return Integer number of weeks
     */
    private Integer maxWeeksNum(Document document, User user) {
        if (user.getRole().getName().equals("Visiting Professor")) {
            return 1;
        }
        if (document.getType().getName().equals("Book")) {
            if (user.getRole().getName().equals("Instructor") || user.getRole().getName().equals("TA") ||
                    user.getRole().getName().equals("Professor")) {
                return 4;
            } else if (document.isBestseller()) {
                return 2;
            } else {
                return 3;
            }
        } else {
            return 2;
        }
    }

    public void checkOut(Document document, User user) {
    }

    private void outstandingRequest(Document document) {

    }


}
