import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class RandomAccessSwingApp extends JFrame {
    private static final String FILE_NAME = "random_access_data.txt";
    private static final int RECORD_SIZE = 50;
    
    private JTextField positionField, nameField, ageField;
    private JTextArea displayArea;
    
    public RandomAccessSwingApp() {
        setTitle("Random Access File Operations");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        positionField = new JTextField(5);
        nameField = new JTextField(20);
        ageField = new JTextField(5);
        
        inputPanel.add(new JLabel("Record Position (0-9):"));
        inputPanel.add(positionField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Age:"));
        inputPanel.add(ageField);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton writeButton = new JButton("Write");
        JButton readButton = new JButton("Read");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton displayAllButton = new JButton("Display All");
        
        buttonPanel.add(writeButton);
        buttonPanel.add(readButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(displayAllButton);
        
        // Display area
        displayArea = new JTextArea(10, 40);
        displayArea.setEditable(false);
        
        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(displayArea), BorderLayout.SOUTH);
        
        // Button actions
        writeButton.addActionListener(e -> writeRecord());
        readButton.addActionListener(e -> readRecord());
        updateButton.addActionListener(e -> updateRecord());
        deleteButton.addActionListener(e -> deleteRecord());
        displayAllButton.addActionListener(e -> displayAllRecords());
    }
    
    private void writeRecord() {
        try {
            int pos = Integer.parseInt(positionField.getText());
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            
            try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw")) {
                file.seek(pos * RECORD_SIZE);
                String record = String.format("%-20s %-5d", name, age);
                file.writeBytes(record);
                displayArea.setText("Record written successfully at position " + pos);
            }
        } catch (NumberFormatException | IOException e) {
            displayArea.setText("Error: " + e.getMessage());
        }
    }
    
    private void readRecord() {
        try {
            int pos = Integer.parseInt(positionField.getText());
            
            try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "r")) {
                file.seek(pos * RECORD_SIZE);
                byte[] recordBytes = new byte[RECORD_SIZE];
                file.read(recordBytes);
                String record = new String(recordBytes).trim();
                
                if (record.isEmpty() || record.equals("null")) {
                    displayArea.setText("No record found at position " + pos);
                } else {
                    nameField.setText(record.substring(0, 20).trim());
                    ageField.setText(record.substring(20).trim());
                    displayArea.setText("Record at position " + pos + ": " + record);
                }
            }
        } catch (NumberFormatException | IOException e) {
            displayArea.setText("Error: " + e.getMessage());
        }
    }
    
    private void updateRecord() {
        try {
            int pos = Integer.parseInt(positionField.getText());
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            
            try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw")) {
                file.seek(pos * RECORD_SIZE);
                byte[] recordBytes = new byte[RECORD_SIZE];
                file.read(recordBytes);
                String existingRecord = new String(recordBytes).trim();
                
                if (existingRecord.isEmpty() || existingRecord.equals("null")) {
                    displayArea.setText("No record to update at position " + pos);
                } else {
                    file.seek(pos * RECORD_SIZE);
                    String record = String.format("%-20s %-5d", name, age);
                    file.writeBytes(record);
                    displayArea.setText("Record updated successfully at position " + pos);
                }
            }
        } catch (NumberFormatException | IOException e) {
            displayArea.setText("Error: " + e.getMessage());
        }
    }
    
    private void deleteRecord() {
        try {
            int pos = Integer.parseInt(positionField.getText());
            
            try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw")) {
                file.seek(pos * RECORD_SIZE);
                byte[] recordBytes = new byte[RECORD_SIZE];
                file.read(recordBytes);
                String existingRecord = new String(recordBytes).trim();
                
                if (existingRecord.isEmpty() || existingRecord.equals("null")) {
                    displayArea.setText("No record to delete at position " + pos);
                } else {
                    file.seek(pos * RECORD_SIZE);
                    file.writeBytes(String.format("%-20s %-5d", "null", 0));
                    displayArea.setText("Record deleted successfully at position " + pos);
                    nameField.setText("");
                    ageField.setText("");
                }
            }
        } catch (NumberFormatException | IOException e) {
            displayArea.setText("Error: " + e.getMessage());
        }
    }
    
    private void displayAllRecords() {
        try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "r")) {
            StringBuilder sb = new StringBuilder("All Records:\n");
            
            for (int i = 0; i < 10; i++) {
                file.seek(i * RECORD_SIZE);
                byte[] recordBytes = new byte[RECORD_SIZE];
                file.read(recordBytes);
                String record = new String(recordBytes).trim();
                
                if (!record.isEmpty() && !record.equals("null")) {
                    sb.append("Position ").append(i).append(": ").append(record).append("\n");
                }
            }
            
            displayArea.setText(sb.toString());
        } catch (IOException e) {
            displayArea.setText("Error: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RandomAccessSwingApp app = new RandomAccessSwingApp();
            app.setVisible(true);
        });
    }
}