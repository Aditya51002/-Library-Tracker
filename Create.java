import java.sql.*;

public class Create {
    public static void create() {
        try {
            Connection con = Connect.connect();
            Statement stmt = con.createStatement();

            // Create enhanced users table with additional fields
            String createUserTable = "CREATE TABLE IF NOT EXISTS USERS(" +
                "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                "USERNAME VARCHAR(50) NOT NULL UNIQUE, " +
                "PASSWORD VARCHAR(50) NOT NULL, " +
                "EMAIL VARCHAR(100), " +
                "PHONE VARCHAR(20), " +
                "ROLE VARCHAR(20) DEFAULT 'user', " +
                "ADDRESS TEXT)";

            // Create enhanced books table with additional fields
            String createBookTable = "CREATE TABLE IF NOT EXISTS BOOKS(" +
                "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                "NAME VARCHAR(100) NOT NULL, " +
                "AUTHOR VARCHAR(100) NOT NULL, " +
                "PUBLISHER VARCHAR(100), " +
                "ISBN VARCHAR(20), " +
                "CATEGORY VARCHAR(50), " +
                "DESCRIPTION TEXT, " +
                "STATUS VARCHAR(10) DEFAULT 'available')";

            // Create enhanced issue tracking table with foreign keys
            String createIssueTable = "CREATE TABLE IF NOT EXISTS ISSUE(" +
                "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                "BOOKID INT NOT NULL, " +
                "USERID INT NOT NULL, " +
                "ISSUEDATE DATE NOT NULL, " +
                "RETURNDATE DATE, " +
                "FOREIGN KEY (BOOKID) REFERENCES BOOKS(ID) ON DELETE CASCADE, " +
                "FOREIGN KEY (USERID) REFERENCES USERS(ID) ON DELETE CASCADE)";

            // Execute table creation
            stmt.executeUpdate(createUserTable);
            stmt.executeUpdate(createBookTable);
            stmt.executeUpdate(createIssueTable);

            // Insert default admin and sample users
            stmt.executeUpdate("INSERT IGNORE INTO USERS (USERNAME, PASSWORD, EMAIL, PHONE, ROLE, ADDRESS) VALUES " +
                "('admin', 'admin', 'admin@library.com', '1234567890', 'admin', 'Library Administration Office')");
            
            stmt.executeUpdate("INSERT IGNORE INTO USERS (USERNAME, PASSWORD, EMAIL, PHONE, ROLE, ADDRESS) VALUES " +
                "('Aditya', 'hello', 'aditya@email.com', '9876543210', 'librarian', 'Sample Address 123')");
            
            stmt.executeUpdate("INSERT IGNORE INTO USERS (USERNAME, PASSWORD, EMAIL, PHONE, ROLE, ADDRESS) VALUES " +
                "('john_doe', 'password123', 'john@email.com', '5555123456', 'user', '456 User Street')");
            
            stmt.executeUpdate("INSERT IGNORE INTO USERS (USERNAME, PASSWORD, EMAIL, PHONE, ROLE, ADDRESS) VALUES " +
                "('jane_smith', 'mypass456', 'jane@email.com', '5555789012', 'user', '789 Reader Lane')");

            // Sample books with enhanced data
            stmt.executeUpdate("INSERT IGNORE INTO BOOKS (NAME, AUTHOR, PUBLISHER, ISBN, CATEGORY, DESCRIPTION, STATUS) VALUES " +
                "('The Alchemist', 'Paulo Coelho', 'HarperCollins', '978-0062315007', 'Fiction', 'A philosophical story about following your dreams and finding your personal legend.', 'available')");
            
            stmt.executeUpdate("INSERT IGNORE INTO BOOKS (NAME, AUTHOR, PUBLISHER, ISBN, CATEGORY, DESCRIPTION, STATUS) VALUES " +
                "('Clean Code', 'Robert C. Martin', 'Prentice Hall', '978-0132350884', 'Technology', 'A handbook of agile software craftsmanship with practical advice on writing clean, maintainable code.', 'available')");
            
            stmt.executeUpdate("INSERT IGNORE INTO BOOKS (NAME, AUTHOR, PUBLISHER, ISBN, CATEGORY, DESCRIPTION, STATUS) VALUES " +
                "('Data Structures and Algorithms', 'Seymour Lipschutz', 'McGraw-Hill', '978-0070380127', 'Education', 'Comprehensive guide to data structures and algorithms with examples and exercises.', 'available')");
            
            stmt.executeUpdate("INSERT IGNORE INTO BOOKS (NAME, AUTHOR, PUBLISHER, ISBN, CATEGORY, DESCRIPTION, STATUS) VALUES " +
                "('To Kill a Mockingbird', 'Harper Lee', 'J.B. Lippincott & Co.', '978-0061120084', 'Fiction', 'A classic American novel dealing with themes of racial injustice and moral growth.', 'available')");
            
            stmt.executeUpdate("INSERT IGNORE INTO BOOKS (NAME, AUTHOR, PUBLISHER, ISBN, CATEGORY, DESCRIPTION, STATUS) VALUES " +
                "('Introduction to Algorithms', 'Thomas H. Cormen', 'MIT Press', '978-0262033848', 'Technology', 'Comprehensive textbook covering algorithms and data structures used in computer science.', 'available')");

            // Create indexes for better performance
            try {
                stmt.executeUpdate("CREATE INDEX idx_users_username ON USERS(USERNAME)");
                stmt.executeUpdate("CREATE INDEX idx_books_name ON BOOKS(NAME)");
                stmt.executeUpdate("CREATE INDEX idx_books_category ON BOOKS(CATEGORY)");
                stmt.executeUpdate("CREATE INDEX idx_books_status ON BOOKS(STATUS)");
            } catch (Exception indexEx) {
                // Indexes might already exist, ignore
            }

            System.out.println("✅ Database and tables created successfully with sample data!");
            System.out.println("✅ Default Admin: username=admin, password=admin");
            System.out.println("✅ Sample User: username=Aditya, password=hello");
            
        } catch (Exception e) {
            System.out.println("❌ Database setup error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
