package com.falafelteam.shelfish.controller;

import com.falafelteam.shelfish.model.AuthorKinds.Editor;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.model.documents.DocumentType;
import com.falafelteam.shelfish.repository.DocumentTypeRepository;
import com.falafelteam.shelfish.service.BookingService;
import com.falafelteam.shelfish.service.DocumentService;
import com.falafelteam.shelfish.service.DocumentTypeService;
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
    private final DocumentTypeService documentTypeService;

    @Autowired
    public IndexController(DocumentService documentService, UserService userService, BookingService bookingService,
                           DocumentTypeService documentTypeService) {
        this.documentService = documentService;
        this.userService = userService;
        this.bookingService = bookingService;
        this.documentTypeService = documentTypeService;
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
        return "document";
    }

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable("id") int id, Model model) throws Exception {
        model.addAttribute("user", userService.getById(id));
        return "user";
    }

    @GetMapping("/addDocument")
    public String addDocument(Model model) throws Exception {
        DocumentForm form = new DocumentForm();
        model.addAttribute("document", form);
        model.addAttribute("types", documentTypeService.getAllTypes());
        return "add_document";
    }

    @PostMapping("/addDocument")
    public String addDocument(@ModelAttribute("document") DocumentForm documentForm) throws Exception {
        documentForm.validate();
        Document document;
        DocumentType documentType = documentTypeService.getByName(documentForm.getType());
        if (documentForm.getType().equals("Article")) {
            document = new Document(documentForm.getName(), documentForm.getDescription(), documentForm.getIsBestseller(),
                    documentForm.getCopies(), documentForm.getIsReference(), new Publisher(documentForm.getPublisher()),
                    new Editor(documentForm.getEditor()), documentType, documentForm.getTags());
        } else if (documentForm.getType().equals("AV")) {
            document = new Document(documentForm.getName(), documentForm.getDescription(), documentForm.getIsBestseller(),
                    documentForm.getCopies(), documentForm.getIsReference(), documentForm.getParsedAuthors(), documentType,
                    documentForm.getTags());
        } else if (documentForm.getType().equals("Book")) {
            document = new Document(documentForm.getName(), documentForm.getDescription(), documentForm.getIsBestseller(),
                    documentForm.getCopies(),documentForm.getIsReference(), documentForm.getParsedAuthors(),
                    new Publisher(documentForm.getPublisher()), documentType, documentForm.getTags());
        }
        return "redirect:/";
    }

    @GetMapping("/modifyDocument/{id}")
    public String modifyDocumet(@PathVariable("id") int id, Model model) throws Exception {
        model.addAttribute("document", documentService.getById(id));
        //documentService.save(documentService.getById(id));
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

    @GetMapping("/search")
    public String search() {
        return "search";
    }
}
