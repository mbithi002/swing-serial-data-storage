import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class App extends JFrame {
    private List<Person> dataList = new ArrayList<>();
    private JTextField idField, nameField, ageField;
    private JTextArea displayArea;
    private final String binaryFile = "data.bin";
    private final String serialFile = "data.ser";

    public App() {
        setTitle("Data Storage Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");
        JButton binarySaveBtn = new JButton("Save Binary");
        JButton binaryLoadBtn = new JButton("Load Binary");
        JButton serialSaveBtn = new JButton("Save Serial");
        JButton serialLoadBtn = new JButton("Load Serial");

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(binarySaveBtn);
        buttonPanel.add(binaryLoadBtn);
        buttonPanel.add(serialSaveBtn);
        buttonPanel.add(serialLoadBtn);

        // Display area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Button actions
        addBtn.addActionListener(e -> addRecord());
        updateBtn.addActionListener(e -> updateRecord());
        deleteBtn.addActionListener(e -> deleteRecord());
        clearBtn.addActionListener(e -> clearFields());
        binarySaveBtn.addActionListener(e -> saveBinary());
        binaryLoadBtn.addActionListener(e -> loadBinary());
        serialSaveBtn.addActionListener(e -> saveSerial());
        serialLoadBtn.addActionListener(e -> loadSerial());

        updateDisplay();
    }

    private void addRecord() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            dataList.add(new Person(id, name, age));
            updateDisplay();
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format");
        }
    }

    private void updateRecord() {
        try {
            int id = Integer.parseInt(idField.getText());
            for (Person p : dataList) {
                if (p.getId() == id) {
                    p.setName(nameField.getText());
                    p.setAge(Integer.parseInt(ageField.getText()));
                    updateDisplay();
                    clearFields();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Record not found");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format");
        }
    }

    private void deleteRecord() {
        try {
            int id = Integer.parseInt(idField.getText());
            dataList.removeIf(p -> p.getId() == id);
            updateDisplay();
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format");
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
    }

    private void updateDisplay() {
        displayArea.setText("");
        for (Person p : dataList) {
            displayArea.append(p.toString() + "\n");
        }
    }

    private void saveBinary() {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(binaryFile))) {
            dos.writeInt(dataList.size());
            for (Person p : dataList) {
                dos.writeInt(p.getId());
                dos.writeUTF(p.getName());
                dos.writeInt(p.getAge());
            }
            JOptionPane.showMessageDialog(this, "Data saved in binary format");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving binary file");
        }
    }

    private void loadBinary() {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(binaryFile))) {
            int size = dis.readInt();
            dataList.clear();
            for (int i = 0; i < size; i++) {
                int id = dis.readInt();
                String name = dis.readUTF();
                int age = dis.readInt();
                dataList.add(new Person(id, name, age));
            }
            updateDisplay();
            JOptionPane.showMessageDialog(this, "Data loaded from binary file");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading binary file");
        }
    }

    private void saveSerial() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serialFile))) {
            oos.writeObject(dataList);
            JOptionPane.showMessageDialog(this, "Data saved in serialized format");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving serialized file");
        }
    }

    private void loadSerial() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serialFile))) {
            dataList = (List<Person>) ois.readObject();
            updateDisplay();
            JOptionPane.showMessageDialog(this, "Data loaded from serialized file");
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error loading serialized file");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.setVisible(true);
        });
    }
}

class Person implements Serializable {
    private int id;
    private String name;
    private int age;

    public Person(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Age: " + age;
    }
}