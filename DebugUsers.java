import java.sql.*;

public class DebugUsers {
    public static void main(String[] args) {
        try {
            Connection con = Connect.connect();
            Statement stmt = con.createStatement();
            
            System.out.println("=== DEBUG: Checking USERS table ===");
            
            // Check if table exists and what data is in it
            ResultSet rs = stmt.executeQuery("SELECT * FROM USERS");
            
            System.out.println("Users in database:");
            System.out.println("ID | USERNAME | PASSWORD | ROLE");
            System.out.println("---|----------|----------|-----");
            
            while (rs.next()) {
                int id = rs.getInt("ID");
                String username = rs.getString("USERNAME");
                String password = rs.getString("PASSWORD");
                String role = rs.getString("ROLE");
                System.out.println(id + " | " + username + " | " + password + " | " + role);
            }
            
            // Test specific login query
            System.out.println("\n=== Testing login query for admin/admin ===");
            PreparedStatement loginStmt = con.prepareStatement("SELECT * FROM USERS WHERE USERNAME=? AND PASSWORD=?");
            loginStmt.setString(1, "admin");
            loginStmt.setString(2, "admin");
            ResultSet loginRs = loginStmt.executeQuery();
            
            if (loginRs.next()) {
                System.out.println("✅ Login query successful!");
                System.out.println("Found user: " + loginRs.getString("USERNAME") + " with role: " + loginRs.getString("ROLE"));
            } else {
                System.out.println("❌ Login query failed - no matching user found");
            }
            
            con.close();
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
