package com.falafelteam.shelfish;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.model.documents.DocumentType;
import com.falafelteam.shelfish.repository.DocumentUserRepository;
import com.falafelteam.shelfish.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Delivery3Tests {

    @Autowired
    private DocumentUserRepository documentUserRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentTypeService documentTypeService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    private void initialState() throws Exception {

        // database

        documentTypeService.add("Article");
        documentTypeService.add("Audio/Video Material");
        documentTypeService.add("Book");

        roleService.add("Librarian", 0);
        roleService.add("Student", 1);
        roleService.add("Instructor", 2);
        roleService.add("TA", 3);
        roleService.add("Visiting Professor", 4);
        roleService.add("Professor", 5);

        // documents
        List<Author> authors = new LinkedList<>();
        authors.add(new Author("Thomas H. Cormen"));
        authors.add(new Author("Charles E. Leiserson"));
        authors.add(new Author("Ronald L. Rives"));
        authors.add(new Author("Clifford Stein"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        Document d1 = new Document("Introduction to Algorithms", "", false, 3,
                false, authors, new Publisher("MIT Press"), documentTypeService.getByName("Book"),
                "", simpleDateFormat.parse("2009"));
        documentService.add(d1);
    }

    @Test
    public void test1(){
        
    }

    @Test
    public void test2(){

    }

    @Test
    public void test3(){

    }

    @Test
    public void test4(){

    }

    @Test
    public void test5(){

    }

    @Test
    public void test6(){

    }

    @Test
    public void test7(){

    }

    @Test
    public void test8(){

    }

    @Test
    public void test9(){

    }

    @Test
    public void test10(){

    }
}
