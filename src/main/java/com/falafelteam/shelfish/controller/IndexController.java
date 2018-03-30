package com.falafelteam.shelfish.controller;

import com.falafelteam.shelfish.model.documents.Document;
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

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Kak dilishke";
    }

<<<<<<< HEAD
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
    public String addDocument() {
        return "add_document";
    }

    @PostMapping("/addDocument")
    public String addDocument(@ModelAttribute("document") Document document) {
        documentService.save(document);
        return "redirect:/addDocument";
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
