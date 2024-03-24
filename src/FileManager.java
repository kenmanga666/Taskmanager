package src;

import java.util.*;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import java.io.*;
import org.json.*;

public class FileManager {
    
    /**
     * Loads tasks from a file.
     * @param filePath The file path from which the tasks will be loaded.
     */
    public static void loadTasksFromFile(String filePath, DefaultListModel<Task> taskListModel) {
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
            file.write("[\n");
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                if (task.isRemoved()) { 
                    continue;
                }
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
     * Group tasks based on category to save them to corresponding files
     * @param ancientListModel The list of tasks before the last modification
     *                        (used to check if all tasks have been removed)
     * If null, it means that the task manager is closing or a task has been added
     */
    public static void saveTasksByCategory(DefaultListModel<Task> taskListModel, DefaultListModel<Task> ancientListModel, boolean isSpeedyManager, String selectedcategory, boolean isClosing) {
        Map<String, List<Task>> tasksByCategory = new HashMap<String, List<Task>>();

        // Check if the task manager is closing or if a task has been added
        if (ancientListModel != null) {
            // Check if all tasks have been removed
            if (taskListModel.size() == 0 && ancientListModel.size() != 0) {
                for (int i = 0; i < ancientListModel.size(); i++) {
                    Task task = ancientListModel.getElementAt(i);
                    String category = task.getCategory();
                    if (!tasksByCategory.containsKey(category)) {
                        tasksByCategory.put(category, new ArrayList<>());
                    }
                    tasksByCategory.get(category).add(task);
                }
            }
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
            File correspondingFile = new File(filePath);
            saveTasksToFile(tasks, filePath);
            if (isFileEmpty(correspondingFile) && isClosing) {
                correspondingFile.delete();
                if (correspondingFile.exists()) {
                    JOptionPane.showMessageDialog(null, "The file " + filePath + " is empty, so it has been deleted", "File Deleted", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    /**
     * Add the file path to the taskfiles.txt file
     * @param filePath The file path to be added
     */
    public static void addFilePath(String filePath) {
        try {
            FileWriter fileWriter = new FileWriter("static/categorys.txt", true);
            FileReader fileReader = new FileReader("static/categorys.txt");
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
    public static void removeFilePath(String filePath) {
        try {
            FileReader fileReader = new FileReader("static/categorys.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder newTaskFiles = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.equals(filePath)) {
                    newTaskFiles.append(line + "\n");
                }
            }
            FileWriter fileWriter = new FileWriter("static/categorys.txt");
            fileWriter.write(newTaskFiles.toString());
            fileWriter.close();
            fileReader.close();
            JOptionPane.showMessageDialog(null, "the selected category as been removed from the list of category", "Category Deleted", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the file is empty or not
     * @param file
     * @return boolean
     */
    private static boolean isFileEmpty(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonContent.append(line);
            }
            bufferedReader.close();
            if (jsonContent.toString().equals("[]") || jsonContent.toString().equals("")) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}