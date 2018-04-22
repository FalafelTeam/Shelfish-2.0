INSERT INTO document_type
(id, name)
VALUES
(1, "Article"),
(2, "Audio/Video Material"),
(3, "Book");

INSERT INTO role
(id, name, priority)
VALUES
(1, "Librarian", 0),
(2, "Student", 1),
(3, "Instructor", 2),
(4, "TA", 3),
(5, "Visiting Professor", 4),
(6, "Professor", 5);