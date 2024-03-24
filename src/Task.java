package src;

public class Task {
    private String title;
    private Priority priority;
    private String description;
    private Task[] subTaskList;
    private String dueDate;
    private String category;
    private String filepath;
    private boolean removed = false;

    public Task(String title, Priority priority, String description, Task[] subTaskList, String dueDate, String category) {
        // If the task is a subtask (it doesn't have a description, subtask list, due date, and category)
        if (title != null && priority != null && description == null && subTaskList == null && dueDate == null && category == null) {
            this.title = title;
            this.priority = priority;
        } else {
            this.title = title;
            this.priority = priority;
            this.description = description;
            if (subTaskList != null) {
                this.subTaskList = subTaskList;
            } else {
                this.subTaskList = new Task[0];
            }
            this.dueDate = dueDate;
            this.category = category;
            this.filepath = "static/" + category + ".json";
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Task[] getSubTaskList() {
        return subTaskList;
    }

    public String getSubTaskListToString() {
        StringBuilder subTaskListString = new StringBuilder();
        for (int i = 0; i < subTaskList.length; i++) {
            subTaskListString.append(subTaskList[i].getTitle() + " || " + subTaskList[i].getPriority());
            if (i < subTaskList.length - 1) {
                subTaskListString.append(", ");
            }
        }
        return subTaskListString.toString();
    }

    public static Task[] getSubTaskListFromString(String subTaskListString) {
        if (subTaskListString.equals("[]") || subTaskListString.equals("")) {
            return new Task[0];
        } else {
            String[] subTaskListArray = subTaskListString.split(", ");
            Task[] subTaskList = new Task[subTaskListArray.length];
            for (int i = 0; i < subTaskListArray.length; i++) {
                String[] subTaskArray = subTaskListArray[i].split(" \\|\\| ");
                String title = subTaskArray[0].trim();
                Priority priority = Priority.valueOf(subTaskArray[1].trim());
                subTaskList[i] = new Task(title, priority, "", null, null, null);
            }
            return subTaskList;
        }
    }

    public void appendToSubTaskList(Task subTask) {
        Task[] newSubTaskList = new Task[subTaskList.length + 1];
        System.arraycopy(subTaskList, 0, newSubTaskList, 0, subTaskList.length);
        newSubTaskList[subTaskList.length] = subTask;
        subTaskList = newSubTaskList;
    }

    public void removeFromSubTaskList(int index) {
        Task[] newSubTaskList = new Task[subTaskList.length - 1];
        for (int i = 0, j = 0; i < subTaskList.length; i++) {
            if (i != index) {
                newSubTaskList[j] = subTaskList[i];
                j++;
            }
        }
        subTaskList = newSubTaskList;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFilepath() {
        return filepath;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved() {
        this.removed = true;
    }

    enum Priority {
        LOW, MEDIUM, HIGH
    }
}