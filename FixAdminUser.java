import java.sql.*;

public class FixAdminUser {
    public static void main(String[] args) {
        try {
            Connection con = Connect.connect();
            
            // First, let's insert the admin user
            System.out.println("Inserting admin user...");
            PreparedStatement stmt = con.prepareStatement(
                "INSERT IGNORE INTO USERS (USERNAME, PASSWORD, EMAIL, PHONE, ROLE, ADDRESS) VALUES (?, ?, ?, ?, ?, ?)"
            );
            stmt.setString(1, "admin");
            stmt.setString(2, "admin");
            stmt.setString(3, "admin@library.com");
            stmt.setString(4, "1234567890");
            stmt.setString(5, "admin");
            stmt.setString(6, "Library Administration Office");
            
            int result = stmt.executeUpdate();
            
            if (result > 0) {
                System.out.println("✅ Admin user inserted successfully!");
            } else {
                System.out.println("ℹ️ Admin user already exists or insert ignored");
            }
            
            // Check all users now
            System.out.println("\n=== All Users After Fix ===");
            Statement checkStmt = con.createStatement();
            ResultSet rs = checkStmt.executeQuery("SELECT ID, USERNAME, PASSWORD, ROLE FROM USERS");
            
            System.out.println("ID | USERNAME | PASSWORD | ROLE");
            System.out.println("---|----------|----------|-----");
            
            while (rs.next()) {
                System.out.println(rs.getInt("ID") + " | " + rs.getString("USERNAME") + " | " + rs.getString("PASSWORD") + " | " + rs.getString("ROLE"));
            }
            
            con.close();
            System.out.println("\n🎉 You can now login with admin/admin!");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
