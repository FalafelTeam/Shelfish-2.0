package com.falafelteam.shelfish.service;

import com.falafelteam.shelfish.model.DocumentUser;
import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.model.users.User;
import com.falafelteam.shelfish.repository.DocumentRepository;
import com.falafelteam.shelfish.repository.DocumentUserRepository;
import com.falafelteam.shelfish.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * A service class for the booking system
 */
@Service
public class BookingService {

    private final DocumentUserRepository documentUserRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final EmailSendService emailSendService;

    @Autowired
    public BookingService(DocumentUserRepository documentUserRepository, DocumentRepository documentRepository, UserRepository userRepository) {
        this.documentUserRepository = documentUserRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.emailSendService = new EmailSendService();
    }

    /**
     * method for booking a document in the library
     *
     * @param document          - document that is to be booked
     * @param user              - user who wants to book the document
     * @param preferredWeeksNum - number of weeks for which the user prefers to book the document
     * @throws Exception "The document is reference material"
     *                   "Cannot book several copies of document"
     *                   "The preferred number of weeks is too big"
     */
    public void book(Document document, User user, Integer preferredWeeksNum) throws Exception {
        if (user.getRole().getPriority() == 0) {
            outstandingRequest(document);
            return;
        }
        if (document.isReference()) {
            throw new Exception("The document is reference material. Don't dare to try to take this.");
        }
        if (documentUserRepository.findByDocumentAndUser(document, user) != null) {
            throw new Exception("Cannot book several copies of the document. I mean really, why would you do that?");
        }
        int maxWeeksNum = maxWeeksNum(document, user);
        if (preferredWeeksNum > maxWeeksNum) {
            throw new Exception("The preferred number of weeks is too big. Hold on buddy, too much for you.");
        }
        DocumentUser documentUser = new DocumentUser(document, user, preferredWeeksNum);
        documentUserRepository.save(documentUser);
        document.addToQueue(documentUser);
        documentRepository.save(document);

        emailSendService.sendEmail(user, BOOKED_SUBJ, BOOKED_MESSAGE);
    }

    /**
     * method for booking a document in the library when the preferred number of weeks is not stated
     *
     * @param document - document that is to be booked
     * @param user     - user who wants to book the document
     * @throws Exception "The document is reference material"
     *                   "Cannot book several copies of document"
     */
    public void book(Document document, User user) throws Exception {
        book(document, user, maxWeeksNum(document, user));
    }

