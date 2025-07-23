import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReturnBook extends JFrame {
    private JTextField bookIdField;
    private JTextArea bookDetailsArea;
    
    public ReturnBook() {
        setTitle("Return Book - Library Management System");
        setSize(500, 400);
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
                Color color1 = new Color(255, 160, 122);
                Color color2 = new Color(220, 20, 60);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("📤 Return Book", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        
        // Book ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel bookIdLabel = new JLabel("Book ID:");
        bookIdLabel.setForeground(Color.WHITE);
        bookIdLabel.setFont(new Font("Arial", Font.BOLD, 16));
        formPanel.add(bookIdLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        bookIdField = new JTextField(15);
        styleTextField(bookIdField);
        formPanel.add(bookIdField, gbc);
        
        gbc.gridx = 2;
        JButton searchBtn = createStyledButton("Search", new Color(30, 144, 255));
        formPanel.add(searchBtn, gbc);
        
        // Book Details
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        JLabel detailsLabel = new JLabel("Book Details:");
        detailsLabel.setForeground(Color.WHITE);
        detailsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        formPanel.add(detailsLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        bookDetailsArea = new JTextArea(8, 25);
        styleTextArea(bookDetailsArea);
        bookDetailsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bookDetailsArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 20, 60), 2));
        formPanel.add(scrollPane, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        
        JButton returnButton = createStyledButton("Return Book", new Color(34, 139, 34));
        JButton clearButton = createStyledButton("Clear", new Color(255, 69, 0));
        JButton cancelButton = createStyledButton("Cancel", new Color(220, 20, 60));
        
        buttonPanel.add(returnButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(cancelButton);
        
        // Add action listeners
        searchBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchBook();
            }
        });
        
        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnBook();
            }
        });
        
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 20, 60), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
    }
    
    private void styleTextArea(JTextArea area) {
        area.setFont(new Font("Arial", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        area.setBackground(new Color(255, 255, 255));
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(120, 35));
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
    
    private void searchBook() {
        String bookId = bookIdField.getText().trim();
        if (bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a book ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Connection con = Connect.connect();
            
            // Get book and issue details
            String query = "SELECT b.*, i.USERID, u.USERNAME, i.ISSUEDATE, i.RETURNDATE, " +
                          "DATEDIFF(CURDATE(), i.RETURNDATE) as OVERDUE " +
                          "FROM BOOKS b " +
                          "LEFT JOIN ISSUE i ON b.ID = i.BOOKID " +
                          "LEFT JOIN USERS u ON i.USERID = u.ID " +
                          "WHERE b.ID = ? AND b.STATUS = 'issued'";
            
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, bookId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int overdue = rs.getInt("OVERDUE");
                String overdueInfo = overdue > 0 ? "\n⚠️ OVERDUE BY " + overdue + " DAYS!" : "\n✅ Not overdue";
                
                String details = String.format(
                    "📖 BOOK INFORMATION\n" +
                    "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                    "📚 Name: %s\n" +
                    "✍️ Author: %s\n" +
                    "🏢 Publisher: %s\n" +
                    "📂 Category: %s\n" +
                    "📊 Status: %s\n\n" +
                    "👤 ISSUED TO\n" +
                    "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                    "🆔 User ID: %s\n" +
                    "👤 Username: %s\n" +
                    "📅 Issue Date: %s\n" +
                    "📅 Due Date: %s%s",
                    rs.getString("NAME"),
                    rs.getString("AUTHOR"),
                    rs.getString("PUBLISHER") != null ? rs.getString("PUBLISHER") : "N/A",
                    rs.getString("CATEGORY") != null ? rs.getString("CATEGORY") : "N/A",
                    rs.getString("STATUS"),
                    rs.getInt("USERID"),
                    rs.getString("USERNAME"),
                    rs.getDate("ISSUEDATE"),
                    rs.getDate("RETURNDATE"),
                    overdueInfo
                );
                
                bookDetailsArea.setText(details);
                if (overdue > 0) {
                    bookDetailsArea.setBackground(new Color(255, 230, 230));
                } else {
                    bookDetailsArea.setBackground(new Color(230, 255, 230));
                }
                
            } else {
                bookDetailsArea.setText("❌ Book not found or not currently issued!");
                bookDetailsArea.setBackground(new Color(255, 200, 200));
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error searching book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void returnBook() {
        String bookId = bookIdField.getText().trim();
        
        if (bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a book ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (bookDetailsArea.getText().contains("❌")) {
            JOptionPane.showMessageDialog(this, "Please search for a valid issued book first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to return this book?",
            "Confirm Return",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection con = Connect.connect();
                
                // Update book status to available
                PreparedStatement updateBook = con.prepareStatement("UPDATE BOOKS SET STATUS='available' WHERE ID=?");
                updateBook.setString(1, bookId);
                updateBook.executeUpdate();
                
                // Remove from issue table
                PreparedStatement deleteIssue = con.prepareStatement("DELETE FROM ISSUE WHERE BOOKID=?");
                deleteIssue.setString(1, bookId);
                deleteIssue.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "✅ Book returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error returning book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearFields() {
        bookIdField.setText("");
        bookDetailsArea.setText("");
        bookDetailsArea.setBackground(Color.WHITE);
    }
    
    public static void showReturnBookDialog() {
        SwingUtilities.invokeLater(() -> {
            new ReturnBook().setVisible(true);
        });
    }
}
