INSERT INTO document_type
(id, name)
VALUES
(1, "Article"),
(2, "Audio/Video Material"),
(3, "Book");

INSERT INTO role
(id, name, priority)
VALUES
(1, "Admin", 0),
(2, "LibrarianPriv1", 0),
(3, "LibrarianPriv2", 0),
(4, "LibrarianPriv3", 0),
(5, "Student", 1),
(6, "Instructor", 2),
(7, "TA", 3),
(8, "Visiting Professor", 4),
(9, "Professor", 5);