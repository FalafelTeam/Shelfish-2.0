package com.falafelteam.shelfish.controller;

import com.falafelteam.shelfish.model.AuthorKinds.Editor;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import com.falafelteam.shelfish.model.documents.Article;
import com.falafelteam.shelfish.model.documents.Book;
import com.falafelteam.shelfish.service.BookingService;
import com.falafelteam.shelfish.service.DocumentService;
import com.falafelteam.shelfish.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class IndexController {

    private final DocumentService documentService;

    private final UserService userService;

    private final BookingService bookingService;

    @Autowired
    public IndexController(DocumentService documentService, UserService userService, BookingService bookingService) {
        this.documentService = documentService;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/allDocuments")
    public String allDocuments() {
        return "all_documents";
    }
    
    @GetMapping("/document/{id}")
    public String getDocument(@PathVariable("id") int id, Model model) throws Exception {
        model.addAttribute("document", documentService.getById(id));
        if (documentService.getById(id) instanceof Book) {
            return "book";
        }
        return "document";
    }

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable("id") int id, Model model) throws Exception {
        model.addAttribute("user", userService.getById(id));
        return "user";
    }

    @GetMapping("/addDocument")
    public String addDocument(Model model) {
        model.addAttribute("document", new DocumentForm());
        return "add_document";
    }

    @PostMapping("/addDocument")
    public String addDocument(@ModelAttribute("document") DocumentForm documentForm) {
        if (documentForm.getAuthors().equals("") && !documentForm.getEditor().equals("") &&
                !documentForm.getPublisher().equals("")) {
            Article document = new Article(documentForm.getName(), documentForm.getIsBestseller(),
                    documentForm.getCopies(), documentForm.getPrice(), documentForm.getIsReference(),
                    new Editor(documentForm.getEditor()), new Publisher(documentForm.getPublisher()));
        }
        System.out.println(documentForm.getType());
        //documentService.save(document);
        return "redirect:/";
    }

    @GetMapping("/modifyDocument/{id}")
    public String modifyDocumet(@PathVariable("id") int id, Model model) throws Exception {
        model.addAttribute("document", documentService.getById(id));
        documentService.save(documentService.getById(id));
        return "redirect:/document/" + id;
    }

    @PostMapping("/modifyUser/{id}")
    public String modifyUsers(@PathVariable("id") int id, Model model) throws Exception {
        model.addAttribute("user", userService.getById(id));
        userService.save(userService.getById(id));
        return "redirect:/user/" + id;
    }

    @GetMapping("/deleteDocument/{id}")
    public String deleteDocument(@PathVariable("id") int id) {
        documentService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/outstanding/{documentId}")
    public String outstandingRequest(@PathVariable("documentId") int id) {
        bookingService.outstandingRequest(id);
        return "redirect:/document/" + id;
    }

    @GetMapping("/search")
    public String search() {
        return "search";
    }
}