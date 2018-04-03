package com.falafelteam.shelfish;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.model.users.User;
import com.falafelteam.shelfish.repository.*;
import com.falafelteam.shelfish.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
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

    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    DocumentTypeRepository documentTypeRepository;
    @Autowired
    EditorRepository editorRepository;
    @Autowired
    PublisherRepository publisherRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");

        List<Author> authors = new LinkedList<>();
        authors.add(new Author("Thomas H. Cormen"));
        authors.add(new Author("Charles E. Leiserson"));
        authors.add(new Author("Ronald L. Rives"));
        authors.add(new Author("Clifford Stein"));
        Document d1 = new Document("Introduction to Algorithms", "", false, 3,
                5000, false, authors, new Publisher("MIT Press"),
                documentTypeService.getByName("Book"), "", simpleDateFormat.parse("2009"));
        documentService.add(d1);

        authors = new LinkedList<>();
        authors.add(new Author("Erich Gamma"));
        authors.add(new Author("Ralph Johnson"));
        authors.add(new Author("John Vlissides"));
        authors.add(new Author("Richard Helm"));
        Document d2 = new Document("Design Patterns: Elements of Reusable Object-Oriented Software", "",
                true, 3, 1700, false, authors,
                new Publisher("Addison-Wesley Professional"), documentTypeService.getByName("Book"),
                "", simpleDateFormat.parse("2003"));
        documentService.add(d2);

        authors = new LinkedList<>();
        authors.add(new Author("Tony Hoare"));
        Document d3 = new Document("Null References: The Billion Dollar Mistake", "", 2,
                700, false, authors, documentTypeService.getByName("Audio/Video Material"), "");
        documentService.add(d3);

        // users

        userService.save(new User("Sergey Afonso", "s.afonso@inoopolis.ru", "123456",
                "Via Margutta, 3", "30001", roleService.getByName("Professor")));
        userService.save(new User("Nadia Teixeira", "n.teixeira@innopolis.ru", "123456",
                "Via Sacra, 13", "30002", roleService.getByName("Professor")));
        userService.save(new User("Elvira Espindola", "e.espindola@innopolis.ru", "123456",
                "Via del Corso, 22", "30003", roleService.getByName("Professor")));
        userService.save(new User("Andrey Velo", "a.velo@innopolis.ru", "123456",
                "Avenida Mazatlan, 250", "30004", roleService.getByName("Student")));
        userService.save(new User("Veronika Rama", "v.rama@innopolis.ru", "123456",
                "Stret Atocha, 27", "30005", roleService.getByName("Visiting Professor")));
    }

    public void deleteVseK_huyam(){
        authorRepository.deleteAll();
        documentRepository.deleteAll();
        documentTypeRepository.deleteAll();
        documentUserRepository.deleteAll();
        editorRepository.deleteAll();
        publisherRepository.deleteAll();
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void test1() throws Exception {
        // init

        initialState();
        User p1 = userService.getByName("Sergey Afonso");
        Document d1 = documentService.getByName("Introduction to Algorithms");
        Document d2 = documentService.getByName("Design Patterns: Elements of Reusable Object-Oriented Software");
        bookingService.book(d1, p1, 4);
        bookingService.book(d2, p1, 4);
        bookingService.checkOut(d1, p1, new Date(2018, 3, 5));
        bookingService.checkOut(d2, p1, new Date(2018, 3, 5));

        // action

        int fine = bookingService.calculateFine(documentUserRepository.findByDocumentAndUser(d2, p1));
        bookingService.returnDocument(d2, p1);

        // check

        assert (fine == 0);
        assert (documentUserRepository.findByDocumentAndUser(d2, p1) == null);
        assert (!d2.getUsers().contains(p1));
        deleteVseK_huyam();
    }

    @Test
    public void test2() throws Exception {
        // init
        initialState();
        // i
        User p1 = userService.getByName("Sergey Afonso");
        Document d1 = documentService.getByName("Introduction to Algorithms");
        Document d2 = documentService.getByName("Design Patterns: Elements of Reusable Object-Oriented Software");
        bookingService.book(d1, p1, 4);
        bookingService.book(d2, p1, 4);
        bookingService.checkOut(d1, p1, new Date(2018, 3, 5));
        bookingService.checkOut(d2, p1, new Date(2018, 3, 5));
        //ii
        User s = userService.getByName("Andrey Velo");
        bookingService.book(d1, s, 2);
        bookingService.book(d2, s, 2);
        bookingService.checkOut(d1, s, new Date(2018, 3, 5));
        bookingService.checkOut(d2, s, new Date(2018, 3, 5));
        //iii
        User v = userService.getByName("Veronika Rama");
        bookingService.book(d1, v, 2);
        bookingService.book(d2, v, 2);
        bookingService.checkOut(d1, v, new Date(2018, 3, 5));
        bookingService.checkOut(d2, v, new Date(2018, 3, 5));

        // action
        int fine1 = bookingService.calculateFine(documentUserRepository.findByDocumentAndUser(d1, p1));
        int fine2 = bookingService.calculateFine(documentUserRepository.findByDocumentAndUser(d2, p1));
        int fine3 = bookingService.calculateFine(documentUserRepository.findByDocumentAndUser(d1, s));
        int fine4 = bookingService.calculateFine(documentUserRepository.findByDocumentAndUser(d2, s));
        int fine5 = bookingService.calculateFine(documentUserRepository.findByDocumentAndUser(d1, v));
        int fine6 = bookingService.calculateFine(documentUserRepository.findByDocumentAndUser(d2, v));

        //check
        assert (fine1 == 0);
        assert (fine2 == 0);
        assert (fine3 == 700);
        assert (fine4 == 1400);
        assert (fine5 == 2100);
        assert (fine6 == 1700);

        deleteVseK_huyam();
    }

    @Test
    public void test3() throws Exception {
        initialState();



        deleteVseK_huyam();
    }

    @Test
    public void test4() throws Exception {
        initialState();



        deleteVseK_huyam();
    }

    @Test
    public void test5() throws Exception {
        initialState();



        deleteVseK_huyam();
    }

    @Test
    public void test6() throws Exception {
        initialState();



        deleteVseK_huyam();
    }

    @Test
    public void test7() throws Exception {
        initialState();



        deleteVseK_huyam();
    }

    @Test
    public void test8() throws Exception {
        initialState();



        deleteVseK_huyam();
    }

    @Test
    public void test9() throws Exception {
        initialState();



        deleteVseK_huyam();
    }

    @Test
    public void test10() throws Exception {
        initialState();



        deleteVseK_huyam();
    }
}
