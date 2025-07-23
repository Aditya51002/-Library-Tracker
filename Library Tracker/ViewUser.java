import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewUser extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> roleFilter;
    
    public ViewUser() {
        setTitle("View All Users - Library Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(255, 240, 245);
                Color color2 = new Color(219, 112, 147);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("👥 Users Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(139, 0, 139));
        titlePanel.add(titleLabel);
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setOpaque(false);
        filterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(199, 21, 133), 2),
            "Search & Filter",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(139, 0, 139)
        ));
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 12));
        searchField = new JTextField(15);
        searchField.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        String[] roles = {"All", "user", "librarian", "admin"};
        roleFilter = new JComboBox<>(roles);
        roleFilter.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JButton searchButton = createStyledButton("Search", new Color(255, 20, 147));
        JButton refreshButton = createStyledButton("Refresh", new Color(34, 139, 34));
        
        filterPanel.add(searchLabel);
        filterPanel.add(searchField);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(roleLabel);
        filterPanel.add(roleFilter);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(searchButton);
        filterPanel.add(refreshButton);
        
        // Table setup
        String[] columnNames = {"ID", "Username", "Email", "Phone", "Role"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        userTable = new JTable(tableModel);
        userTable.setFont(new Font("Arial", Font.PLAIN, 12));
        userTable.setRowHeight(25);
        userTable.setSelectionBackground(new Color(255, 182, 193));
        userTable.setSelectionForeground(Color.BLACK);
        userTable.setGridColor(new Color(199, 21, 133));
        userTable.setShowGrid(true);
        
        // Table header styling
        JTableHeader header = userTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(199, 21, 133));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(199, 21, 133), 2));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Action listeners
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchUsers();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadAllUsers();
            }
        });
        
        // Double click to view details
        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewUserDetails();
                }
            }
        });
        
        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Load initial data
        loadAllUsers();
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(80, 30));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void loadAllUsers() {
        tableModel.setRowCount(0);
        try {
            Connection con = Connect.connect();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM USERS ORDER BY ID");
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("ID"),
                    rs.getString("USERNAME"),
                    rs.getString("EMAIL") != null ? rs.getString("EMAIL") : "N/A",
                    rs.getString("PHONE") != null ? rs.getString("PHONE") : "N/A",
                    rs.getString("ROLE") != null ? rs.getString("ROLE") : "user"
                };
                tableModel.addRow(row);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void searchUsers() {
        String searchText = searchField.getText().trim();
        String selectedRole = (String) roleFilter.getSelectedItem();
        
        tableModel.setRowCount(0);
        
        try {
            Connection con = Connect.connect();
            StringBuilder query = new StringBuilder("SELECT * FROM USERS WHERE 1=1");
            
            if (!searchText.isEmpty()) {
                query.append(" AND (USERNAME LIKE ? OR EMAIL LIKE ? OR PHONE LIKE ?)");
            }
            if (!selectedRole.equals("All")) {
                query.append(" AND ROLE = ?");
            }
            query.append(" ORDER BY ID");
            
            PreparedStatement ps = con.prepareStatement(query.toString());
            int paramIndex = 1;
            
            if (!searchText.isEmpty()) {
                String searchPattern = "%" + searchText + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
            }
            if (!selectedRole.equals("All")) {
                ps.setString(paramIndex++, selectedRole);
            }
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("ID"),
                    rs.getString("USERNAME"),
                    rs.getString("EMAIL") != null ? rs.getString("EMAIL") : "N/A",
                    rs.getString("PHONE") != null ? rs.getString("PHONE") : "N/A",
                    rs.getString("ROLE") != null ? rs.getString("ROLE") : "user"
                };
                tableModel.addRow(row);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error searching users: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void viewUserDetails() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (Integer) tableModel.getValueAt(selectedRow, 0);
            showUserDetailsDialog(userId);
        }
    }
    
    private void showUserDetailsDialog(int userId) {
        try {
            Connection con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERS WHERE ID = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String details = String.format(
                    "👤 User Details\n\n" +
                    "ID: %d\n" +
                    "Username: %s\n" +
                    "Email: %s\n" +
                    "Phone: %s\n" +
                    "Role: %s\n" +
                    "Address: %s",
                    rs.getInt("ID"),
                    rs.getString("USERNAME"),
                    rs.getString("EMAIL") != null ? rs.getString("EMAIL") : "N/A",
                    rs.getString("PHONE") != null ? rs.getString("PHONE") : "N/A",
                    rs.getString("ROLE") != null ? rs.getString("ROLE") : "user",
                    rs.getString("ADDRESS") != null ? rs.getString("ADDRESS") : "No address provided"
                );
                
                JTextArea textArea = new JTextArea(details);
                textArea.setFont(new Font("Arial", Font.PLAIN, 12));
                textArea.setEditable(false);
                textArea.setBackground(new Color(255, 248, 220));
                
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(350, 250));
                
                JOptionPane.showMessageDialog(this, scrollPane, "User Details", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading user details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void showViewUsersDialog() {
        SwingUtilities.invokeLater(() -> {
            new ViewUser().setVisible(true);
        });
    }
}
