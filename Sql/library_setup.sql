-- Library Management System Database Setup
-- Enhanced version with additional fields for better functionality
-- Created: July 2025

CREATE DATABASE IF NOT EXISTS library;
USE library;

-- Create enhanced users table with additional fields
CREATE TABLE IF NOT EXISTS USERS (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    USERNAME VARCHAR(50) NOT NULL UNIQUE,
    PASSWORD VARCHAR(50) NOT NULL,
    EMAIL VARCHAR(100),
    PHONE VARCHAR(20),
    ROLE VARCHAR(20) DEFAULT 'user',
    ADDRESS TEXT
);

-- Insert default admin and sample users
INSERT IGNORE INTO USERS (USERNAME, PASSWORD, EMAIL, PHONE, ROLE, ADDRESS) VALUES 
('admin', 'admin', 'admin@library.com', '1234567890', 'admin', 'Library Administration Office'),
('Aditya', 'hello', 'aditya@email.com', '9876543210', 'librarian', 'Sample Address 123'),
('john_doe', 'password123', 'john@email.com', '5555123456', 'user', '456 User Street'),
('jane_smith', 'mypass456', 'jane@email.com', '5555789012', 'user', '789 Reader Lane');

-- Create enhanced books table with additional fields
CREATE TABLE IF NOT EXISTS BOOKS (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(100) NOT NULL,
    AUTHOR VARCHAR(100) NOT NULL,
    PUBLISHER VARCHAR(100),
    ISBN VARCHAR(20),
    CATEGORY VARCHAR(50),
    DESCRIPTION TEXT,
    STATUS VARCHAR(10) DEFAULT 'available'
);

-- Sample books with enhanced data
INSERT IGNORE INTO BOOKS (NAME, AUTHOR, PUBLISHER, ISBN, CATEGORY, DESCRIPTION, STATUS) VALUES
('The Alchemist', 'Paulo Coelho', 'HarperCollins', '978-0062315007', 'Fiction', 'A philosophical story about following your dreams and finding your personal legend.', 'available'),
('Clean Code', 'Robert C. Martin', 'Prentice Hall', '978-0132350884', 'Technology', 'A handbook of agile software craftsmanship with practical advice on writing clean, maintainable code.', 'available'),
('Data Structures and Algorithms', 'Seymour Lipschutz', 'McGraw-Hill', '978-0070380127', 'Education', 'Comprehensive guide to data structures and algorithms with examples and exercises.', 'available'),
('To Kill a Mockingbird', 'Harper Lee', 'J.B. Lippincott & Co.', '978-0061120084', 'Fiction', 'A classic American novel dealing with themes of racial injustice and moral growth.', 'available'),
('Introduction to Algorithms', 'Thomas H. Cormen', 'MIT Press', '978-0262033848', 'Technology', 'Comprehensive textbook covering algorithms and data structures used in computer science.', 'available'),
('1984', 'George Orwell', 'Secker & Warburg', '978-0452284234', 'Fiction', 'Dystopian novel about totalitarianism and surveillance in a future society.', 'available'),
('The Art of Computer Programming', 'Donald E. Knuth', 'Addison-Wesley', '978-0201896831', 'Technology', 'Multi-volume work covering many kinds of programming algorithms and their analysis.', 'available'),
('Pride and Prejudice', 'Jane Austen', 'T. Egerton', '978-0141439518', 'Fiction', 'Classic romance novel about manners, marriage, and social class in 19th century England.', 'available');

-- Create enhanced issue tracking table
CREATE TABLE IF NOT EXISTS ISSUE (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    BOOKID INT NOT NULL,
    USERID INT NOT NULL,
    ISSUEDATE DATE NOT NULL,
    RETURNDATE DATE,
    FOREIGN KEY (BOOKID) REFERENCES BOOKS(ID) ON DELETE CASCADE,
    FOREIGN KEY (USERID) REFERENCES USERS(ID) ON DELETE CASCADE
);

-- Sample issued books data (optional - for testing)
INSERT IGNORE INTO ISSUE (BOOKID, USERID, ISSUEDATE, RETURNDATE) VALUES
(1, 2, '2025-07-01', '2025-07-15'),
(3, 3, '2025-07-10', '2025-07-24');

-- Update book status for issued books
UPDATE BOOKS SET STATUS = 'issued' WHERE ID IN (
    SELECT DISTINCT BOOKID FROM ISSUE WHERE BOOKID IS NOT NULL
);

-- Create indexes for better performance
CREATE INDEX idx_users_username ON USERS(USERNAME);
CREATE INDEX idx_books_name ON BOOKS(NAME);
CREATE INDEX idx_books_author ON BOOKS(AUTHOR);
CREATE INDEX idx_books_category ON BOOKS(CATEGORY);
CREATE INDEX idx_books_status ON BOOKS(STATUS);
CREATE INDEX idx_issue_bookid ON ISSUE(BOOKID);
CREATE INDEX idx_issue_userid ON ISSUE(USERID);
CREATE INDEX idx_issue_dates ON ISSUE(ISSUEDATE, RETURNDATE);

-- Display setup completion message
SELECT 'Library Management System Database Setup Complete!' as Message;
SELECT 'Default Admin: username=admin, password=admin' as AdminCredentials;
SELECT 'Sample User: username=Aditya, password=hello' as SampleUser;
