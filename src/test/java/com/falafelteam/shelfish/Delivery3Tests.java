package com.falafelteam.shelfish;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import com.falafelteam.shelfish.model.DocumentUser;
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
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Delivery3Tests {

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
    private DocumentUserRepository documentUserRepository;
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

    public void deleteVseK_huyam() {
        authorRepository.deleteAll();
        documentUserRepository.deleteAll();
        documentRepository.deleteAll();
        documentTypeRepository.deleteAll();
        editorRepository.deleteAll();
        publisherRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void test1() throws Exception {
        // init

        initialState();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        User p1 = userService.getByName("Sergey Afonso");
        Document d1 = documentService.getByName("Introduction to Algorithms");
        Document d2 = documentService.getByName("Design Patterns: Elements of Reusable Object-Oriented Software");
        bookingService.book(d1, p1, 4);
        bookingService.book(d2, p1, 4);
        bookingService.checkOut(d1, p1, simpleDateFormat.parse("2018-03-05"));
        bookingService.checkOut(d2, p1, simpleDateFormat.parse("2018-03-05"));

        // action

        int fine = bookingService.calculateFine(documentUserRepository.findByDocumentAndUser(d2, p1));
        bookingService.returnDocument(d2, p1);

        // check

        assert (fine == 200);
        assert (documentUserRepository.findByDocumentAndUser(d2, p1) == null);
        assert (!d2.getUsers().contains(p1));
        deleteVseK_huyam();
    }

    @Test
    public void test2() throws Exception {
        // init
        initialState();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // i
        User p1 = userService.getByName("Sergey Afonso");
        Document d1 = documentService.getByName("Introduction to Algorithms");
        Document d2 = documentService.getByName("Design Patterns: Elements of Reusable Object-Oriented Software");
        bookingService.book(d1, p1, 4);
        bookingService.book(d2, p1, 4);
        bookingService.checkOut(d1, p1, simpleDateFormat.parse("2018-03-05"));
        bookingService.checkOut(d2, p1, simpleDateFormat.parse("2018-03-05"));
        //ii
        User s = userService.getByName("Andrey Velo");
        d1 = documentService.getByName("Introduction to Algorithms");
        d2 = documentService.getByName("Design Patterns: Elements of Reusable Object-Oriented Software");
        bookingService.book(d1, s, 3);
        bookingService.book(d2, s, 2);
        bookingService.checkOut(d1, s, simpleDateFormat.parse("2018-03-05"));
        bookingService.checkOut(d2, s, simpleDateFormat.parse("2018-03-05"));
        DocumentUser docUs700 = documentUserRepository.findByDocumentAndUser(d1, s);
        //iii
        User v = userService.getByName("Veronika Rama");
        d1 = documentService.getByName("Introduction to Algorithms");
        d2 = documentService.getByName("Design Patterns: Elements of Reusable Object-Oriented Software");
        bookingService.book(d1, v, 1);
        docUs700 = documentUserRepository.findByDocumentAndUser(d1, s);
        bookingService.book(d2, v, 1);
        bookingService.checkOut(d1, v, simpleDateFormat.parse("2018-03-05"));
        bookingService.checkOut(d2, v, simpleDateFormat.parse("2018-03-05"));

        // action
        int fine1 = bookingService.calculateFine(documentUserRepository.findByDocumentAndUser(d1, p1));
        int fine2 = bookingService.calculateFine(documentUserRepository.findByDocumentAndUser(d2, p1));
        docUs700 = documentUserRepository.findByDocumentAndUser(d1, s);
        int fine3 = bookingService.calculateFine(documentUserRepository.findByDocumentAndUser(d1, s));
        docUs700 = documentUserRepository.findByDocumentAndUser(d1, s);
        int fine4 = bookingService.calculateFine(documentUserRepository.findByDocumentAndUser(d2, s));
        int fine5 = bookingService.calculateFine(documentUserRepository.findByDocumentAndUser(d1, v));
        int fine6 = bookingService.calculateFine(documentUserRepository.findByDocumentAndUser(d2, v));

        //check
        assert (fine1 == 200);
        assert (fine2 == 200);
        assert (fine3 == 900);
        assert (fine4 == 1600);
        assert (fine5 == 2300);
        assert (fine6 == 1700);

        deleteVseK_huyam();
    }

    @Test
    public void test3() throws Exception {

        // init

        initialState();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // i

        User p1 = userService.getByName("Sergey Afonso");
        Document d1 = documentService.getByName("Introduction to Algorithms");
        bookingService.book(d1, p1, 4);
        bookingService.checkOut(d1, p1, simpleDateFormat.parse("2018-03-29"));

        //ii

        User s = userService.getByName("Andrey Velo");
        Document d2 = documentService.getByName("Design Patterns: Elements of Reusable Object-Oriented Software");
        bookingService.book(d2, s, 2);
        bookingService.checkOut(d2, s, simpleDateFormat.parse("2018-03-29"));

        //iii

        User v = userService.getByName("Veronika Rama");
        d2 = documentService.getByName("Design Patterns: Elements of Reusable Object-Oriented Software");
        bookingService.book(d2, v, 1);
        bookingService.checkOut(d2, v, simpleDateFormat.parse("2018-03-29"));

        // action

        d1 = documentService.getByName("Introduction to Algorithms");
        bookingService.renewDocument(d1, p1, simpleDateFormat.parse("2018-04-02"));
        d2 = documentService.getByName("Design Patterns: Elements of Reusable Object-Oriented Software");
        bookingService.renewDocument(d2, s, simpleDateFormat.parse("2018-04-02"));
        d2 = documentService.getByName("Design Patterns: Elements of Reusable Object-Oriented Software");
        bookingService.renewDocument(d2, v, simpleDateFormat.parse("2018-04-02"));

        String date1 = simpleDateFormat.format(bookingService.getDueDate(documentUserRepository.findByDocumentAndUser(d1, p1)));
        String date2 = simpleDateFormat.format(bookingService.getDueDate(documentUserRepository.findByDocumentAndUser(d2, s)));
        String date3 = simpleDateFormat.format(bookingService.getDueDate(documentUserRepository.findByDocumentAndUser(d2, v)));

        // check

        assert (date1.equals("2018-04-30"));
        assert (date2.equals("2018-04-16"));
        assert (date3.equals("2018-04-09"));

        deleteVseK_huyam();
    }

    @Test
    public void test4() throws Exception {
        // init
        initialState();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        User p1 = userService.getByName("Sergey Afonso");
        Document d1 = documentService.getByName("Introduction to Algorithms");
        bookingService.book(d1, p1);
        bookingService.checkOut(d1, p1, simpleDateFormat.parse("2018-03-29"));
        User s = userService.getByName("Andrey Velo");
        Document d2 = documentService.getByName("Design Patterns: Elements of Reusable Object-Oriented Software");
        bookingService.book(d2, s);
        bookingService.checkOut(d2, s, simpleDateFormat.parse("2018-03-29"));
        User v = userService.getByName("Veronika Rama");
        d2 = documentService.getByName("Design Patterns: Elements of Reusable Object-Oriented Software");
        bookingService.book(d2, v);
        bookingService.checkOut(d2, v, simpleDateFormat.parse("2018-03-29"));

        // action
        d2 = documentService.getByName("Design Patterns: Elements of Reusable Object-Oriented Software");
        bookingService.outstandingRequest(d2);
        d1 = documentService.getByName("Introduction to Algorithms");
        try {
            bookingService.renewDocument(d1, p1, simpleDateFormat.parse("2018-04-02"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        d2 = documentService.getByName("Design Patterns: Elements of Reusable Object-Oriented Software");
        try {
            bookingService.renewDocument(d2, s, simpleDateFormat.parse("2018-04-02"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        d2 = documentRepository.findByName("Design Patterns: Elements of Reusable Object-Oriented Software");
        try {
            bookingService.renewDocument(d2, v, simpleDateFormat.parse("2018-04-02"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        d1 = documentService.getByName("Introduction to Algorithms");
        d2 = documentRepository.findByName("Design Patterns: Elements of Reusable Object-Oriented Software");
        String date1 = simpleDateFormat.format(bookingService.getDueDate(documentUserRepository.findByDocumentAndUser(d1, p1)));
        String date2 = simpleDateFormat.format(bookingService.getDueDate(documentUserRepository.findByDocumentAndUser(d2, s)));
        String date3 = simpleDateFormat.format(bookingService.getDueDate(documentUserRepository.findByDocumentAndUser(d2, v)));

        //check
        assert (date1.equals("2018-04-30"));
        assert (date2.equals("2018-04-12"));
        assert (date3.equals("2018-04-05"));

        deleteVseK_huyam();
    }

    @Test
    public void test5() throws Exception {

        // init

        initialState();

        // i

        User p1 = userService.getByName("Sergey Afonso");
        Document d3 = documentRepository.findByName("Null References: The Billion Dollar Mistake");
        bookingService.book(d3, p1);
        bookingService.checkOut(d3, p1);

        // ii

        User s = userService.getByName("Andrey Velo");
        d3 = documentRepository.findByName("Null References: The Billion Dollar Mistake");
        bookingService.book(d3, s);
        bookingService.checkOut(d3, s);

        // iii

        User v = userService.getByName("Veronika Rama");
        d3 = documentRepository.findByName("Null References: The Billion Dollar Mistake");
        bookingService.book(d3, v);

        // check

        assert (bookingService.getWaitingList(d3).contains(v));
        assert (!bookingService.getWaitingList(d3).contains(p1));
        assert (!bookingService.getWaitingList(d3).contains(s));

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