    /**
     * method that adds weeks to the date
     *
     * @param date    - date which has to be increased
     * @param weekNum - number of weeks by which the date should be increased
     * @return the increased date
     */
    private Date addWeeks(Date date, Integer weekNum) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 7 * weekNum);
        return cal.getTime();
    }

    /**
     * method that finds the maximum number of weeks the user can check out the document for,
     * works properly only for pairs of document and user for which it is possible to count maximum weeks number
     *
     * @param document - document that is being checked out
     * @param user     - user who checks out the document
     * @return number of weeks
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

    /**
     * method for checking out a document
     *
     * @param document - document that is to be checked out
     * @param user     - user who wants to check out the document
     * @throws Exception "Document wasn't booked at all. Trying to cheat?"
     *                   "Your booking has expired! You have been deleted from the queue."
     *                   "Not yet available. Too far in a queue"
     *                   "You have the document on hands! Don't cheat with us!"
     */
    public void checkOut(Document document, User user) throws Exception {
        checkOut(document, user, new Date());
    }

    /**
     * method for checking out a document when the date of checking out is stated
     *
     * @param document - document that is to be checked out
     * @param user     - user who wants to check out the document
     * @param date     - the date of the checking out
     * @throws Exception "Document wasn't booked at all. Trying to cheat?"
     *                   "Your booking has expired! You have been deleted from the queue."
     *                   "Not yet available. Too far in a queue"
     *                   "You have the document on hands! Don't cheat with us!"
     */
    public void checkOut(Document document, User user, Date date) throws Exception {
        DocumentUser docUser = documentUserRepository.findByDocumentAndUser(document, user);
        if (docUser == null) {
            throw new Exception("Document wasn't booked at all. Trying to cheat?");
        }

        if (docUser.getStatus().equals(docUser.getStatusNEW())) {
            if (checkIfAvailableToCheckOut(document, user)) {
                docUser.setStatus(docUser.getStatusTAKEN());
                if ((new Date().getTime() - docUser.getDate().getTime()) / (24 * 60 * 60 * 1000) > 1) {
                    docUser.getDocument().removeFromQueue(docUser);
                    throw new Exception("Your booking has expired! You have been deleted from the queue.");
                }
                docUser.setDate(date);
                documentUserRepository.save(docUser);
                emailSendService.sendEmail(user, CHECKOUT_SUBJ, CHECKOUT_MESSAGE);
            } else {
                throw new Exception("Not yet available. Too far in a queue. Ya tolko sprosit'");
            }
        } else if (docUser.getStatus().equals(docUser.getStatusRENEWED()) || docUser.getStatus() == docUser.getStatusTAKEN()) {
            throw new Exception("You have the document on hands! Don't cheat with us!");
        }
    }

    /**
     * method for returning a document
     *
     * @param document - document that is to be returned
     * @param user     - user who wants to return the document
     * @throws Exception "Document wasn't booked"
     *                   "Cannot return the document. Pay a fine first."
     */
    public void returnDocument(Document document, User user) throws Exception {
        DocumentUser docUser = documentUserRepository.findByDocumentAndUser(document, user);
        if (docUser.getStatus() == null || docUser.getStatus().equals(docUser.getStatusNEW())) {
            throw new Exception("Document wasn't booked. It's a simple pattern. Book-checkout-return");
        }
        if (calculateFine(docUser) != 0) {
            throw new Exception("Cannot return the document. Pay a fine first.");
        }
        userRepository.save(user);
        documentRepository.save(document);
        documentUserRepository.deleteById(docUser.getId());

    }

    /**
     * method for renewing the document
     *
     * @param document - document that is to be renewed
     * @param user     - user who wants to renew the document
     * @throws Exception "Renew of the document is currently unavailable. Please return the document to the library as soon as possible"
     *                   "Document wasn't booked"
     *                   "Document was already renewed once"
     *                   "The document is overdue, renew is forbidden"
     */
    public void renewDocument(Document document, User user) throws Exception {
        renewDocument(document, user, new Date());
    }

    /**
     * method for renewing the document when the date is stated
     *
     * @param document - document that is to be renewed
     * @param user     - user who wants to renew the document
     * @param date     - the date of the document renewal
     * @throws Exception "Renew of the document is currently unavailable. Please return the document to the library as soon as possible"
     *                   "Document wasn't booked"
     *                   "Document was already renewed once"
     *                   "The document is overdue, renew is forbidden"
     */
    public void renewDocument(Document document, User user, Date date) throws Exception {
        DocumentUser docUser = documentUserRepository.findByDocumentAndUser(document, user);
        if (document.isHasOutstanding()) {
            throw new Exception("Renew of the document is currently unavailable. Please return the document to the library as soon as possible");
        }
        if (docUser == null || docUser.getStatus().equals(docUser.getStatusNEW())) {
            throw new Exception("Document wasn't booked. It's a simple pattern. Book-checkout-return");
        }
        if (docUser.getStatus().equals(docUser.getStatusRENEWED()) && !docUser.getUser().getRole().equals("Visiting Professor")) {
            throw new Exception("Document was already renewed once. What takes you so long to read?");
        }
        if (calculateFine(docUser, date) != 0) {
            int fine = calculateFine(docUser);
            throw new Exception("The document is overdue, renew is forbidden. Hint: pay then book again. May work if you're lucky.");
        }
        docUser.setStatus(docUser.getStatusRENEWED());
        docUser.setWeekNum(maxWeeksNum(document, user));
        docUser.setDate(date);
        documentUserRepository.save(docUser);

        emailSendService.sendEmail(user, RENEW_SUBJ, RENEW_MESSAGE);
    }

    /**
     * method for throwing an outstanding request
     *
     * @param document - document that is to be thrown an outstanding request on
     */
    public void outstandingRequest(Document document) {
        LinkedList<User> deleted = getWaitingList(document);
        deleteNotTakenFromQueue(document);
        document.setHasOutstanding(true);
        for (User user : deleted) {
            emailSendService.sendEmail(user, OUTSTANDING_SUBJ, OUTSTANDING_MESSAGE);
        }
        documentRepository.save(document);
    }

    /**
     * method for checking if the document is available to be checked out by the user
     *
     * @param document - document that is to be checked
     * @param user     - user who is to be checked
     * @return if the document is available to be checked out by the user
     * @throws Exception "The user is not in the queue"
     */
    private boolean checkIfAvailableToCheckOut(Document document, User user) throws Exception {
        int priority = user.getRole().getPriority();
        DocumentUser checkedDocumentUser = documentUserRepository.findByDocumentAndUser(document, user);
        if (!checkedDocumentUser.getStatus().equals("new")) {
            throw new Exception("The user is not in the queue. Ya tolko sprosit' ");
        }
        List<DocumentUser> documentUsers = documentUserRepository.findAllByDocumentAndStatus(document, "new");
        List<DocumentUser> upper = new LinkedList<>();
        for (DocumentUser documentUser : documentUsers) {
            if (documentUser.getUser().getRole().getPriority() < priority || (
                    documentUser.getUser().getRole().getPriority() == priority &&
                            documentUser.getDate().before(checkedDocumentUser.getDate()))) {
                upper.add(documentUser);
            }
        }
        if (upper.size() < availableCopies(document)) {
            return true;
        } else return false;
    }

    /**
     * method for calculating available copies of the document
     *
     * @param document - document available copies of which are to be calculated
     * @return number of available copies of the document
     */
    public int availableCopies(Document document) {
        if (document.isReference()) {
            return 0;
        }
        if (document.isHasOutstanding()) {
            return 0;
        }
        return document.getCopies() - documentUserRepository.findAllByDocumentAndStatus(document, "taken").size() -
                documentUserRepository.findAllByDocumentAndStatus(document, "renewed").size();
    }

    /**
     * method for calculating fine
     *
     * @param docUser - document and user relation for which the fine is to be calculated
     * @return fine in rubles
     */
    public int calculateFine(DocumentUser docUser) {
        long diff = new Date().getTime() - docUser.getDate().getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffDays <= docUser.getWeekNum() * 7) {
            return 0;
        }
        int fine = (int) (diffDays - docUser.getWeekNum() * 7) * 100;
        if (fine > docUser.getDocument().getPrice()) {
            return docUser.getDocument().getPrice();
        }
        return fine;
    }

    /**
     * method for calculating fine when the date is stated
     *
     * @param documentUser - document and user relation for which the fine is to be calculated
     * @param date         - date on which the fine is to be calculated
     * @return fine in rubles
     */
    public int calculateFine(DocumentUser documentUser, Date date) {
        long diff = date.getTime() - documentUser.getDate().getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffDays <= documentUser.getWeekNum() * 7) {
            return 0;
        }
        int fine = (int) (diffDays - documentUser.getWeekNum() * 7) * 100;
        if (fine > documentUser.getDocument().getPrice()) {
            return documentUser.getDocument().getPrice();
        }
        return fine;
    }

    /**
     * method for getting the due date
     *
     * @param docUser - document and user relation for which the due date is to be calculated
     * @return the due date
     */
    public Date getDueDate(DocumentUser docUser) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(docUser.getDate());
        calendar.add(Calendar.DAY_OF_YEAR, docUser.getWeekNum() * 7);
        return calendar.getTime();
    }

    /**
     * method for getting the waiting list of the document
     *
     * @param document - document for which the waiting list is to be got
     * @return the waiting list
     */
    public LinkedList<User> getWaitingList(Document document) {
        LinkedList<User> waitingList = new LinkedList<>();
        for (DocumentUser docUs : document.getUsers()) {
            if (docUs.getStatus().equals(docUs.getStatusNEW())) {
                waitingList.add(docUs.getUser());
            }
        }
        return waitingList;
    }

    /**
     * method for deleting the users who do not have the document on hands from the documentUser relations of the document
     *
     * @param document - document for which the users are to be deleted
     */
    public void deleteNotTakenFromQueue(Document document) {
        LinkedList<DocumentUser> toBeDeleted = new LinkedList<>();
        for (DocumentUser documentUser : document.getUsers()) {
            if (documentUser.getStatus().equals(documentUser.getStatusNEW())) {
                toBeDeleted.add(documentUser);
            }
        }
        document.getUsers().removeAll(toBeDeleted);
        documentRepository.save(document);
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
