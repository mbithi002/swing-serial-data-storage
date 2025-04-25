import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Record implements Serializable {
    String name;
    int age;

    public Record(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return name + ", " + age;
    }
}

public class FileCRUD extends JFrame {
    private JTextField nameField, ageField, indexField;
    private JTextArea displayArea;
    private List<Record> records = new ArrayList<>();
    private final String TEXT_FILE = "records.txt";
    private final String BINARY_FILE = "records.dat";

    public FileCRUD() {
        setTitle("Record Manager");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));
        add(mainPanel);

        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(245, 245, 245));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Record Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(nameField, gbc);

        // Age field
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        ageField = new JTextField(5);
        ageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(ageField, gbc);

        // Index field
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Index:"), gbc);
        gbc.gridx = 1;
        indexField = new JTextField(5);
        indexField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(indexField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));

        // Create styled buttons
        JButton addButton = createStyledButton("Add", new Color(76, 175, 80));
        JButton updateButton = createStyledButton("Update", new Color(33, 150, 243));
        JButton deleteButton = createStyledButton("Delete", new Color(244, 67, 54));
        JButton loadButton = createStyledButton("Load Text", new Color(255, 152, 0));
        JButton saveBinaryButton = createStyledButton("Save Binary", new Color(156, 39, 176));
        JButton loadBinaryButton = createStyledButton("Load Binary", new Color(0, 150, 136));
        JButton clearRecordsButton = createStyledButton("Clear", new Color(158, 158, 158));
        JButton deleteRecordsButton = createStyledButton("Delete All", new Color(244, 67, 54));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(saveBinaryButton);
        buttonPanel.add(loadBinaryButton);
        buttonPanel.add(clearRecordsButton);
        buttonPanel.add(deleteRecordsButton);

        // Display area
        displayArea = new JTextArea();
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Records List"));

        // Add components to main panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        topPanel.setBackground(new Color(245, 245, 245));

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        addButton.addActionListener(e -> addRecord());
        updateButton.addActionListener(e -> updateRecord());
        deleteButton.addActionListener(e -> deleteRecord());
        loadButton.addActionListener(e -> loadRecords());
        saveBinaryButton.addActionListener(e -> saveBinary());
        loadBinaryButton.addActionListener(e -> loadBinary());
        clearRecordsButton.addActionListener(e -> clearRecords());
        deleteRecordsButton.addActionListener(e -> deleteAllRecords());
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void addRecord() {
        try {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            records.add(new Record(name, age));
            saveRecords();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRecord() {
        try {
            int index = Integer.parseInt(indexField.getText());
            if (index >= 0 && index < records.size()) {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                records.set(index, new Record(name, age));
                saveRecords();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid index!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid index number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecord() {
        try {
            int index = Integer.parseInt(indexField.getText());
            if (index >= 0 && index < records.size()) {
                records.remove(index);
                saveRecords();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid index!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid index number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveRecords() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEXT_FILE))) {
            for (Record r : records) {
                writer.println(r.name + "," + r.age);
            }
            clearFields();
            displayRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRecords() {
        records.clear();
        try (Scanner scanner = new Scanner(new File(TEXT_FILE))) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                if (data.length >= 2) {
                    records.add(new Record(data[0], Integer.parseInt(data[1])));
                }
            }
            displayRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayRecords() {
        displayArea.setText("");
        for (int i = 0; i < records.size(); i++) {
            displayArea.append(i + ": " + records.get(i).toString() + "\n");
        }
    }

    private void saveBinary() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BINARY_FILE))) {
            oos.writeObject(records);
            clearFields();
            displayRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        indexField.setText("");
    }

    public void clearRecords() {
        records.clear();
        displayRecords();
    }

    public void deleteAllRecords() {
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete ALL records?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            records.clear();
            displayRecords();

            try(PrintWriter writer = new PrintWriter(new FileWriter(TEXT_FILE))) {
                writer.println("");
            } catch (IOException e) {
                e.printStackTrace();
            }

            try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BINARY_FILE))) {
                oos.writeObject(new ArrayList<>());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadBinary() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BINARY_FILE))) {
            records = (List<Record>) ois.readObject();
            displayRecords();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileCRUD app = new FileCRUD();
            app.setVisible(true);
        });
    }
}