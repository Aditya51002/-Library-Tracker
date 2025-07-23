import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class IssueBook extends JFrame {
    private JTextField bookIdField, userIdField;
    private JComboBox<String> dueDaysCombo;
    private JTextArea bookDetailsArea, userDetailsArea;
    private JLabel dueDateLabel;
    
    public IssueBook() {
        setTitle("Issue Book - Library Management System");
        setSize(700, 600);
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
                Color color1 = new Color(144, 238, 144);
                Color color2 = new Color(34, 139, 34);
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
        JLabel titleLabel = new JLabel("📖 Issue Book", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Book ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel bookIdLabel = new JLabel("Book ID:");
        bookIdLabel.setForeground(Color.WHITE);
        bookIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(bookIdLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        bookIdField = new JTextField(15);
        styleTextField(bookIdField);
        formPanel.add(bookIdField, gbc);
        
        gbc.gridx = 2;
        JButton searchBookBtn = createStyledButton("Search Book", new Color(30, 144, 255));
        formPanel.add(searchBookBtn, gbc);
        
        // Book Details
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        JLabel bookDetailsLabel = new JLabel("Book Details:");
        bookDetailsLabel.setForeground(Color.WHITE);
        bookDetailsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(bookDetailsLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        bookDetailsArea = new JTextArea(3, 20);
        styleTextArea(bookDetailsArea);
        bookDetailsArea.setEditable(false);
        JScrollPane bookScrollPane = new JScrollPane(bookDetailsArea);
        bookScrollPane.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
        formPanel.add(bookScrollPane, gbc);
        
        // User ID
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setForeground(Color.WHITE);
        userIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(userIdLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        userIdField = new JTextField(15);
        styleTextField(userIdField);
        formPanel.add(userIdField, gbc);
        
        gbc.gridx = 2;
        JButton searchUserBtn = createStyledButton("Search User", new Color(255, 140, 0));
        formPanel.add(searchUserBtn, gbc);
        
        // User Details
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        JLabel userDetailsLabel = new JLabel("User Details:");
        userDetailsLabel.setForeground(Color.WHITE);
        userDetailsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(userDetailsLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        userDetailsArea = new JTextArea(3, 20);
        styleTextArea(userDetailsArea);
        userDetailsArea.setEditable(false);
        JScrollPane userScrollPane = new JScrollPane(userDetailsArea);
        userScrollPane.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
        formPanel.add(userScrollPane, gbc);
        
        // Due Days
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        JLabel dueDaysLabel = new JLabel("Due Days:");
        dueDaysLabel.setForeground(Color.WHITE);
        dueDaysLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(dueDaysLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        String[] days = {"7", "14", "21", "30"};
        dueDaysCombo = new JComboBox<>(days);
        styleComboBox(dueDaysCombo);
        formPanel.add(dueDaysCombo, gbc);
        
        // Due Date
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        JLabel dueDateTitleLabel = new JLabel("Due Date:");
        dueDateTitleLabel.setForeground(Color.WHITE);
        dueDateTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(dueDateTitleLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        dueDateLabel = new JLabel("");
        dueDateLabel.setForeground(Color.WHITE);
        dueDateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dueDateLabel.setOpaque(true);
        dueDateLabel.setBackground(new Color(0, 0, 0, 50));
        dueDateLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        formPanel.add(dueDateLabel, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        
        JButton issueButton = createStyledButton("Issue Book", new Color(34, 139, 34));
        JButton clearButton = createStyledButton("Clear", new Color(255, 69, 0));
        JButton cancelButton = createStyledButton("Cancel", new Color(220, 20, 60));
        
        buttonPanel.add(issueButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(cancelButton);
        
        // Add action listeners
        searchBookBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchBook();
            }
        });
        
        searchUserBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchUser();
            }
        });
        
        dueDaysCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateDueDate();
            }
        });
        
        issueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                issueBook();
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
        
        // Initialize due date
        updateDueDate();
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    private void styleTextArea(JTextArea area) {
        area.setFont(new Font("Arial", Font.PLAIN, 12));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        area.setBackground(new Color(255, 255, 255));
    }
    
    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Arial", Font.PLAIN, 14));
        combo.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(120, 30));
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
            PreparedStatement ps = con.prepareStatement("SELECT * FROM BOOKS WHERE ID = ?");
            ps.setString(1, bookId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String status = rs.getString("STATUS");
                if ("issued".equals(status)) {
                    bookDetailsArea.setText("❌ Book is already issued!");
                    bookDetailsArea.setBackground(new Color(255, 200, 200));
                } else {
                    String details = String.format(
                        "✅ Book Found!\n" +
                        "Name: %s\n" +
                        "Author: %s\n" +
                        "Publisher: %s\n" +
                        "Category: %s\n" +
                        "Status: %s",
                        rs.getString("NAME"),
                        rs.getString("AUTHOR"),
                        rs.getString("PUBLISHER") != null ? rs.getString("PUBLISHER") : "N/A",
                        rs.getString("CATEGORY") != null ? rs.getString("CATEGORY") : "N/A",
                        status
                    );
                    bookDetailsArea.setText(details);
                    bookDetailsArea.setBackground(new Color(200, 255, 200));
                }
            } else {
                bookDetailsArea.setText("❌ Book not found!");
                bookDetailsArea.setBackground(new Color(255, 200, 200));
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error searching book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void searchUser() {
        String userId = userIdField.getText().trim();
        if (userId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a user ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Connection con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERS WHERE ID = ?");
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String details = String.format(
                    "✅ User Found!\n" +
                    "Username: %s\n" +
                    "Email: %s\n" +
                    "Phone: %s\n" +
                    "Role: %s",
                    rs.getString("USERNAME"),
                    rs.getString("EMAIL") != null ? rs.getString("EMAIL") : "N/A",
                    rs.getString("PHONE") != null ? rs.getString("PHONE") : "N/A",
                    rs.getString("ROLE") != null ? rs.getString("ROLE") : "user"
                );
                userDetailsArea.setText(details);
                userDetailsArea.setBackground(new Color(200, 255, 200));
            } else {
                userDetailsArea.setText("❌ User not found!");
                userDetailsArea.setBackground(new Color(255, 200, 200));
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error searching user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateDueDate() {
        int days = Integer.parseInt((String) dueDaysCombo.getSelectedItem());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dueDateLabel.setText(sdf.format(cal.getTime()));
    }
    
    private void issueBook() {
        String bookId = bookIdField.getText().trim();
        String userId = userIdField.getText().trim();
        
        if (bookId.isEmpty() || userId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Book ID and User ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if book details are loaded and book is available
        if (bookDetailsArea.getText().contains("❌")) {
            JOptionPane.showMessageDialog(this, "Please select a valid and available book!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if user details are loaded
        if (userDetailsArea.getText().contains("❌")) {
            JOptionPane.showMessageDialog(this, "Please select a valid user!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Connection con = Connect.connect();
            
            // Insert into ISSUE table
            PreparedStatement issueStmt = con.prepareStatement(
                "INSERT INTO ISSUE(BOOKID, USERID, ISSUEDATE, RETURNDATE) VALUES (?, ?, CURDATE(), ?)"
            );
            issueStmt.setString(1, bookId);
            issueStmt.setString(2, userId);
            issueStmt.setString(3, dueDateLabel.getText());
            issueStmt.executeUpdate();
            
            // Update book status
            PreparedStatement updateStmt = con.prepareStatement("UPDATE BOOKS SET STATUS='issued' WHERE ID=?");
            updateStmt.setString(1, bookId);
            updateStmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "✅ Book issued successfully!\nDue Date: " + dueDateLabel.getText(), "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error issuing book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearFields() {
        bookIdField.setText("");
        userIdField.setText("");
        bookDetailsArea.setText("");
        bookDetailsArea.setBackground(Color.WHITE);
        userDetailsArea.setText("");
        userDetailsArea.setBackground(Color.WHITE);
        dueDaysCombo.setSelectedIndex(0);
        updateDueDate();
    }
    
    public static void showIssueBookDialog() {
        SwingUtilities.invokeLater(() -> {
            new IssueBook().setVisible(true);
        });
    }
}
