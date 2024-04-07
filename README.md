# TaskManager

Welcome to my Task Manager, this little application was developed in Java and allows you to manage your tasks with a simple graphic user interface using Java Swing.

## How to Use

The Task Manager allows you to manage your tasks with a User Interface (UI) that is easy to use. You can add, remove, list, and save your tasks to files according to the category of the task. (further features will be added in the future).

## Instructions to Run the Application

1. Make sure you have Java installed on your system.
2. Clone the repository.
3. Compile the source files using the Makefile with the command: `make build` or `make`.
4. Run the application via the Makefile with the command: `make run`.

> [!WARNING]
> **The Makefile doesn't work for now (json import problems)**

- For now, you can run the application by running the `mainPage.java` file. (I'm working on fixing the Makefile)

## Start of the Application / Main Menu

At the start of the application, you will be prompted to the main page where you'll be able to choose between the following options:
- Add a Category of tasks.
- Remove a Category of tasks.

> [!NOTE]
> The categories are the different types of tasks you want to manage (e.g. Work, Personal, School, etc.)
> You can add as many categories as you want.
> When you remove a category, all the tasks in the category will be deleted (if there are any), and the category will be removed from the list of categories (you'll be prompted to confirm the deletion of the category).

- Open the Task Manager of the category of task you want to manage.
- Open the "Speedy Task Manager" (named "Speedy Manager)

> [!TIP]
> Go see the [Features inside the Task Manager](#features-inside-the-task-manager) section to see what you can do inside the Task Manager.
> Go see the [Features inside the Speedy Manager](#features-inside-the-speedy-manager) section to see what you can do inside the Speedy Manager.

- Exit the Task Manager.

## Features inside the Task Manager

The Task Manager will prompt you all the tasks you have in the category you chose. Inside it, you'll be able to:
- Add a task to the list. (to do so you'll have to enter the task's title inside the text field below the list of tasks and click on the "Add Task" Button, you'll be prompted to enter the task's Priority)
- Remove a selected task from the list by clicking the "Remove Task" Button (you'll be prompted to confirm the removal of the task) / if no task is selected, you'll be prompted to delete all the tasks.
- Move tasks to reorder them like you want by selecting a task and clicking on the 'up' ('ʌ') or 'down'('v') button.
- Click on "Open Task" Button to Open the selected task to see its details and edit them:
    - Edit the task's title.
    - Edit the task's priority.
    - Edit the task's description.
    - Edit the task's category.
- Click on "Manage Subtasks" Button to manage the subtasks of the selected task:
    - Add subtasks to a task.
    - Remove subtasks from a task.
    - Move subtasks to reorder them like you want.

> [!NOTE]
> The Task Manager will save the tasks in a json file named after the category you chose. The task are automatically saved when you add, remove or edit a task and also when you quit it.

## Features inside the Speedy Manager

The Speedy Manager is a faster version of the Task Manager where you can add tasks without having to enter the task's category file.

> [!IMPORTANT]
> It will not prompt you the tasks you have in the different categories you have

Inside the Speedy Manager, you'll be able to:
- Add a task. (you'll be prompted to enter the task's priority but also the task's category)
- Remove tasks
- Click on "Open Task" Button to Open the selected task to see its details and edit them (if no task is selected, you'll be prompted to select a task first):
    - Edit the task's title.
    - Edit the task's priority.
    - Edit the task's description.
    - Edit the task's category.
- Click on "Manage Subtasks" Button to manage the subtasks of the selected task (if no task is selected, you'll be prompted to select a task first):
    - Add subtasks to a task.
    - Remove subtasks from a task.

> [!NOTE]
> The Speedy Manager will save the tasks in a json file named after the category you chose for each task. The task are automatically saved when you add, remove or edit a task and also when you quit it.
> Remember that with the Speedy Manager you'll be able to access the tasks you added only by using the Speedy Manager in the current session.

### Note to the User for empty categories

> [!CAUTION]
> If there are no tasks in a category after saving the tasks, the category file will be deleted automatically to avoid having empty files.
> It will be prompted to you that the file as been deleted.
> Meanwhile, the category will still be displayed in the Main Menu and you'll be able to add tasks to it.

## Structure of the Code

The source code is organized in several classes:
- `MainPage` : The main class containing the method `main` and the Main Menu of the application that allows you to choose between the Task Manager and the Speedy Manager.
- `TaskManager` : The class that contains the logic of the Task Manager and the Speedy Manager.
- `SubTaskManager` : The class that contains the logic of the SubTask Manager and the management of the subTasks.
- `Task` : Representation of a task with methods for the creation and management of the task and subTasks.
- `TaskDetails` : The class that contains the logic behind the 'Open Task' button and the management of the task's details (title, priority, description, category).
- `TaskListCellRenderer` : The class that contains the logic behind the rendering of the tasks in the list (to display the task's title centered in the list and color according to the priority of the task).
- `FileManager` : The class that contains the logic behind the saving and loading of the tasks in the json files.

## Authors
[Vallée Kenny](https://github.com/kenmanga666)
---

Enjoy managing your tasks with the Task Manager! If you have any questions or suggestions, feel free to share them.