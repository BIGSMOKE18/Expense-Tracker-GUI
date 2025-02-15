import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseTracker {
    private JFrame frame;
    private JTextField amountField;
    private JComboBox<String> categoryBox;
    private JButton addButton, viewButton, sortButton, filterButton, sumButton;
    private JTextArea displayArea;
    
    private ArrayList<String> expenses;
    private final String FILE_NAME = "expenses.txt";
    
    public ExpenseTracker() {
        frame = new JFrame("Expense Tracker");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        
        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField(10);
        
        JLabel categoryLabel = new JLabel("Category:");
        String[] categories = {"Food", "Transport", "Entertainment", "Others"};
        categoryBox = new JComboBox<>(categories);
        
        addButton = new JButton("Add Expense");
        viewButton = new JButton("View Expenses");
        sortButton = new JButton("Sort by Amount");
        filterButton = new JButton("Filter by Category");
        sumButton = new JButton("Category-wise Summation");
        displayArea = new JTextArea(15, 40);
        displayArea.setEditable(false);
        
        expenses = new ArrayList<>();
        loadExpenses();
        
        addButton.addActionListener(e -> addExpense());
        viewButton.addActionListener(e -> viewExpenses());
        sortButton.addActionListener(e -> sortExpenses());
        filterButton.addActionListener(e -> filterExpenses());
        sumButton.addActionListener(e -> categorySummation());
        
        frame.add(amountLabel);
        frame.add(amountField);
        frame.add(categoryLabel);
        frame.add(categoryBox);
        frame.add(addButton);
        frame.add(viewButton);
        frame.add(sortButton);
        frame.add(filterButton);
        frame.add(sumButton);
        frame.add(new JScrollPane(displayArea));
        
        frame.setVisible(true);
    }
    
    private void addExpense() {
        String amount = amountField.getText();
        String category = (String) categoryBox.getSelectedItem();
        
        if (amount.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter an amount.");
            return;
        }
        
        String expense = amount + " - " + category;
        expenses.add(expense);
        saveExpenses();
        amountField.setText("");
        JOptionPane.showMessageDialog(frame, "Expense added successfully!");
    }
    
    private void viewExpenses() {
        displayArea.setText("Expenses:\n");
        for (String exp : expenses) {
            displayArea.append(exp + "\n");
        }
    }
    
    private void sortExpenses() {
        expenses.sort(Comparator.comparingInt(e -> Integer.parseInt(e.split(" - ")[0])));
        viewExpenses();
    }
    
    private void filterExpenses() {
        String category = (String) categoryBox.getSelectedItem();
        List<String> filtered = expenses.stream()
                .filter(e -> e.endsWith(category))
                .collect(Collectors.toList());
        displayArea.setText("Filtered Expenses:\n");
        for (String exp : filtered) {
            displayArea.append(exp + "\n");
        }
    }
    
    private void categorySummation() {
        Map<String, Integer> categorySum = new HashMap<>();
        for (String exp : expenses) {
            String[] parts = exp.split(" - ");
            int amount = Integer.parseInt(parts[0]);
            String category = parts[1];
            categorySum.put(category, categorySum.getOrDefault(category, 0) + amount);
        }
        
        displayArea.setText("Category-wise Summation:\n");
        for (Map.Entry<String, Integer> entry : categorySum.entrySet()) {
            displayArea.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }
    }
    
    private void saveExpenses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String exp : expenses) {
                writer.write(exp);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving expenses.");
        }
    }
    
    private void loadExpenses() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                expenses.add(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error loading expenses.");
        }
    }
    
    public static void main(String[] args) {
        new ExpenseTracker();
    }
}
