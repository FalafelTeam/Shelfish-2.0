package com.falafelteam.shelfish;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import com.falafelteam.shelfish.model.DocumentUser;
import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.model.documents.DocumentType;
import com.falafelteam.shelfish.model.users.Role;
import com.falafelteam.shelfish.model.users.User;
import com.falafelteam.shelfish.repository.*;
import com.falafelteam.shelfish.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShelfishApplicationTests {

	@Autowired
	DocumentTypeService documentTypeService;
	@Autowired
	DocumentService documentService;
	@Autowired
	DocumentRepository documentRepository;
	@Autowired
	AuthorRepository authorRepository;
	@Autowired
	BookingService bookingService;
	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	DocumentUserRepository documentUserRepository;

	@Test
	public void test1() throws Exception {
		documentTypeService.add("Book");
		DocumentType documentType = documentTypeService.getByName("Book");
		LinkedList<Author> author = new LinkedList<Author>();
		author.add(new Author("Lev Tolstoy"));
		Document doc = new Document("War and Peace", "About war and peace", false, 1, false,
				author, new Publisher("RusKniga"),
				documentType, "kik, lol, haha, war, peace, obviously");
		documentService.add(doc);
		assert(documentRepository.findByName("War and Peace") != null);
		assert(authorRepository.findByName("Lev Tolstoy") != null);

		Role role = new Role("Student", 1);
		roleRepository.save(role);
		User user = new User("Alexey Karachev", "k.alexeika@innopolis.ru", "111111", "", "+79179377299", role);
		userService.save(user);
		assert(userRepository.findByName("Alexey Karachev") != null);

		bookingService.book(doc, user, 2);

		DocumentUser docUser = documentUserRepository.findByDocumentAndUser(doc, user);
		assert(docUser != null);
		assert(docUser.getStatus().equals("new"));
		assert(docUser.getStatus().equals(docUser.getStatusNEW()));

		bookingService.checkOut(doc, user);

		assert (docUser.getStatus().equals(docUser.getStatusNEW()));
	}

}
