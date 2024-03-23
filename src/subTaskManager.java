package src;

import javax.swing.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class subTaskManager extends JFrame {
    private DefaultListModel<Task> subTaskListModel = new DefaultListModel<>();

    public subTaskManager(Task currentTask, String frameTitle, String filepath) {
        setTitle(frameTitle);
        setSize(500, 300);
        setLocationRelativeTo(null);
        InitializeUI(this, currentTask);
        loadSubTasks(currentTask);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveSubTasksToFile(subTaskListModel, currentTask, filepath);
            }
        });
    }

    private void InitializeUI(JFrame frame, Task currentTask) {
        // Create a panel to store the text area and the button
        JPanel panel1 = new JPanel();
        // Create a text area and a button to add a new task and add them to the panel1
        JTextField textField = new JTextField(20);
        JButton addSubTaskButton = new JButton("Add SubTask");
        panel1.add(textField);
        panel1.add(addSubTaskButton);
        // Create a button to remove a task and add it to the panel1
        JButton removeSubTaskButton = new JButton("Remove SubTask");
        panel1.add(removeSubTaskButton);
        // Set the layout of the panel to FlowLayout
        panel1.setLayout(new FlowLayout());

        /* Create a panel to store the list of tasks and a scroll pane
           Create a list to store the tasks and add it to the panel2 */
        JPanel panel2 = new JPanel();
        JList<Task> subTaskList = createTaskList();
        panel2.add(subTaskList, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(subTaskList);
        panel2.add(scrollPane);
        // Set the layout of the panel to BorderLayout
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));

        // Add action listeners to the buttons
        addSubTaskButton.addActionListener(e -> {
            String subTaskTitle = textField.getText().trim();
            if (!subTaskTitle.isEmpty()) {
                Task.Priority selectedPriority = (Task.Priority) JOptionPane.showInputDialog(frame, "Select priority:",
                        "Priority", JOptionPane.QUESTION_MESSAGE, null, Task.Priority.values(), Task.Priority.MEDIUM);
                if (selectedPriority != null) {
                    Task subTask = new Task(subTaskTitle, selectedPriority, "", null, null, null);
                    subTaskListModel.addElement(subTask);
                    currentTask.appendToSubTaskList(subTask);
                    textField.setText("");
                }
            }
        });
        removeSubTaskButton.addActionListener(e -> removeSubTask(subTaskList, currentTask));

        // add the panels to the frame
        frame.add(panel1, BorderLayout.SOUTH);
        frame.add(panel2, BorderLayout.CENTER);
    }

    private JList<Task> createTaskList() {
        // Create a list to store the tasks
        JList<Task> subTaskList = new JList<>(subTaskListModel);
        // Set the cell renderer for the task list
        subTaskList.setCellRenderer(new TaskListCellRenderer());
        return subTaskList;
    }

    private void removeSubTask(JList<Task> subTaskList, Task currentTask) {
        // Remove a task from the list
        int selectedIndex = subTaskList.getSelectedIndex();
        if (selectedIndex != -1) {
            subTaskListModel.remove(selectedIndex);
            currentTask.removeFromSubTaskList(selectedIndex);
        }
    }

    private void saveSubTasksToFile(DefaultListModel<Task> subTaskList, Task currentTask, String filepath) {
        // Save the subTasks to a file
        File file = new File(filepath);
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonContent.append(line);
            }
            bufferedReader.close();
            JSONArray taskArray = new JSONArray(jsonContent.toString());
            for (int i = 0; i < taskArray.length(); i++) {
                JSONObject taskObject = taskArray.getJSONObject(i);
                String oldSubTaskList = taskObject.getString("SubTasks");
                String newSubTaskList = currentTask.getSubTaskListToString();
                if (currentTask.getSubTaskList().length > 0) {
                    if (!oldSubTaskList.equals(newSubTaskList)) {
                        taskObject.put("SubTasks", newSubTaskList);
                    } else {
                        break;
                    }
                } else {
                    taskObject.put("SubTasks", "");
                }
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(taskArray.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSubTasks(Task currentTask) {
        if (currentTask.getSubTaskList().length > 0) {
            for (Task subTask : currentTask.getSubTaskList()) {
                subTaskListModel.addElement(subTask);
            }
        }
    }
}