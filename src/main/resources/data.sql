INSERT INTO document_type
(id, name)
VALUES
(1, "Article"),
(2, "Audio/Video Material"),
(3, "Book");

INSERT INTO role
(id, name, priority)
VALUES
(1, "LibrarianPriv1", 0),
(2, "LibrarianPriv2", 0),
(3, "LibrarianPriv3", 0),
(4, "Student", 1),
(5, "Instructor", 2),
(6, "TA", 3),
(7, "Visiting Professor", 4),
(8, "Professor", 5);