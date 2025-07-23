import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login {
    public static void login() {
        JFrame frame = new JFrame("Library Management System - Login");
        frame.setSize(500, 600);
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
                Color color1 = new Color(74, 144, 226);
                Color color2 = new Color(80, 170, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("📚 Library Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Welcome! Please login to continue", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitleLabel.setForeground(Color.WHITE);
        
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Login form panel
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel userLabel = new JLabel("👤 Username:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userLabel.setForeground(Color.WHITE);
        formPanel.add(userLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField userField = new JTextField(20);
        styleTextField(userField);
        formPanel.add(userField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        JLabel passLabel = new JLabel("🔒 Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passLabel.setForeground(Color.WHITE);
        formPanel.add(passLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPasswordField passField = new JPasswordField(20);
        styleTextField(passField);
        formPanel.add(passField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton loginBtn = createStyledButton("🚀 Login", new Color(76, 175, 80));
        JButton createDbBtn = createStyledButton("🔧 Setup Database", new Color(255, 152, 0));
        
        buttonPanel.add(loginBtn);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(createDbBtn);
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        
        JTextArea infoArea = new JTextArea(
            "ℹ️ Default Admin Credentials:\n" +
            "Username: admin\n" +
            "Password: admin\n\n" +
            "💡 First time setup? Click 'Setup Database' to initialize the system."
        );
        infoArea.setFont(new Font("Arial", Font.PLAIN, 12));
        infoArea.setForeground(Color.WHITE);
        infoArea.setOpaque(false);
        infoArea.setEditable(false);
        infoArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        infoPanel.add(infoArea);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(infoPanel, BorderLayout.CENTER);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        frame.add(mainPanel);
        frame.setVisible(true);

        // Action listeners
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText().trim();
                String password = String.valueOf(passField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter both username and password!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Connection con = Connect.connect();
                    if (con == null) {
                        JOptionPane.showMessageDialog(frame, "Database connection failed! Please setup database first.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    PreparedStatement stmt = con.prepareStatement("SELECT ID, USERNAME, PASSWORD FROM USERS WHERE USERNAME=? AND PASSWORD=?");
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        // Use simple username-based role logic (no ROLE column needed)
                        String userId = rs.getString("ID");
                        
                        if (username.equals("admin") || username.equals("Aditya") || username.equals("root")) {
                            // Admin/Librarian users get admin access
                            frame.dispose();
                            AdminMenu.admin_menu();
                        } else {
                            // Regular users get user access
                            frame.dispose();
                            UserMenu.user_menu(userId);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "❌ Invalid credentials! Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage() + "\nPlease setup database first.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        createDbBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(frame, 
                    "This will create/reset the database tables.\nAre you sure you want to continue?", 
                    "Confirm Database Setup", 
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        Create.create();
                        JOptionPane.showMessageDialog(frame, "✅ Database setup completed successfully!\nYou can now login with admin/admin", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Error setting up database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        // Enter key support
        ActionListener loginAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginBtn.doClick();
            }
        };
        
        userField.addActionListener(loginAction);
        passField.addActionListener(loginAction);
    }
    
    private static void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
    }
    
    private static JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(160, 40));
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
}
