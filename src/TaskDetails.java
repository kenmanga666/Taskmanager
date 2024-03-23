package src;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.io.IOException;
import javax.swing.*;
import org.json.*;

public class TaskDetails extends JFrame {
    private JTextField titleField;
    private JComboBox<Task.Priority> priorityComboBox;
    private JTextArea descriptionArea;
    private JTextField categoryField;
    private String oldTitle;
    
    public TaskDetails(Task task, String filepath) {
        setTitle("Task Details");
        setSize(500, 300);
        setLocationRelativeTo(null);
        InitializeUI(this, task);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!task.getTitle().equals(titleField.getText())) {
                    oldTitle = task.getTitle();
                    task.setTitle(titleField.getText());
                }
                if (!task.getPriority().equals(priorityComboBox.getSelectedItem())) {
                    task.setPriority((Task.Priority) priorityComboBox.getSelectedItem());
                }
                if (!task.getCategory().equals(categoryField.getText())) {
                    task.setCategory(categoryField.getText());
                }
                task.setDescription(descriptionArea.getText());
                saveDetailsToFile(task.getDescription(), task.getTitle(), task.getPriority(), task.getCategory(), filepath);
            }
        });
    }

    private void InitializeUI(JFrame frame, Task task) {
        // Create a panel to store the Informations
        JPanel panel = new JPanel();
        
        JLabel TaskTitleLabel = new JLabel("Task Title: ");
        titleField = new JTextField(task.getTitle(), 20);
        JLabel priorityLabel = new JLabel("Priority: ");
        priorityComboBox = new JComboBox<Task.Priority>(Task.Priority.values());
        priorityComboBox.setSelectedItem(task.getPriority());
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionArea = new JTextArea(task.getDescription(), 5, 20);
        JLabel categoryLabel = new JLabel("Category: ");
        categoryField = new JTextField(task.getCategory(), 20);

        panel.add(TaskTitleLabel);
        panel.add(titleField);
        panel.add(priorityLabel);
        panel.add(priorityComboBox);
        panel.add(descriptionLabel);
        panel.add(new JScrollPane(descriptionArea));
        panel.add(categoryLabel);
        panel.add(categoryField);
        // set panel layout to FlowLayout
        panel.setLayout(new GridLayout(8,1));
        
        // Add the panel to the frame
        frame.add(panel, BorderLayout.CENTER);
    }

    private void saveDetailsToFile(String description, String newTitle, Task.Priority newPriority, String newCategory, String filepath) {
        // Sauvegarder la description de la tâche correspondante dans le fichier task.json
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
            JSONArray jsonArray = new JSONArray(jsonContent.toString());
            boolean found = false;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("Title");
                String priority = jsonObject.getString("Priority");
                String category = jsonObject.getString("Category");
                if (title.equals(newTitle)) {
                    found = true;
                    jsonObject.put("Description", description);
                    if (!priority.equals(newPriority.toString())) {
                        jsonObject.put("Priority", newPriority);
                    }
                    if (!category.equals(newCategory)) {
                        jsonObject.put("Category", newCategory);
                    }
                    break;
                }
            }
    
            if (!found) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString("Title");
                    String priority = jsonObject.getString("Priority");
                    String category = jsonObject.getString("Category");
                    if (title.equals(oldTitle)) {
                        found = true;
                        jsonObject.put("Title", newTitle);
                        jsonObject.put("Description", description);
                        if (!priority.equals(newPriority.toString())) {
                            jsonObject.put("Priority", newPriority);
                        }
                        if (!category.equals(newCategory)) {
                            jsonObject.put("Category", newCategory);
                        }
                        break;
                    }
                }
            }
    
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonArray.toString(2));
            fileWriter.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}