package com.falafelteam.shelfish.service;

import com.falafelteam.shelfish.model.DocumentUser;
import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.model.users.User;
import com.falafelteam.shelfish.repository.DocumentRepository;
import com.falafelteam.shelfish.repository.DocumentUserRepository;
import com.falafelteam.shelfish.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    private final LoggingService logService;

    @Autowired
    public BookingService(DocumentUserRepository documentUserRepository, DocumentRepository documentRepository,
                          UserRepository userRepository) throws IOException {
        this.documentUserRepository = documentUserRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.emailSendService = new EmailSendService();
        this.logService = new LoggingService();
    }

    /**
     * method that runs "scripts" at 4AM every day
     */
    @Scheduled(cron = "0 20 4 * * *")
    private void scheduled() throws Exception {
        sendOneDayLeftEmails();
        sendFirstFineEmails();
        sendAvailableToCheckOutEmails();
        deleteOverdueBookings();
    }

    private void sendOneDayLeftEmails() throws IOException {
        for(DocumentUser docUser : documentUserRepository.findAll()){
            if(getDueDate(docUser).getTime() >= new Date().getTime()-86400000){
                emailSendService.sendEmail(docUser.getUser(), emailSendService.getDAYTILLRETURN_SUBJ(), emailSendService.getDAYTILLRETURN_MESSAGE());
            }
        }
    }

    private void sendFirstFineEmails() throws IOException {
        for(DocumentUser docUser : documentUserRepository.findAll()){
            if(getDueDate(docUser).getTime() >= new Date().getTime() && getDueDate(docUser).getTime() <= new Date().getTime()+86400000){
                emailSendService.sendEmail(docUser.getUser(), emailSendService.getFIRSTFINE_SUBJ(), emailSendService.getFIRSTFINE_MESSAGE());
            }
        }
    }
    private void sendAvailableToCheckOutEmails() throws Exception {
        for(DocumentUser docUser : documentUserRepository.findAll()){
            if(checkIfAvailableToCheckOut(docUser.getDocument(), docUser.getUser())){
                emailSendService.sendEmail(docUser.getUser(), emailSendService.getAVAIL_SUBJ(), emailSendService.getAVAIL_MESSAGE());
            }
        }
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

        emailSendService.sendEmail(user, emailSendService.getBOOKED_SUBJ(), emailSendService.getBOOKED_MESSAGE());
        logService.bookLog(user, document);
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
     * method that deletes all overdue bookings from the database
     */
    private void deleteOverdueBookings() {
        Iterable<DocumentUser> documentUsers = documentUserRepository.findAll();
        for (DocumentUser documentUser: documentUsers) {
            if (documentUser.getStatus().equals(documentUser.getStatusNEW())) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(documentUser.getDate());
                cal.add(Calendar.DATE, 1);
                if (cal.getTime().before(new Date())) {
                    userRepository.save(documentUser.getUser());
                    documentRepository.save(documentUser.getDocument());
                    documentUserRepository.delete(documentUser);
                }
            }
        }
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
                    throw new Exception("Client's booking has expired! The client has been deleted from the queue.");
                }
                document.setAvailableCopies(document.getAvailableCopies()-1);
                docUser.setDate(date);
                documentUserRepository.save(docUser);
                emailSendService.sendEmail(user, emailSendService.getCHECKOUT_SUBJ(), emailSendService.getCHECKOUT_MESSAGE());
                logService.checkOutLog(user, document);
            } else {
                throw new Exception("Not yet available. Too far in a queue.");
            }
        } else if (docUser.getStatus().equals(docUser.getStatusRENEWED()) ||
                docUser.getStatus().equals(docUser.getStatusTAKEN())) {
            throw new Exception("Client has the document on hands.");
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
        emailSendService.sendEmail(user, emailSendService.getRETURNED_SUBJ(), emailSendService.getRETURNED_MESSAGE());
        logService.returnLog(user, document);
        document.setAvailableCopies(document.getAvailableCopies()+1);
        userRepository.save(user);
        documentRepository.save(document);
        documentUserRepository.deleteById(docUser.getId());

    }

    /**
     * method for renewing the document
     *
     * @param document - document that is to be renewed
     * @param user     - user who wants to renew the document
     * @throws Exception "Renew of the document is currently unavailable. Please return the document to the library as
     *                      soon as possible"
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
     * @throws Exception "Renew of the document is currently unavailable. Please return the document to the library as
     *                      soon as possible"
     *                   "Document wasn't booked"
     *                   "Document was already renewed once"
     *                   "The document is overdue, renew is forbidden"
     */
    public void renewDocument(Document document, User user, Date date) throws Exception {
        DocumentUser docUser = documentUserRepository.findByDocumentAndUser(document, user);
        if (document.isHasOutstanding()) {
            throw new Exception("Renew of the document is currently unavailable. Please return the document to the " +
                                     "library as soon as possible");
        }
        if (docUser == null || docUser.getStatus().equals(docUser.getStatusNEW())) {
            throw new Exception("Document wasn't booked. It's a simple pattern. Book-checkout-return");
        }
        if (docUser.getStatus().equals(docUser.getStatusRENEWED())) {
            throw new Exception("Document was already renewed once. What takes you so long to read?");
        }
        if (calculateFine(docUser, date) != 0) {
            int fine = calculateFine(docUser);
            throw new Exception("The document is overdue, renew is forbidden. Hint: pay then book again. " +
                                     "May work if you're lucky.");
        }
        if (!docUser.getUser().getRole().equals("Visiting Professor")) {
            docUser.setStatus(docUser.getStatusRENEWED());
        }
        docUser.setWeekNum(maxWeeksNum(document, user));
        docUser.setDate(date);
        documentUserRepository.save(docUser);

        emailSendService.sendEmail(user, emailSendService.getRENEW_SUBJ(), emailSendService.getRENEW_MESSAGE());
        logService.renewLog(user, document);
    }

    /**
     * method for throwing an outstanding request
     *
     * @param document - document that is to be thrown an outstanding request on
     */
    public void outstandingRequest(Document document) throws IOException {
        LinkedList<User> deleted = getWaitingList(document);
        deleteNotTakenFromQueue(document);
        document.setHasOutstanding(true);
        logService.outstandingLog(document);
        for (User user : deleted) {
            emailSendService.sendEmail(user, emailSendService.getOUTSTANDING_SUBJ(), emailSendService.getOUTSTANDING_MESSAGE());

        }
        for (DocumentUser docUser : document.getUsers()){
            emailSendService.sendEmail(docUser.getUser(), emailSendService.getOUTSTAND_RETURN_REQUEST_SUBJ(), emailSendService.getOUTSTAND_RETURN_REQUEST_MESSAGE());
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
        if (document.isReference() || document.isHasOutstanding()) {
            return 0;
        }
        return document.getAvailableCopies();
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
     * method for deleting the users who do not have the document on hands from the documentUser relations of the
     * document
     *
     * @param document - document for which the users are to be deleted
     */
    private void deleteNotTakenFromQueue(Document document) {
        LinkedList<DocumentUser> toBeDeleted = new LinkedList<>();
        for (DocumentUser documentUser : document.getUsers()) {
            if (documentUser.getStatus().equals(documentUser.getStatusNEW())) {
                toBeDeleted.add(documentUser);
            }
        }
        document.getUsers().removeAll(toBeDeleted);
        documentRepository.save(document);
    }

}
