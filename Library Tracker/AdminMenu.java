import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminMenu {
    public static void admin_menu() {
        JFrame frame = new JFrame("Admin Dashboard - Library Management System");
        frame.setSize(800, 600);
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
                Color color1 = new Color(72, 61, 139);
                Color color2 = new Color(123, 104, 238);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("🛡️ Admin Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Button panel with grid layout
        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 20, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Create styled buttons
        JButton addUserBtn = createStyledButton("👤 Add User", new Color(255, 20, 147));
        JButton viewUsersBtn = createStyledButton("👥 View Users", new Color(255, 20, 147));
        JButton addBookBtn = createStyledButton("📚 Add Book", new Color(30, 144, 255));
        JButton viewBooksBtn = createStyledButton("📖 View Books", new Color(30, 144, 255));
        JButton issueBooksBtn = createStyledButton("📤 Issue Book", new Color(34, 139, 34));
        JButton returnBooksBtn = createStyledButton("📥 Return Book", new Color(220, 20, 60));
        JButton viewIssuedBtn = createStyledButton("📋 Issued Books", new Color(255, 140, 0));
        JButton reportsBtn = createStyledButton("📊 Reports", new Color(138, 43, 226));
        JButton logoutBtn = createStyledButton("🚪 Logout", new Color(105, 105, 105));
        
        buttonPanel.add(addUserBtn);
        buttonPanel.add(viewUsersBtn);
        buttonPanel.add(addBookBtn);
        buttonPanel.add(viewBooksBtn);
        buttonPanel.add(issueBooksBtn);
        buttonPanel.add(returnBooksBtn);
        buttonPanel.add(viewIssuedBtn);
        buttonPanel.add(reportsBtn);
        buttonPanel.add(logoutBtn);
        
        // Footer panel
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        JLabel footerLabel = new JLabel("Library Management System v2.0 - Admin Panel", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        frame.add(mainPanel);
        frame.setVisible(true);

        // Action listeners
        addUserBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddUser.showAddUserDialog();
            }
        });

        viewUsersBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewUser.showViewUsersDialog();
            }
        });

        addBookBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddBook.showAddBookDialog();
            }
        });

        viewBooksBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewBook.showViewBooksDialog();
            }
        });

        issueBooksBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                IssueBook.showIssueBookDialog();
            }
        });

        returnBooksBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ReturnBook.showReturnBookDialog();
            }
        });

        viewIssuedBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewIssuedBooks.showViewIssuedBooksDialog();
            }
        });

        reportsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Reports feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
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
        button.setPreferredSize(new Dimension(180, 80));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
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
}
