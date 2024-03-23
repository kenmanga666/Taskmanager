package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import org.json.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskManager extends JFrame {
    private DefaultListModel<Task> taskListModel = new DefaultListModel<>();
    private boolean isSpeedyManager;
    private String selectedcategory;

    public TaskManager(String title, String selectedcategory, String selectedcategoryfilepath, boolean isSpeedyManager) {
        this.isSpeedyManager = isSpeedyManager;
        this.selectedcategory = selectedcategory;
        setTitle(title);
        setSize(680, 400);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveTasksByCategory();
                // Close the task manager and return to the main page
                mainPage.main(null);
            }
        });
        setLocationRelativeTo(null);
        InitializeUI(this);
        if (!isSpeedyManager) {
            loadTasksFromFile(selectedcategoryfilepath);
        }
    }

    private void InitializeUI(JFrame frame) {
        // Create a panel to store the text area and the button
        JPanel panel1 = new JPanel();
        // Create a text area and a button to add a new task and add them to the panel1
        JTextField textField = new JTextField(20);
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

        /*
         * Add an action listener to the addTaskButton that
         * adds a new task to the list of tasks when the button is clicked.
         */
        addTaskButton.addActionListener(e -> {
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
                    speedCategory = JOptionPane.showInputDialog(frame, "Enter category:");
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
                        task = new Task(taskTitle, selectedPriority, "", null, dueDate, speedCategory);
                    }else {
                        task = new Task(taskTitle, selectedPriority, "", null, dueDate, selectedcategory);
                    }
                    taskListModel.addElement(task);
                    textField.setText("");
                    saveTasksByCategory();
                }
            }
        });
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
                subTaskManager subTaskManagerWindow = new subTaskManager(selectedTask, selectedTask.getTitle() + " Subtasks", selectedTask.getFilepath());
                subTaskManagerWindow.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a task to manage subtasks", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // add the panels to the frame
        frame.add(panel1, BorderLayout.SOUTH);
        frame.add(panel2, BorderLayout.CENTER);
    }

    private JList<Task> createTaskList() {
        // Create a list to store the tasks
        JList<Task> taskList = new JList<>(taskListModel);
        // Set the cell renderer for the task list
        taskList.setCellRenderer(new TaskListCellRenderer());
        return taskList;
    }

    /**
     * Removes a task from the list of tasks.
     * @param taskList The list of tasks from which the task will be removed.
     */
    private void removeTask(JList<Task> taskList) {
        // Remove the selected task from the list
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this task?", "Delete Task", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                taskListModel.remove(selectedIndex);
            }
        // If no task is selected, Remove all tasks
        } else if (taskListModel.size() > 0) {
            int choice = JOptionPane.showConfirmDialog(null, "Do you want to delete all tasks?", "Delete All Tasks", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                taskListModel.removeAllElements();
            }
        } else {
            JOptionPane.showMessageDialog(null, "There's no tasks to delete", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads tasks from a file.
     * @param filePath The file path from which the tasks will be loaded.
     */
    private void loadTasksFromFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            // If the file is empty, return
            if (file.length() == 0) {
                return;
            }
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuilder jsonContent = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    jsonContent.append(line);
                }
                bufferedReader.close();
                JSONArray jsonArray = new JSONArray(jsonContent.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString("Title");
                    Task.Priority priority = Task.Priority.valueOf(jsonObject.getString("Priority"));
                    String description = jsonObject.getString("Description");
                    String creationDate = jsonObject.getString("Creation date");
                    Task[] subTaskList = Task.getSubTaskListFromString(jsonObject.getString("SubTasks"));
                    String category = jsonObject.getString("Category");
                    Task task = new Task(title, priority, description, subTaskList, creationDate, category);
                    taskListModel.addElement(task);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the list of tasks to a file.
     * @param tasks     The list of tasks to be saved.
     * @param filePath  The file path where the tasks will be saved.
     */
    private static void saveTasksToFile(List<Task> tasks, String filePath) {
        if (!new File(filePath).exists()) {
            addFilePath(filePath);
        }
        try (FileWriter file = new FileWriter(filePath)) {
            file.write("[");
            // If the list of tasks is empty (all tasks have been removed), delete the file and the filepath from taskfiles.txt
            if (tasks.isEmpty()) {
                File fileToDelete = new File(filePath);
                fileToDelete.delete();
                removeFilePath(filePath);
                return;
            } else {
                file.write("\n");
            }
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                file.write("  {\n");
                file.write("    \"Title\": \"" + task.getTitle() + "\",\n");
                file.write("    \"Priority\": \"" + task.getPriority() + "\",\n");
                file.write("    \"Description\": \"" + task.getDescription() + "\",\n");
                file.write("    \"Creation date\": \"" + task.getDueDate() + "\",\n");
                file.write("    \"Category\": \"" + task.getCategory() + "\",\n");
                file.write("    \"SubTasks\": \"[" + task.getSubTaskListToString() + "]\"\n");
                file.write("  }");
                // Add a comma and a new line if the task is not the last task
                if (i < tasks.size() - 1) {
                    file.write(",\n");
                } else {
                    file.write("\n");
                }
            }
            file.write("]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save tasks to files based on category
     */
    private void saveTasksByCategory() {
        Map<String, List<Task>> tasksByCategory = new HashMap<String, List<Task>>();

        // If the list of tasks is empty (all tasks have been removed), delete the file and the filepath from taskfiles.txt
        if (taskListModel.size() == 0 && !isSpeedyManager) {
            File fileToDelete = new File("static/" + selectedcategory + ".json");
            fileToDelete.delete();
            removeFilePath("static/" + selectedcategory + ".json");
            return;
        }
                
        // Group tasks by category in a map (only if it's a speedy manager because else it's already grouped by category)
        if (isSpeedyManager) {
            for (int i = 0; i < taskListModel.size(); i++) {
                Task task = taskListModel.getElementAt(i);
                String category = task.getCategory();
                if (!tasksByCategory.containsKey(category)) {
                    tasksByCategory.put(category, new ArrayList<>());
                }
                tasksByCategory.get(category).add(task);
            }
        } else {
            tasksByCategory.put(selectedcategory, new ArrayList<>());
            for (int i = 0; i < taskListModel.size(); i++) {
                Task task = taskListModel.getElementAt(i);
                tasksByCategory.get(selectedcategory).add(task);
            }
        }

        // Save tasks to files based on category
        for (String category : tasksByCategory.keySet()) {
            String filePath = "static/" + category + ".json";
            List<Task> tasks = tasksByCategory.get(category);
            saveTasksToFile(tasks, filePath);
        }
    }

    /**
     * Add the file path to the taskfiles.txt file
     * @param filePath The file path to be added
     */
    private static void addFilePath(String filePath) {
        try {
            
            FileWriter fileWriter = new FileWriter("static/taskfiles.txt", true);
            FileReader fileReader = new FileReader("static/taskfiles.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            boolean fileExists = false;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals(filePath)) {
                    fileExists = true;
                    break;
                }
            }
            if (!fileExists) {
                while ((line = bufferedReader.readLine()) != null) {
                    fileWriter.write(line);
                }
                fileWriter.write(filePath + "\n");
            }
            fileWriter.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove the file path from the taskfiles.txt file
     * @param filePath The file path to be removed
     */
    private static void removeFilePath(String filePath) {
        try {
            FileReader fileReader = new FileReader("static/taskfiles.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder newTaskFiles = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.equals(filePath)) {
                    newTaskFiles.append(line + "\n");
                }
            }
            FileWriter fileWriter = new FileWriter("static/taskfiles.txt");
            fileWriter.write(newTaskFiles.toString());
            fileWriter.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}