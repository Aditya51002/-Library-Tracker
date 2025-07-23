import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UserMenu {
    public static void user_menu(String id) {
        JFrame frame = new JFrame("User Dashboard - Library Management System");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(0, 150, 136);
                Color color2 = new Color(0, 188, 212);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("📚 User Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Welcome panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setOpaque(false);
        JLabel welcomeLabel = new JLabel("Welcome, User #" + id + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        welcomeLabel.setForeground(Color.WHITE);
        welcomePanel.add(welcomeLabel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        
        JButton viewBooksBtn = createStyledButton("📖 View All Books", new Color(33, 150, 243));
        JButton issueBooksBtn = createStyledButton("📤 Issue Book", new Color(76, 175, 80));
        JButton myBooksBtn = createStyledButton("📋 My Issued Books", new Color(255, 152, 0));
        JButton logoutBtn = createStyledButton("🚪 Logout", new Color(244, 67, 54));
        
        buttonPanel.add(viewBooksBtn);
        buttonPanel.add(issueBooksBtn);
        buttonPanel.add(myBooksBtn);
        buttonPanel.add(logoutBtn);
        
        // Footer panel
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        JLabel footerLabel = new JLabel("Library Management System v2.0 - User Panel", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        
        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(welcomePanel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        frame.add(mainPanel);
        frame.setVisible(true);

        // Action listeners
        viewBooksBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewBook.showViewBooksDialog();
            }
        });

        issueBooksBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showUserIssueDialog(id);
            }
        });

        myBooksBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMyIssuedBooks(id);
            }
        });

        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Login.login();
            }
        });
    }
    
    private static JButton createStyledButton(String text, Color color) {
        JButton button = new JButton("<html><center>" + text + "</center></html>");
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(200, 100));
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
    
    private static void showUserIssueDialog(String userId) {
        String bookId = JOptionPane.showInputDialog(null, "Enter Book ID to issue:");
        if (bookId != null && !bookId.trim().isEmpty()) {
            try {
                Connection con = Connect.connect();
                
                // Check if book exists and is available
                PreparedStatement checkBook = con.prepareStatement("SELECT * FROM BOOKS WHERE ID=? AND STATUS='available'");
                checkBook.setString(1, bookId.trim());
                ResultSet rs = checkBook.executeQuery();
                
                if (rs.next()) {
                    // Issue the book
                    PreparedStatement issueStmt = con.prepareStatement(
                        "INSERT INTO ISSUE(BOOKID, USERID, ISSUEDATE, RETURNDATE) VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY))"
                    );
                    issueStmt.setString(1, bookId.trim());
                    issueStmt.setString(2, userId);
                    issueStmt.executeUpdate();
                    
                    // Update book status
                    PreparedStatement updateBook = con.prepareStatement("UPDATE BOOKS SET STATUS='issued' WHERE ID=?");
                    updateBook.setString(1, bookId.trim());
                    updateBook.executeUpdate();
                    
                    JOptionPane.showMessageDialog(null, 
                        "✅ Book '" + rs.getString("NAME") + "' issued successfully!\nDue date: 14 days from today", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "❌ Book not found or not available!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error issuing book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private static void showMyIssuedBooks(String userId) {
        try {
            Connection con = Connect.connect();
            String query = "SELECT b.NAME, b.AUTHOR, i.ISSUEDATE, i.RETURNDATE, " +
                          "DATEDIFF(CURDATE(), i.RETURNDATE) as OVERDUE " +
                          "FROM ISSUE i " +
                          "JOIN BOOKS b ON i.BOOKID = b.ID " +
                          "WHERE i.USERID = ? AND b.STATUS = 'issued'";
            
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            
            StringBuilder message = new StringBuilder("📚 YOUR ISSUED BOOKS\n");
            message.append("═══════════════════════════════════════\n\n");
            
            boolean hasBooks = false;
            while (rs.next()) {
                hasBooks = true;
                int overdue = rs.getInt("OVERDUE");
                String overdueInfo = overdue > 0 ? " (⚠️ " + overdue + " days overdue!)" : "";
                
                message.append("📖 ").append(rs.getString("NAME")).append("\n");
                message.append("✍️ Author: ").append(rs.getString("AUTHOR")).append("\n");
                message.append("📅 Issued: ").append(rs.getDate("ISSUEDATE")).append("\n");
                message.append("📅 Due: ").append(rs.getDate("RETURNDATE")).append(overdueInfo).append("\n");
                message.append("─────────────────────────────────────\n");
            }
            
            if (!hasBooks) {
                message.append("No books currently issued.");
            }
            
            JTextArea textArea = new JTextArea(message.toString());
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setEditable(false);
            textArea.setBackground(new Color(240, 248, 255));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            
            JOptionPane.showMessageDialog(null, scrollPane, "My Issued Books", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error loading your books: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
