import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ToDoListApp extends JFrame {
    private TaskManager taskManager;
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JTextField taskInputField;
    
    public ToDoListApp() {
        taskManager = new TaskManager();
        
        setTitle("To-Do List Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        
        createTaskTable();
        JScrollPane scrollPane = new JScrollPane(taskTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        refreshTaskTable();
    }
    
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        
        taskInputField = new JTextField();
        JButton addButton = new JButton("Add Task");
        
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });
        
        taskInputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });
        
        inputPanel.add(new JLabel("New Task: "), BorderLayout.WEST);
        inputPanel.add(taskInputField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);
        
        return inputPanel;
    }
    
    private void createTaskTable() {
        String[] columns = {"ID", "Description", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        taskTable = new JTable(tableModel);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskTable.getTableHeader().setReorderingAllowed(false);
        
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        taskTable.getColumnModel().getColumn(1).setPreferredWidth(400);
        taskTable.getColumnModel().getColumn(2).setPreferredWidth(70);
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton completeButton = new JButton("Mark Completed");
        JButton deleteButton = new JButton("Delete Task");
        
        completeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                markTaskAsCompleted();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteTask();
            }
        });
        
        buttonPanel.add(completeButton);
        buttonPanel.add(deleteButton);
        
        return buttonPanel;
    }
    
    private void addTask() {
        String taskDescription = taskInputField.getText().trim();
        if (!taskDescription.isEmpty()) {
            taskManager.addTask(taskDescription);
            taskInputField.setText("");
            refreshTaskTable();
        } else {
            JOptionPane.showMessageDialog(this, "Task description cannot be empty!",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void markTaskAsCompleted() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            int taskId = (int) taskTable.getValueAt(selectedRow, 0);
            taskManager.markTaskAsCompleted(taskId);
            refreshTaskTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to mark as completed.",
                    "Selection Required", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void deleteTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            int taskId = (int) taskTable.getValueAt(selectedRow, 0);
            taskManager.deleteTask(taskId);
            refreshTaskTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to delete.",
                    "Selection Required", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void refreshTaskTable() {
        // Clear existing rows
        tableModel.setRowCount(0);
        
        List<Task> tasks = taskManager.getAllTasks();
        for (Task task : tasks) {
            Object[] rowData = {
                task.getId(),
                task.getDescription(),
                task.isCompleted() ? "Completed" : "Pending"
            };
            tableModel.addRow(rowData);
        }
    }
    
    public static void main(String[] args) {
        // Use the Event Dispatch Thread for Swing applications
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ToDoListApp app = new ToDoListApp();
                app.setVisible(true);
            }
        });
    }
}