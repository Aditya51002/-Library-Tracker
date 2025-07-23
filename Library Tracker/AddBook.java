import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddBook extends JFrame {
    private JTextField nameField, authorField, publisherField, isbnField;
    private JTextArea descriptionArea;
    private JComboBox<String> categoryCombo;
    
    public AddBook() {
        setTitle("Add New Book - Library Management System");
        setSize(500, 600);
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
                Color color1 = new Color(135, 206, 250);
                Color color2 = new Color(70, 130, 180);
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
        JLabel titleLabel = new JLabel("📚 Add New Book", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Book Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel nameLabel = new JLabel("Book Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = new JTextField(20);
        styleTextField(nameField);
        formPanel.add(nameField, gbc);
        
        // Author
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setForeground(Color.WHITE);
        authorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(authorLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        authorField = new JTextField(20);
        styleTextField(authorField);
        formPanel.add(authorField, gbc);
        
        // Publisher
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        JLabel publisherLabel = new JLabel("Publisher:");
        publisherLabel.setForeground(Color.WHITE);
        publisherLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(publisherLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        publisherField = new JTextField(20);
        styleTextField(publisherField);
        formPanel.add(publisherField, gbc);
        
        // ISBN
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        JLabel isbnLabel = new JLabel("ISBN:");
        isbnLabel.setForeground(Color.WHITE);
        isbnLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(isbnLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        isbnField = new JTextField(20);
        styleTextField(isbnField);
        formPanel.add(isbnField, gbc);
        
        // Category
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setForeground(Color.WHITE);
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(categoryLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        String[] categories = {"Fiction", "Non-Fiction", "Science", "Technology", "History", "Biography", "Education", "Other"};
        categoryCombo = new JComboBox<>(categories);
        styleComboBox(categoryCombo);
        formPanel.add(categoryCombo, gbc);
        
        // Description
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setForeground(Color.WHITE);
        descLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(descLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(4, 20);
        styleTextArea(descriptionArea);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        formPanel.add(scrollPane, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        
        JButton addButton = createStyledButton("Add Book", new Color(34, 139, 34));
        JButton clearButton = createStyledButton("Clear", new Color(255, 69, 0));
        JButton cancelButton = createStyledButton("Cancel", new Color(220, 20, 60));
        
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(cancelButton);
        
        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBook();
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
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    private void styleTextArea(JTextArea area) {
        area.setFont(new Font("Arial", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }
    
    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Arial", Font.PLAIN, 14));
        combo.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(100, 35));
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
    
    private void addBook() {
        String name = nameField.getText().trim();
        String author = authorField.getText().trim();
        String publisher = publisherField.getText().trim();
        String isbn = isbnField.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();
        String description = descriptionArea.getText().trim();
        
        if (name.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Book name and author are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Connection con = Connect.connect();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO BOOKS(NAME, AUTHOR, PUBLISHER, ISBN, CATEGORY, DESCRIPTION, STATUS) VALUES (?, ?, ?, ?, ?, ?, 'available')"
            );
            ps.setString(1, name);
            ps.setString(2, author);
            ps.setString(3, publisher);
            ps.setString(4, isbn);
            ps.setString(5, category);
            ps.setString(6, description);
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "✅ Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearFields() {
        nameField.setText("");
        authorField.setText("");
        publisherField.setText("");
        isbnField.setText("");
        categoryCombo.setSelectedIndex(0);
        descriptionArea.setText("");
    }
    
    public static void showAddBookDialog() {
        SwingUtilities.invokeLater(() -> {
            new AddBook().setVisible(true);
        });
    }
}
