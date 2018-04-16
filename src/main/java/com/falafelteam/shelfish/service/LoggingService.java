package com.falafelteam.shelfish.service;

import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.model.users.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A logger class. By default logs everything to /tmp/log.txt
 */
public class LoggingService {
    private final String logFileName = "tmp/log.txt" ;
    private FileWriter tempWriter;
    private BufferedWriter pr;

    public LoggingService() throws IOException {
        tempWriter = new FileWriter("input.txt", true);
        pr = new BufferedWriter(tempWriter);
    }

    public void bookLog(User user, Document document) throws IOException {
        printTime();
        pr.append("User id: " + user.getId() + " booked document id: " + document.getId());
    }

    public void checkOutLog(User user, Document document)throws IOException{
        printTime();
        pr.append("User id: " + user.getId() + " checked out document id: " + document.getId());    }

    public void renewLog(User user, Document document)throws IOException{
        printTime();
        pr.append("User id: " + user.getId() + " renewed document id: " + document.getId());
    }

    public void returnLog(User user, Document document)throws IOException{
        printTime();
        pr.append("User id: " + user.getId() + " returned document id: " + document.getId());
    }

    public void outstandingLog(Document document)throws IOException{
        printTime();
        pr.append("Outstanding request thrown on document id " + document.getId());
    }

    public void signUpLog(User user)throws IOException{
        printTime();
        pr.append("User id " + user.getId() + " signed up");
    }

    public void deletedUserLog(User user)throws IOException{
        printTime();
        pr.append("User id " + user.getId() + " deleted");
    }

    public void newDocumentLog(Document document)throws IOException{
        printTime();
        pr.append("Document id " + document.getId() + " created");

    }

    public void deletedDocument(Document document)throws IOException{
        printTime();
        pr.append("Document id " + document.getId() + " deleted");
    }

    public void startUpLog()throws IOException{
        printTime();
        pr.append("System launched");
    }

    public void clearLog()throws IOException{
        tempWriter = new FileWriter("input.txt");
        pr = new BufferedWriter(tempWriter);
        pr.write("");
        pr.close();
        tempWriter = new FileWriter("input.txt", true);
        pr = new BufferedWriter(tempWriter);
    }

    public void printTime() throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date now = new Date();
        pr.append("[");
        pr.append(format.format(now));
        pr.append("] ");
    }

    public void tellWhatIsLog() throws IOException {
        printTime();
        pr.append("what is log; baby don't hurt me; don't hurt me; no more;");
        pr.append("http://asciilove.ytmnd.com/");
    }
}
