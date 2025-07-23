import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewBook extends JFrame {
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> categoryFilter;
    private JComboBox<String> statusFilter;
    
    public ViewBook() {
        setTitle("View All Books - Library Management System");
        setSize(1000, 700);
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
                Color color1 = new Color(240, 248, 255);
                Color color2 = new Color(176, 196, 222);
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
        JLabel titleLabel = new JLabel("📚 Books Collection", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 25, 112));
        titlePanel.add(titleLabel);
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setOpaque(false);
        filterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Filters & Search",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(25, 25, 112)
        ));
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 12));
        searchField = new JTextField(15);
        searchField.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 12));
        String[] categories = {"All", "Fiction", "Non-Fiction", "Science", "Technology", "History", "Biography", "Education", "Other"};
        categoryFilter = new JComboBox<>(categories);
        categoryFilter.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        String[] statuses = {"All", "available", "issued"};
        statusFilter = new JComboBox<>(statuses);
        statusFilter.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JButton searchButton = createStyledButton("Search", new Color(30, 144, 255));
        JButton refreshButton = createStyledButton("Refresh", new Color(34, 139, 34));
        
        filterPanel.add(searchLabel);
        filterPanel.add(searchField);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(categoryLabel);
        filterPanel.add(categoryFilter);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(statusLabel);
        filterPanel.add(statusFilter);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(searchButton);
        filterPanel.add(refreshButton);
        
        // Table setup
        String[] columnNames = {"ID", "Book Name", "Author", "Publisher", "ISBN", "Category", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        bookTable = new JTable(tableModel);
        bookTable.setFont(new Font("Arial", Font.PLAIN, 12));
        bookTable.setRowHeight(25);
        bookTable.setSelectionBackground(new Color(173, 216, 230));
        bookTable.setSelectionForeground(Color.BLACK);
        bookTable.setGridColor(new Color(70, 130, 180));
        bookTable.setShowGrid(true);
        
        // Table header styling
        JTableHeader header = bookTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Action listeners
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchBooks();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadAllBooks();
            }
        });
        
        // Double click to view details
        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewBookDetails();
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
        loadAllBooks();
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
    
    private void loadAllBooks() {
        tableModel.setRowCount(0);
        try {
            Connection con = Connect.connect();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM BOOKS ORDER BY ID");
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("ID"),
                    rs.getString("NAME"),
                    rs.getString("AUTHOR"),
                    rs.getString("PUBLISHER") != null ? rs.getString("PUBLISHER") : "N/A",
                    rs.getString("ISBN") != null ? rs.getString("ISBN") : "N/A",
                    rs.getString("CATEGORY") != null ? rs.getString("CATEGORY") : "N/A",
                    rs.getString("STATUS")
                };
                tableModel.addRow(row);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void searchBooks() {
        String searchText = searchField.getText().trim();
        String selectedCategory = (String) categoryFilter.getSelectedItem();
        String selectedStatus = (String) statusFilter.getSelectedItem();
        
        tableModel.setRowCount(0);
        
        try {
            Connection con = Connect.connect();
            StringBuilder query = new StringBuilder("SELECT * FROM BOOKS WHERE 1=1");
            
            if (!searchText.isEmpty()) {
                query.append(" AND (NAME LIKE ? OR AUTHOR LIKE ? OR PUBLISHER LIKE ?)");
            }
            if (!selectedCategory.equals("All")) {
                query.append(" AND CATEGORY = ?");
            }
            if (!selectedStatus.equals("All")) {
                query.append(" AND STATUS = ?");
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
            if (!selectedCategory.equals("All")) {
                ps.setString(paramIndex++, selectedCategory);
            }
            if (!selectedStatus.equals("All")) {
                ps.setString(paramIndex++, selectedStatus);
            }
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("ID"),
                    rs.getString("NAME"),
                    rs.getString("AUTHOR"),
                    rs.getString("PUBLISHER") != null ? rs.getString("PUBLISHER") : "N/A",
                    rs.getString("ISBN") != null ? rs.getString("ISBN") : "N/A",
                    rs.getString("CATEGORY") != null ? rs.getString("CATEGORY") : "N/A",
                    rs.getString("STATUS")
                };
                tableModel.addRow(row);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error searching books: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void viewBookDetails() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
            int bookId = (Integer) tableModel.getValueAt(selectedRow, 0);
            showBookDetailsDialog(bookId);
        }
    }
    
    private void showBookDetailsDialog(int bookId) {
        try {
            Connection con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM BOOKS WHERE ID = ?");
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String details = String.format(
                    "📖 Book Details\n\n" +
                    "ID: %d\n" +
                    "Name: %s\n" +
                    "Author: %s\n" +
                    "Publisher: %s\n" +
                    "ISBN: %s\n" +
                    "Category: %s\n" +
                    "Status: %s\n" +
                    "Description: %s",
                    rs.getInt("ID"),
                    rs.getString("NAME"),
                    rs.getString("AUTHOR"),
                    rs.getString("PUBLISHER") != null ? rs.getString("PUBLISHER") : "N/A",
                    rs.getString("ISBN") != null ? rs.getString("ISBN") : "N/A",
                    rs.getString("CATEGORY") != null ? rs.getString("CATEGORY") : "N/A",
                    rs.getString("STATUS"),
                    rs.getString("DESCRIPTION") != null ? rs.getString("DESCRIPTION") : "No description available"
                );
                
                JTextArea textArea = new JTextArea(details);
                textArea.setFont(new Font("Arial", Font.PLAIN, 12));
                textArea.setEditable(false);
                textArea.setBackground(new Color(248, 248, 255));
                
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));
                
                JOptionPane.showMessageDialog(this, scrollPane, "Book Details", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading book details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void showViewBooksDialog() {
        SwingUtilities.invokeLater(() -> {
            new ViewBook().setVisible(true);
        });
    }
}
