package src;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;

public class mainPage extends JFrame{
    private DefaultListModel<Category> model = new DefaultListModel<>();
    private JList<Category> categoryList = new JList<Category>();
    
    private mainPage() {
        setTitle("Main Page");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        InitializeUI(this);
        loadCategories(categoryList);
    }

    private void InitializeUI(JFrame frame){
        // Create a panel to store the buttons
        JPanel panel = new JPanel();
        // Create a button to view the tasks and add it to the panel
        JButton viewTaskButton = new JButton("View Tasks");
        panel.add(viewTaskButton);
        // Create a button to access an empty task manager and add it to the panel
        JButton speedTaskManagerButton = new JButton("Speedy Manager");
        panel.add(speedTaskManagerButton);
        // Set the layout of the panel to FlowLayout
        panel.setLayout(new FlowLayout());
        // Create a new panel to store the list of categories and a scroll pane
        JPanel panel2 = new JPanel();
        // Create labels to describe the purpose of the list and add them to the panel2
        JLabel descriptionLabel = new JLabel("Choose a Category to view tasks or create a new one", SwingConstants.CENTER);
        JLabel descriptionLabel2 = new JLabel("You can also acces the Speedy Manager to create tasks", SwingConstants.CENTER);
        JLabel descriptionLabel3 = new JLabel("Tasks created with Speedy Manager will be saved in the category you choose", SwingConstants.CENTER);
        panel2.add(descriptionLabel, BorderLayout.NORTH);
        panel2.add(descriptionLabel2, BorderLayout.NORTH);
        panel2.add(descriptionLabel3, BorderLayout.NORTH);
        // Take the list of categories and add it to the panel2
        categoryList.setModel(model);
        categoryList.setCellRenderer(new CategoryListCellRenderer());
        JScrollPane scrollPane = new JScrollPane(categoryList);
        panel2.add(scrollPane);
        // Set the layout of the panel to BorderLayout
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));

        /*
         * Add an action listener to the viewTaskButton to open the TaskManager
         * of the selected category when the button is clicked
         */
        viewTaskButton.addActionListener(e -> {
            TaskManager taskManager = new TaskManager("Task Manager", getselectedcategory(), getselectedcategoryFilePath(), false);
            taskManager.setVisible(true);
            // Close the main page without exit the program
            frame.dispose();
        });

        speedTaskManagerButton.addActionListener(e -> {
            TaskManager taskManager = new TaskManager("Speedy Manager", "", "", true);
            taskManager.setVisible(true);
            // Close the main page without exit the program
            frame.dispose();
        });

        // Add panels to the frame
        frame.add(panel, BorderLayout.SOUTH);
        frame.add(panel2, BorderLayout.CENTER);
    }

    /**
     * Get the selected category from the list of categories
     * @return the selected category to be used in the TaskManager
     */
    private String getselectedcategoryFilePath() {
        int index = categoryList.getSelectedIndex();
        try {
            return model.getElementAt(index).getFilepath();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the selected category from the list of categories
     * @return the selected category to be used in the TaskManager
     */
    private String getselectedcategory() {
        int index = categoryList.getSelectedIndex();
        try {
            return model.getElementAt(index).getName();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Load the categories from the file and add them to the list of categories
     * @param categoryList the list in which the categories are to be added
     */
    private void loadCategories(JList<Category> categoryList) {
        File file = new File("static/taskfiles.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] stringArray = line.split("/");
                    String[] stringArray2 = stringArray[1].split("\\.");
                    String categoryName = stringArray2[0];
                    Category category = new Category(categoryName, line);
                    model.addElement(category);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        mainPage mainPage = new mainPage();
        mainPage.setVisible(true);
    }
}

class Category{
    private String name;
    private String filepath;

    public Category(String name, String filepath) {
        this.name = name;
        this.filepath = filepath;
    }

    public String getName() {
        return name;
    }

    public String getFilepath() {
        return filepath;
    }
}

/**
 * A custom cell renderer for the list of categories
 */
class CategoryListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        Category category = (Category) value;
        setText(category.getName());
        setHorizontalAlignment(SwingConstants.CENTER);
        return this;
    }
}