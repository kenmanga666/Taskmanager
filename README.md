# TaskManager

Welcome to my Task Manager, this little application was developed in Java and allows you to manage your tasks with a simple command line interface.

## How to Use

The Task Manager allows you to manage your tasks with a User Interface (UI) that is easy to use. You can add, remove, list, and save your tasks to a file. (further features will be added in the future).

At the start of the application, you will be prompted to the main
page where you'll be able to choose between the following options:
- Open the Task Manager of the gategory of task you want to manage.
- Open the "Speedy Task Manager" (named "Speedy Manager)

| :exclamation: Aware                       |
|:------------------------------------------|
|  Go see the "Features inside the Task Manager" section to see what you can do inside the Task Manager. |

- Exit the Task Manager.

## Instructions to Run the Application

1. Make sure you have Java installed on your system.
2. Clone the repository or download the source files.
3. Compile the source files using the Makefile with the command: `make build` or `make`.
4. Run the application via the Makefile with the command: `make run`.

> [!WARNING]
>The Makefile doesn't work for now (json problems)

- For now, you can run the application by running the `mainPage.java` file. (I'm working on fixing the Makefile)

## Features inside the Task Manager
The Task Manager will prompt you all the task you have in the category you chose. Inside it, you'll be able to:
- Add a task to the list. (to do so you'll have to enter the task's title inside the text field below the list of tasks and click on the "Add Task" Button, you'll be prompted to enter the task's Priority)
- Remove a selected task from the list by clicking on the "Remove Task" Button (you'll be prompted to confirm the removal of the task) / if no task is selected, you'll be prompted to delete all the tasks.
- Click on "Open Task" Button to Open the selected task to see its details and edit them:
    - Edit the task's title.
    - Edit the task's priority.
    - Edit the task's description.
    - Edit the task's category.
- Click on "Manage Subtasks" Button to manage the subtasks of the selected task:
    - Add subtasks to a task.
    - Remove subtasks from a task.

| :memo:        | Take note of this       |
|---------------|:------------------------|
| The Task Manager will save the tasks in a json file named after the category you chose. The task are automatically saved when you add or edit a task and also when you quit it. |

## Features inside the Speedy Manager
The Speedy Manager is a faster version of the Task Manager where you can add tasks without having to enter the task's category file.

> [!IMPORTANT]
> It'll not prompt you the tasks you have in the different categories you have |

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

| :memo:        | Take note of this       |
|---------------|:------------------------|
| |The Speedy Manager will save the tasks in a json file named after the category you chose for each task. The task are automatically saved when you add, or edit a task and also when you quit it. |
| |Remember that with the Speedy Manager you'll be able to access the tasks you added only by using the Speedy Manager in the current session |

## Structure of the Code