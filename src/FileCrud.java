import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.*;


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
    private JTextField nameField, ageField;
    private JTextArea displayArea;
    private List<Record> records = new ArrayList<>();
    private final String TEXT_FILE = "records.txt";
    private final String BINARY_FILE = "records.dat";

    public FileCRUD() {
        setTitle("File CRUD Application");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        nameField = new JTextField(10);
        ageField = new JTextField(5);
        JButton addButton = new JButton("Add");
        JButton loadButton = new JButton("Load");
        JButton saveBinaryButton = new JButton("Save Binary");
        JButton loadBinaryButton = new JButton("Load Binary");
        displayArea = new JTextArea(10, 30);

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Age:"));
        add(ageField);
        add(addButton);
        add(loadButton);
        add(saveBinaryButton);
        add(loadBinaryButton);
        add(new JScrollPane(displayArea));

        addButton.addActionListener(e -> addRecord());
        loadButton.addActionListener(e -> loadRecords());
        saveBinaryButton.addActionListener(e -> saveBinary());
        loadBinaryButton.addActionListener(e -> loadBinary());
    }

    private void addRecord() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        records.add(new Record(name, age));
        saveRecords();
    }

    private void saveRecords() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEXT_FILE))) {
            for (Record r : records) {
                writer.println(r.name + "," + r.age);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRecords() {
        records.clear();
        try (Scanner scanner = new Scanner(new File(TEXT_FILE))) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                records.add(new Record(data[0], Integer.parseInt(data[1])));
            }
            displayRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayRecords() {
        displayArea.setText("");
        for (Record r : records) {
            displayArea.append(r.toString() + "\n");
        }
    }

    private void saveBinary() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BINARY_FILE))) {
            oos.writeObject(records);
        } catch (IOException e) {
            e.printStackTrace();
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
        SwingUtilities.invokeLater(() -> new FileCRUD().setVisible(true));
    }
}
