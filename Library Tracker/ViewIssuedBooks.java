import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewIssuedBooks extends JFrame {
    private JTable issuedTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    
    public ViewIssuedBooks() {
        setTitle("View Issued Books - Library Management System");
        setSize(1000, 600);
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
                Color color1 = new Color(255, 228, 196);
                Color color2 = new Color(255, 140, 0);
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
        JLabel titleLabel = new JLabel("📋 Issued Books", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(139, 69, 19));
        titlePanel.add(titleLabel);
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setOpaque(false);
        filterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 140, 0), 2),
            "Search & Actions",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(139, 69, 19)
        ));
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 12));
        searchField = new JTextField(15);
        searchField.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JButton searchButton = createStyledButton("Search", new Color(255, 140, 0));
        JButton refreshButton = createStyledButton("Refresh", new Color(34, 139, 34));
        JButton returnButton = createStyledButton("Return Book", new Color(220, 20, 60));
        
        filterPanel.add(searchLabel);
        filterPanel.add(searchField);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(searchButton);
        filterPanel.add(refreshButton);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(returnButton);
        
        // Table setup
        String[] columnNames = {"Issue ID", "Book ID", "Book Name", "Author", "User ID", "Username", "Issue Date", "Due Date", "Days Overdue"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        issuedTable = new JTable(tableModel);
        issuedTable.setFont(new Font("Arial", Font.PLAIN, 11));
        issuedTable.setRowHeight(25);
        issuedTable.setSelectionBackground(new Color(255, 218, 185));
        issuedTable.setSelectionForeground(Color.BLACK);
        issuedTable.setGridColor(new Color(255, 140, 0));
        issuedTable.setShowGrid(true);
        
        // Table header styling
        JTableHeader header = issuedTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(new Color(255, 140, 0));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(issuedTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 140, 0), 2));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Action listeners
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchIssuedBooks();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadAllIssuedBooks();
            }
        });
        
        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnSelectedBook();
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
        loadAllIssuedBooks();
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(100, 30));
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
    
    private void loadAllIssuedBooks() {
        tableModel.setRowCount(0);
        try {
            Connection con = Connect.connect();
            String query = "SELECT i.ID as ISSUE_ID, i.BOOKID, b.NAME, b.AUTHOR, i.USERID, u.USERNAME, " +
                          "i.ISSUEDATE, i.RETURNDATE, " +
                          "DATEDIFF(CURDATE(), i.RETURNDATE) as OVERDUE " +
                          "FROM ISSUE i " +
                          "JOIN BOOKS b ON i.BOOKID = b.ID " +
                          "JOIN USERS u ON i.USERID = u.ID " +
                          "WHERE b.STATUS = 'issued' " +
                          "ORDER BY i.ISSUEDATE DESC";
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                int overdue = rs.getInt("OVERDUE");
                String overdueStr = overdue > 0 ? String.valueOf(overdue) : "0";
                
                Object[] row = {
                    rs.getInt("ISSUE_ID"),
                    rs.getInt("BOOKID"),
                    rs.getString("NAME"),
                    rs.getString("AUTHOR"),
                    rs.getInt("USERID"),
                    rs.getString("USERNAME"),
                    rs.getDate("ISSUEDATE"),
                    rs.getDate("RETURNDATE"),
                    overdueStr
                };
                tableModel.addRow(row);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading issued books: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void searchIssuedBooks() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadAllIssuedBooks();
            return;
        }
        
        tableModel.setRowCount(0);
        
        try {
            Connection con = Connect.connect();
            String query = "SELECT i.ID as ISSUE_ID, i.BOOKID, b.NAME, b.AUTHOR, i.USERID, u.USERNAME, " +
                          "i.ISSUEDATE, i.RETURNDATE, " +
                          "DATEDIFF(CURDATE(), i.RETURNDATE) as OVERDUE " +
                          "FROM ISSUE i " +
                          "JOIN BOOKS b ON i.BOOKID = b.ID " +
                          "JOIN USERS u ON i.USERID = u.ID " +
                          "WHERE b.STATUS = 'issued' AND " +
                          "(b.NAME LIKE ? OR b.AUTHOR LIKE ? OR u.USERNAME LIKE ?) " +
                          "ORDER BY i.ISSUEDATE DESC";
            
            PreparedStatement ps = con.prepareStatement(query);
            String searchPattern = "%" + searchText + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                int overdue = rs.getInt("OVERDUE");
                String overdueStr = overdue > 0 ? String.valueOf(overdue) : "0";
                
                Object[] row = {
                    rs.getInt("ISSUE_ID"),
                    rs.getInt("BOOKID"),
                    rs.getString("NAME"),
                    rs.getString("AUTHOR"),
                    rs.getInt("USERID"),
                    rs.getString("USERNAME"),
                    rs.getDate("ISSUEDATE"),
                    rs.getDate("RETURNDATE"),
                    overdueStr
                };
                tableModel.addRow(row);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error searching issued books: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void returnSelectedBook() {
        int selectedRow = issuedTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a book to return!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int bookId = (Integer) tableModel.getValueAt(selectedRow, 1);
        String bookName = (String) tableModel.getValueAt(selectedRow, 2);
        String username = (String) tableModel.getValueAt(selectedRow, 5);
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Return book '" + bookName + "' issued to '" + username + "'?",
            "Confirm Return",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection con = Connect.connect();
                
                // Update book status to available
                PreparedStatement updateBook = con.prepareStatement("UPDATE BOOKS SET STATUS='available' WHERE ID=?");
                updateBook.setInt(1, bookId);
                updateBook.executeUpdate();
                
                // Remove from issued books (or mark as returned)
                PreparedStatement deleteIssue = con.prepareStatement("DELETE FROM ISSUE WHERE BOOKID=? AND USERID=?");
                deleteIssue.setInt(1, bookId);
                deleteIssue.setInt(2, (Integer) tableModel.getValueAt(selectedRow, 4));
                deleteIssue.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "✅ Book returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAllIssuedBooks();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error returning book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void showViewIssuedBooksDialog() {
        SwingUtilities.invokeLater(() -> {
            new ViewIssuedBooks().setVisible(true);
        });
    }
}
