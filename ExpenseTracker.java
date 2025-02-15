import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class ExpenseTracker {
    private JFrame frame;
    private JTextField amountField;
    private JComboBox<String> categoryBox;
    private JButton addButton, viewButton;
    private JTextArea displayArea;
    
    private ArrayList<String> expenses;
    private final String FILE_NAME = "expenses.txt";
    
    public ExpenseTracker() {
        frame = new JFrame("Expense Tracker");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        
        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField(10);
        
        JLabel categoryLabel = new JLabel("Category:");
        String[] categories = {"Food", "Transport", "Entertainment", "Others"};
        categoryBox = new JComboBox<>(categories);
        
        addButton = new JButton("Add Expense");
        viewButton = new JButton("View Expenses");
        displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);
        
        expenses = new ArrayList<>();
        loadExpenses();
        
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });
        
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewExpenses();
            }
        });
        
        frame.add(amountLabel);
        frame.add(amountField);
        frame.add(categoryLabel);
        frame.add(categoryBox);
        frame.add(addButton);
        frame.add(viewButton);
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
