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

@Service
public class BookingService {

    private final DocumentUserRepository documentUserRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingService(DocumentUserRepository documentUserRepository, DocumentRepository documentRepository, UserRepository userRepository) {
        this.documentUserRepository = documentUserRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
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
