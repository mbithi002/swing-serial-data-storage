import java.io.*;
import java.util.Scanner;

public class RandomAccessConsoleApp {
    private static final String FILE_NAME = "random_access_data.txt";
    private static final int RECORD_SIZE = 50; // Fixed size for each record
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\nRandom Access File Operations:");
            System.out.println("1. Write record");
            System.out.println("2. Read record");
            System.out.println("3. Update record");
            System.out.println("4. Delete record");
            System.out.println("5. Display all records");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter record position (0-9): ");
                        int pos = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter age: ");
                        int age = scanner.nextInt();
                        writeRecord(pos, name, age);
                        break;
                    case 2:
                        System.out.print("Enter record position to read (0-9): ");
                        pos = scanner.nextInt();
                        readRecord(pos);
                        break;
                    case 3:
                        System.out.print("Enter record position to update (0-9): ");
                        pos = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter new name: ");
                        name = scanner.nextLine();
                        System.out.print("Enter new age: ");
                        age = scanner.nextInt();
                        updateRecord(pos, name, age);
                        break;
                    case 4:
                        System.out.print("Enter record position to delete (0-9): ");
                        pos = scanner.nextInt();
                        deleteRecord(pos);
                        break;
                    case 5:
                        displayAllRecords();
                        break;
                    case 6:
                        System.out.println("Exiting program...");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static void writeRecord(int pos, String name, int age) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw")) {
            file.seek(pos * RECORD_SIZE);
            String record = String.format("%-20s %-5d", name, age);
            file.writeBytes(record);
            System.out.println("Record written successfully!");
        }
    }
    
    private static void readRecord(int pos) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "r")) {
            file.seek(pos * RECORD_SIZE);
            byte[] recordBytes = new byte[RECORD_SIZE];
            file.read(recordBytes);
            String record = new String(recordBytes).trim();
            if (record.isEmpty() || record.equals("null")) {
                System.out.println("No record found at position " + pos);
            } else {
                System.out.println("Record at position " + pos + ": " + record);
            }
        }
    }
    
    private static void updateRecord(int pos, String name, int age) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw")) {
            file.seek(pos * RECORD_SIZE);
            byte[] recordBytes = new byte[RECORD_SIZE];
            file.read(recordBytes);
            String existingRecord = new String(recordBytes).trim();
            
            if (existingRecord.isEmpty() || existingRecord.equals("null")) {
                System.out.println("No record to update at position " + pos);
            } else {
                file.seek(pos * RECORD_SIZE);
                String record = String.format("%-20s %-5d", name, age);
                file.writeBytes(record);
                System.out.println("Record updated successfully!");
            }
        }
    }
    
    private static void deleteRecord(int pos) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw")) {
            file.seek(pos * RECORD_SIZE);
            byte[] recordBytes = new byte[RECORD_SIZE];
            file.read(recordBytes);
            String existingRecord = new String(recordBytes).trim();
            
            if (existingRecord.isEmpty() || existingRecord.equals("null")) {
                System.out.println("No record to delete at position " + pos);
            } else {
                file.seek(pos * RECORD_SIZE);
                file.writeBytes(String.format("%-20s %-5d", "null", 0));
                System.out.println("Record deleted successfully!");
            }
        }
    }
    
    private static void displayAllRecords() throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "r")) {
            System.out.println("\nAll Records:");
            for (int i = 0; i < 10; i++) { // Assuming max 10 records
                file.seek(i * RECORD_SIZE);
                byte[] recordBytes = new byte[RECORD_SIZE];
                file.read(recordBytes);
                String record = new String(recordBytes).trim();
                if (!record.isEmpty() && !record.equals("null")) {
                    System.out.println("Position " + i + ": " + record);
                }
            }
        }
    }
}