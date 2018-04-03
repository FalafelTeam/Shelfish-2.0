package com.falafelteam.shelfish.controller;

import com.falafelteam.shelfish.model.AuthorKinds.Editor;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.model.documents.DocumentType;
import com.falafelteam.shelfish.model.users.Role;
import com.falafelteam.shelfish.model.users.User;
import com.falafelteam.shelfish.repository.DocumentTypeRepository;
import com.falafelteam.shelfish.service.*;
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
    private final RoleService roleService;

    @Autowired
    public IndexController(DocumentService documentService, UserService userService, BookingService bookingService,
                           DocumentTypeService documentTypeService, RoleService roleService) {
        this.documentService = documentService;
        this.userService = userService;
        this.bookingService = bookingService;
        this.documentTypeService = documentTypeService;
        this.roleService = roleService;
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
        Document document = documentFormToDocument(documentForm);
        documentService.add(document);
        document = documentService.getByName(document.getName());
        return "redirect:/document/" + document.getId();
    }

    @GetMapping("/modifyDocument/{id}")
    public String modifyDocument(@PathVariable("id") int id, Model model) throws Exception {
        DocumentForm form = new DocumentForm();
        model.addAttribute("old", documentService.getById(id));
        model.addAttribute("document", form);
        return "modify_document";
    }

    @PostMapping("/modifyDocument/{id}")
    public String modifyDocument(@ModelAttribute("document") DocumentForm documentForm,
                                 @ModelAttribute("old") Document oldDocument,
                                 @PathVariable("id") int id) throws Exception {
        documentForm.validate();
        Document document = documentFormToDocument(documentForm);
        documentService.modify(oldDocument, document);
        return "redirect:/document/" + id;
    }

    private Document documentFormToDocument(DocumentForm documentForm) throws Exception {
        Document document;
        DocumentType documentType = documentTypeService.getByName(documentForm.getType());
        switch (documentForm.getType()) {
            case "Article":
                document = new Document(documentForm.getName(), documentForm.getDescription(), documentForm.getIsBestseller(),
                        documentForm.getCopies(), documentForm.getIsReference(), documentForm.getParsedAuthors(),
                        new Publisher(documentForm.getPublisher()), new Editor(documentForm.getEditor()), documentType,
                        documentForm.getTags(), documentForm.getDate());
                break;
            case "AV":
                document = new Document(documentForm.getName(), documentForm.getDescription(), documentForm.getIsBestseller(),
                        documentForm.getCopies(), documentForm.getIsReference(), documentForm.getParsedAuthors(), documentType,
                        documentForm.getTags());
                break;
            case "Book":
                document = new Document(documentForm.getName(), documentForm.getDescription(), documentForm.getIsBestseller(),
                        documentForm.getCopies(), documentForm.getIsReference(), documentForm.getParsedAuthors(),
                        new Publisher(documentForm.getPublisher()), documentType, documentForm.getTags(), documentForm.getDate());
                break;
            default: throw new Exception("Wrong document type");
        }
        return document;
    }

    @GetMapping("/deleteDocument/{id}")
    public String deleteDocument(@PathVariable("id") int id) {
        documentService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/signUp")
    public String signUp(Model model) {
        UserForm form = new UserForm();
        model.addAttribute("user", form);
        model.addAttribute("roles", roleService.gelAllRoles());
        return "sign_up";
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute("user") UserForm form) throws Exception {
        form.validate();
        Role role = roleService.getByName(form.getRole());
        User user = new User(form.getName(), form.getLogin(), form.getPassword(), form.getAddress(),
                form.getPhoneNumber(), role);
        userService.save(user);
        return "redirect:/";
    }

    @PostMapping("/modifyUser/{id}")
    public String modifyUsers(@PathVariable("id") int id, Model model) throws Exception {
        model.addAttribute("user", userService.getById(id));
        userService.save(userService.getById(id));
        return "redirect:/user/" + id;
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

    @GetMapping("/searchById")
    public String searchById(Model model) {
        model.addAttribute("form", new SearchByIdForm());
        return "search_by_id";
    }

    @PostMapping("/book")
    public String book(@ModelAttribute("document") Document document, @ModelAttribute("user") User user,
                       @ModelAttribute("weeksNum") int weeksNum) throws Exception {
        bookingService.book(document, user, weeksNum);
        return "redirect:/user/" + user.getId();
    }
}
