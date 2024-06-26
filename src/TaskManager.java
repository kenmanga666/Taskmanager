package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TaskManager extends JFrame {
    public static int taskID = FileManager.getIDFromFile();
    JTextField textField;
    private DefaultListModel<Task> taskListModel = new DefaultListModel<>();
    private boolean isSpeedyManager;
    private String selectedcategory;

    public TaskManager(String title, String selectedcategory, String selectedcategoryfilepath, boolean isSpeedyManager) {
        this.isSpeedyManager = isSpeedyManager;
        this.selectedcategory = selectedcategory;
        setTitle(title);
        setSize(730, 400);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                FileManager.saveTasksByCategory(taskListModel, null, isSpeedyManager, selectedcategory, true);
                // Close the task manager and return to the main page
                MainPage.main(null);
            }
        });
        setLocationRelativeTo(null);
        InitializeUI();
        if (!isSpeedyManager) {
            FileManager.loadTasksFromFile(selectedcategoryfilepath, taskListModel);
        }
    }

    private void InitializeUI() {
        // Create a panel to store the text area and the button
        JPanel panel1 = new JPanel();
        // Create a text area and a button to add a new task and add them to the panel1
        textField = new JTextField(20);
        JButton addTaskButton = new JButton("Add Task");
        panel1.add(textField);
        panel1.add(addTaskButton);
        // Create a button to remove a task and add it to the panel1
        JButton removeTaskButton = new JButton("Remove Task");
        panel1.add(removeTaskButton);
        // Create a button to open a task and add it to the panel1
        JButton openTaskButton = new JButton("Open Task");
        panel1.add(openTaskButton);
        // Create a button to manage the subtasks and add it to the panel1
        JButton manageSubtasksButton = new JButton("Manage Subtasks");
        panel1.add(manageSubtasksButton);
        // Set the layout of the panel to FlowLayout
        panel1.setLayout(new FlowLayout());  

        /* Create a panel to store the list of tasks and a scroll pane
           Create a list to store the tasks and add it to the panel2 */
        JPanel panel2 = new JPanel();
        JList<Task> taskList = createTaskList();
        panel2.add(taskList, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(taskList);
        panel2.add(scrollPane);
        // Set the layout of the panel to BorderLayout
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));

        if (!isSpeedyManager) {
            // Create panel to store the buttons to move a task up and down
            JPanel moveTaskPanel = new JPanel();
            // Create buttons to move a task up and down and add them to the panel3
            JButton moveTaskUpButton = new JButton("ʌ");
            moveTaskPanel.add(moveTaskUpButton);
            JButton moveTaskDownButton = new JButton("v");
            moveTaskPanel.add(moveTaskDownButton);
            // Set the layout of the panel to BoxLayout to stack the buttons vertically
            moveTaskPanel.setLayout(new BoxLayout(moveTaskPanel, BoxLayout.PAGE_AXIS));
            // add panel3 to the panel1
            panel1.add(moveTaskPanel);

            /*
             * Add an action listener to the moveTaskUpButton that
             * moves the selected task up in the list of tasks when the button is clicked.
             */
            moveTaskUpButton.addActionListener(e -> moveTaskUp(taskList));
            /*
             * Add an action listener to the moveTaskDownButton that
             * moves the selected task down in the list of tasks when the button is clicked.
             */
            moveTaskDownButton.addActionListener(e -> moveTaskDown(taskList));
        }
        

        /*
         * Add an action listener to the addTaskButton that
         * adds a new task to the list of tasks when the button is clicked.
         */
        addTaskButton.addActionListener(e -> addtask(this));
        /*
         * Add an action listener to the removeTaskButton that
         * removes the selected task from the list of tasks
         * when the button is clicked.
         */
        removeTaskButton.addActionListener(e -> removeTask(taskList));
        /*
         * Add an action listener to the openTaskButton that
         * opens a taskDetails window when the button is clicked.
         * The window displays the details of the selected task.
         */
        openTaskButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                Task selectedTask = taskListModel.getElementAt(selectedIndex);
                TaskDetails taskDetailsWindow = new TaskDetails(selectedTask, selectedTask.getFilepath());
                taskDetailsWindow.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a task to open", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        /*
         * Add an action listener to the manageSubtasksButton that
         * opens a subTaskManager window when the button is clicked.
         * The window allows the user to manage the subtasks of the selected task.
         */
        manageSubtasksButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                Task selectedTask = taskListModel.getElementAt(selectedIndex);
                SubTaskManager subTaskManagerWindow = new SubTaskManager(selectedTask, selectedTask.getTitle() + " Subtasks", selectedTask.getFilepath());
                subTaskManagerWindow.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a task to manage subtasks", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // add the panels to the frame
        this.add(panel1, BorderLayout.SOUTH);
        this.add(panel2, BorderLayout.CENTER);
    }

    private JList<Task> createTaskList() {
        // Create a list to store the tasks
        JList<Task> taskList = new JList<>(taskListModel);
        // Set the cell renderer for the task list
        taskList.setCellRenderer(new TaskListCellRenderer());
        return taskList;
    }

    private void addtask(JFrame frame) {
        String taskTitle = textField.getText().trim();
        if (!taskTitle.isEmpty()) {
            /*
             * Display a dialog box to prompt the user to select a priority
             */
            Task.Priority selectedPriority = (Task.Priority) JOptionPane.showInputDialog(frame, "Select priority:",
                    "Priority", JOptionPane.QUESTION_MESSAGE, null, Task.Priority.values(), Task.Priority.MEDIUM);
            /*
             * Display a dialog box to prompt the user to enter a category for the task if it is a speedy manager task
             */
            String speedCategory = "";
            if (isSpeedyManager) {
                while (speedCategory.isEmpty()) {
                    speedCategory = JOptionPane.showInputDialog(frame, "Enter category:");
                    // If the user cancels the input, return (do not add the task)
                    if (speedCategory == null) {
                        return;
                    }
                    speedCategory = speedCategory.trim();
                    // If the category is empty, display an error message
                    if (speedCategory.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Category cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            if (selectedPriority != null) {
                // Create a new task and add it to the list of tasks
                Task task;
                String dueDate = "";
                while (dueDate.length() != 10) {
                    dueDate = JOptionPane.showInputDialog(frame, "Enter Due Date (dd-mm-yyyy):");
                    // If the user cancels the input, return (do not add the task)
                    if (dueDate == null) {
                        return;
                    }
                    // If the date is not in the format dd-mm-yyyy, display an error message
                    if (dueDate.length() != 10) {
                        JOptionPane.showMessageDialog(null, "Invalid date format. Please enter the date in the format dd-mm-yyyy", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (isSpeedyManager) {
                    task = new Task(taskTitle, selectedPriority, "", null, dueDate, speedCategory, taskID);
                }else {
                    task = new Task(taskTitle, selectedPriority, "", null, dueDate, selectedcategory, taskID);
                }
                taskID++;
                FileManager.saveIDToFile(taskID);
                taskListModel.addElement(task);
                textField.setText("");
                FileManager.saveTasksByCategory(taskListModel, null, isSpeedyManager, selectedcategory, false);
            }
        }
    }

    /**
     * Removes a task from the list of tasks.
     * @param taskList The list of tasks from which the task will be removed.
     */
    private void removeTask(JList<Task> taskList) {
        // Remove the selected task from the list
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task selectedTask = taskListModel.getElementAt(selectedIndex);
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this task ? (selected task : " + selectedTask.getTitle() + ")", "Delete Task", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                DefaultListModel<Task> ancientListModel = taskListModel;
                ancientListModel.getElementAt(selectedIndex).setRemoved();
                taskListModel.remove(selectedIndex);
                FileManager.saveTasksByCategory(taskListModel, ancientListModel, isSpeedyManager, selectedcategory, false);
            }
        // If no task is selected, propose to remove all tasks
        } else if (taskListModel.size() > 0) {
            int choice = JOptionPane.showConfirmDialog(null, "Do you want to delete all tasks?", "Delete All Tasks", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                DefaultListModel<Task> ancientListModel = taskListModel;
                for (int i = 0; i < ancientListModel.size(); i++) {
                    ancientListModel.getElementAt(i).setRemoved();
                }
                taskListModel.removeAllElements();
                FileManager.saveTasksByCategory(taskListModel, ancientListModel, isSpeedyManager, selectedcategory, false);
            }
        } else {
            JOptionPane.showMessageDialog(null, "There's no tasks to delete", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Move the selected task of 1 position up in the list of tasks if up key is pressed
     * @param taskList The list of tasks
     */
    public void moveTaskUp(JList<Task> taskList) {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1 && selectedIndex != 0) {
            Task task = taskListModel.getElementAt(selectedIndex);
            taskListModel.remove(selectedIndex);
            taskListModel.add(selectedIndex - 1, task);
            // Select the task that was moved up
            taskList.setSelectedIndex(selectedIndex - 1);
        } else if (selectedIndex == 0) {
            JOptionPane.showMessageDialog(null, "The task is already at the top of the list", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Please select a task to move up", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Move the selected task of 1 position down in the list of tasks if down key is pressed
     * @param taskList The list of tasks
     */
    public void moveTaskDown(JList<Task> taskList) {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1 && selectedIndex != taskListModel.size() - 1) {
            Task task = taskListModel.getElementAt(selectedIndex);
            taskListModel.remove(selectedIndex);
            taskListModel.add(selectedIndex + 1, task);
            // Select the task that was moved down
            taskList.setSelectedIndex(selectedIndex + 1);
        }else if (selectedIndex == taskListModel.size() - 1) {
            JOptionPane.showMessageDialog(null, "The task is already at the bottom of the list", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Please select a task to move down", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}