package com.falafelteam.shelfish.controller;

import com.falafelteam.shelfish.model.AuthorKinds.Editor;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import com.falafelteam.shelfish.model.DocumentUser;
import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.model.documents.DocumentType;
import com.falafelteam.shelfish.model.users.Role;
import com.falafelteam.shelfish.model.users.User;
import com.falafelteam.shelfish.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * Class that handles http requests
 */
@Controller
@RequestMapping("/")
public class IndexController {

    private final DocumentService documentService;
    private final UserService userService;
    private final DocumentUserService documentUserService;
    private final BookingService bookingService;
    private final DocumentTypeService documentTypeService;
    private final RoleService roleService;
    private final LoggingService loggingService;

    @Autowired
    public IndexController(DocumentService documentService, UserService userService, BookingService bookingService,
                           DocumentTypeService documentTypeService, RoleService roleService, LoggingService loggingService,
                           DocumentUserService documentUserService)
            throws IOException {
        this.documentService = documentService;
        this.userService = userService;
        this.bookingService = bookingService;
        this.documentTypeService = documentTypeService;
        this.roleService = roleService;
        this.loggingService = loggingService;
        this.documentUserService = documentUserService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/searchDocument";
    }

    @GetMapping("/document/{id}")
    public String getDocument(@PathVariable("id") int id, Model model) throws Exception {
        model.addAttribute("document", documentService.getById(id));
        return "document";
    }

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable("id") int id, Model model, Principal principal) throws Exception {
        model.addAttribute("user", userService.getById(id));
        model.addAttribute("documents", documentUserService.getByUser(userService.getById(id)));
        model.addAttribute("form", new BookingForm());
        model.addAttribute(bookingService);
        if (principal.getName().equals("shelfishuser") || userService.getByLogin(principal.getName()).getId().equals(id)) {
            return "user";
        } else {
            if (userService.getByLogin(principal.getName()).getRole().getName().contains("Librarian")){
                return "user";
            } else {
                return "redirect:/error";
            }
        }
    }

    @GetMapping("/myProfile")
    public String myProfile(Principal principal) throws Exception {
        return "redirect:/user/" + userService.getByLogin(principal.getName()).getId();
    }

    @GetMapping("/addDocument")
    public String addDocument(Model model) {
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
        loggingService.newDocumentLog(document);
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
        loggingService.modifiedDocumentLog(document);
        return "redirect:/document/" + id;
    }

    /**
     * method that forms Document instances out of the DocumentForm information
     *
     * @param documentForm - document where the information about the new object is stored
     * @return object of type Document
     * @throws Exception "Wrong document type"
     */
    private Document documentFormToDocument(DocumentForm documentForm) throws Exception {
        Document document;
        DocumentType documentType = documentTypeService.getByName(documentForm.getType());
        switch (documentForm.getType()) {
            case "Article":
                document = new Document(documentForm.getName(), documentForm.getDescription(), documentForm.getIsBestseller(),
                        documentForm.getCopies(), documentForm.getPrice(), documentForm.getIsReference(),
                        documentForm.getParsedAuthors(), new Publisher(documentForm.getPublisher()),
                        new Editor(documentForm.getEditor()), documentType, documentForm.getTags(), documentForm.getDate());
                break;
            case "AV":
                document = new Document(documentForm.getName(), documentForm.getDescription(),
                        documentForm.getCopies(), documentForm.getPrice(), documentForm.getIsReference(),
                        documentForm.getParsedAuthors(), documentType, documentForm.getTags());
                break;
            case "Book":
                document = new Document(documentForm.getName(), documentForm.getDescription(), documentForm.getIsBestseller(),
                        documentForm.getCopies(), documentForm.getPrice(), documentForm.getIsReference(),
                        documentForm.getParsedAuthors(), new Publisher(documentForm.getPublisher()), documentType,
                        documentForm.getTags(), documentForm.getDate());
                break;
            default:
                throw new Exception("Wrong document type");
        }
        return document;
    }

    @GetMapping("/deleteDocument/{id}")
    public String deleteDocument(@PathVariable("id") int id) throws Exception {
        loggingService.deletedDocument(documentService.getById(id));
        documentService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/addUser")
    public String signUp(Model model) {
        UserForm form = new UserForm();
        model.addAttribute("user", form);
        model.addAttribute("roles", roleService.getAllRoles());
        return "add_user";
    }

    @PostMapping("/addUser")
    public String signUp(@ModelAttribute("user") UserForm form) throws Exception {
        form.validate();
        Role role = roleService.getByName(form.getRole());
        User user = new User(form.getName(), form.getLogin(), form.getPassword(), form.getAddress(),
                form.getPhoneNumber(), role);
        userService.register(user);
        loggingService.signUpLog(user);
        return "redirect:/user/" + user.getId();
    }

    @PostMapping("/modifyUser/{id}")
    public String modifyUsers(@PathVariable("id") int id, Model model) throws Exception {
        model.addAttribute("user", userService.getById(id));
        userService.save(userService.getById(id));
        loggingService.modifiedUserLog(userService.getById(id));
        return "redirect:/user/" + id;
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") int id) throws Exception {
        loggingService.deletedUserLog(userService.getById(id));
        userService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/searchDocument")
    public String searchDocument(Model model) {
        model.addAttribute("documents", documentService.getAll());
        model.addAttribute("types", documentTypeService.getAllTypes());
        model.addAttribute("form", new DocumentSearchForm());
        return "search_document";
    }

    @PostMapping("/searchDocument")
    public String searchDocument(Model model, @ModelAttribute("form") DocumentSearchForm form) {
        List<Document> docs;
//        switch(form.getSearchCriteria()){
//            case "Title":
//                if(form.getType().equals("All")) docs = documentService.searchByName(form.getSearch());
//                else docs = documentService.searchByName(form.getSearch(), form.getType());
//                break;
//            case "Author":
//                if(form.getType().equals("All")) docs = documentService.searchByName(form.getSearch());
//                else docs = documentService.searchByName(form.getSearch(), form.getType());
//            case "Publisher":
//
//            default:
//        }
        docs = documentService.searchByName(form.getSearch());
        model.addAttribute("documents", docs);
        model.addAttribute("types", documentTypeService.getAllTypes());
        model.addAttribute("form", new DocumentSearchForm());
        return "search_document";
    }

    @GetMapping("/searchUser")
    public String searchUser(Model model) {
        model.addAttribute("form", new SearchByIdForm());
        return "search_user";
    }

    @PostMapping("searchUser")
    public String searchUser(@ModelAttribute("form") SearchByIdForm form) {
        return "redirect:/user/" + form.getId();
    }

    @PostMapping("/book")
    public String book(@ModelAttribute("document") Document document, Principal principal) throws Exception {
        User user = userService.getByLogin(principal.getName());
        document = documentService.getById(document.getId());
        bookingService.book(document, user);
        return "redirect:/user/" + user.getId();
    }

    @PostMapping("/checkOut")
    public String checkOut(@ModelAttribute("form") BookingForm form) throws Exception {
        Document document = documentService.getById(form.getDocumentId());
        User user = userService.getById(form.getUserId());
        bookingService.checkOut(document, user);
        return "redirect:/user/" + user.getId();
    }

    @PostMapping("/returnDocument")
    public String returnDocument(@ModelAttribute("form") BookingForm form) throws Exception {
        Document document = documentService.getById(form.getDocumentId());
        User user = userService.getById(form.getUserId());
        bookingService.returnDocument(document, user);
        return "redirect:/user/" + user.getId();
    }

    @PostMapping("renewDocument")
    public String renewDocument(@ModelAttribute("form") BookingForm form) throws Exception {
        Document document = documentService.getById(form.getDocumentId());
        User user = userService.getById(form.getUserId());
        bookingService.renewDocument(document, user);
        return "redirect:/user/" + user.getId();
    }
}
