import java.sql.*;

public class SimpleAdminFix {
    public static void main(String[] args) {
        try {
            Connection con = Connect.connect();
            
            // Insert admin user with just the basic columns that exist
            System.out.println("Inserting admin user (basic)...");
            PreparedStatement stmt = con.prepareStatement(
                "INSERT IGNORE INTO USERS (USERNAME, PASSWORD, ROLE) VALUES (?, ?, ?)"
            );
            stmt.setString(1, "admin");
            stmt.setString(2, "admin");
            stmt.setString(3, "admin");
            
            int result = stmt.executeUpdate();
            
            if (result > 0) {
                System.out.println("✅ Admin user inserted successfully!");
            } else {
                System.out.println("ℹ️ Admin user already exists or insert ignored");
            }
            
            // Check users
            System.out.println("\n=== Current Users ===");
            Statement checkStmt = con.createStatement();
            ResultSet rs = checkStmt.executeQuery("SELECT ID, USERNAME, PASSWORD, ROLE FROM USERS ORDER BY ID");
            
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("ID") + " | User: " + rs.getString("USERNAME") + " | Pass: " + rs.getString("PASSWORD") + " | Role: " + rs.getString("ROLE"));
            }
            
            con.close();
            System.out.println("\n🎉 Try login with admin/admin now!");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
