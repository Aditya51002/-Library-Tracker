import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("Testing MySQL connection...");
        
        // Test 1: Try connecting without specifying database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true", "root", "");
            System.out.println("✅ MySQL server is running and accessible!");
            
            // Test 2: Try creating library database
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS library");
            System.out.println("✅ Library database created successfully!");
            
            // Test 3: Try connecting to library database
            con.close();
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library?useSSL=false&allowPublicKeyRetrieval=true", "root", "");
            System.out.println("✅ Connected to library database successfully!");
            
            con.close();
            System.out.println("🎉 All connection tests passed! Your MySQL setup is working.");
            
        } catch (Exception e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
            System.out.println("\nPossible solutions:");
            System.out.println("1. Make sure XAMPP is running");
            System.out.println("2. Start Apache and MySQL services in XAMPP Control Panel");
            System.out.println("3. Check if MySQL is running on port 3306");
            e.printStackTrace();
        }
    }
}
