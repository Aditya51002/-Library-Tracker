import java.sql.*;

public class CheckTableStructure {
    public static void main(String[] args) {
        try {
            Connection con = Connect.connect();
            
            System.out.println("=== USERS Table Structure ===");
            DatabaseMetaData metaData = con.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, "USERS", null);
            
            System.out.println("Column Name | Data Type | Nullable");
            System.out.println("------------|-----------|----------");
            
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                String nullable = columns.getString("IS_NULLABLE");
                System.out.println(columnName + " | " + dataType + " | " + nullable);
            }
            
            // Also check what users exist (without ROLE column)
            System.out.println("\n=== Current Users ===");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID, USERNAME, PASSWORD FROM USERS");
            
            System.out.println("ID | USERNAME | PASSWORD");
            System.out.println("---|----------|----------");
            
            while (rs.next()) {
                System.out.println(rs.getInt("ID") + " | " + rs.getString("USERNAME") + " | " + rs.getString("PASSWORD"));
            }
            
            con.close();
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
