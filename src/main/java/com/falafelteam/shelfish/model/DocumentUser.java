package com.falafelteam.shelfish.model;

import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.model.users.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class DocumentUser {
    private final String statusNEW = "new";
    private final String statusTAKEN = "taken";
    private final String statusRENEWED = "renewed";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Date date;
    private Integer weekNum;
    private String status;

    public DocumentUser(Document document, User user, int weekNum) {
        this.document = document;
        this.user = user;
        this.status = statusNEW;
        this.date = new Date();
        this.weekNum = weekNum;
    }
}
