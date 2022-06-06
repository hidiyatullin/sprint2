package Manager;

import Model.Epic;
import Model.Subtask;
import Model.Task;
import Status.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends ManagerTest{

    @BeforeEach
    void initInFileBackedTasksManager() {
        manager = new FileBackedTasksManager();
        init();
    }

    @Test
    void saveAndLoadTasks() {
        Task task2 = new Task("Задача2", "тест", 1, Status.NEW, LocalDateTime.now(), 15);
        manager.newTask(task2);
        TaskManager manager1 = new FileBackedTasksManager(new File("task.csv"), true);
        assertEquals(manager.getTasks().size(), manager1.getTasks().size());
        Task task3 = new Task("Задача3", "тест", 1, Status.NEW, LocalDateTime.now(), 15);
        manager.newTask(task3);
        manager1 = new FileBackedTasksManager(new File("task.csv"), true);
        assertEquals(3, manager1.getTasks().size());
    }

    @Test
    void saveAndLoadEmptyTasks() {
        assertEquals(1, manager.getTasks().size());
        manager.deleteTasks();
        assertEquals(0, manager.getTasks().size());
        TaskManager manager1 = new FileBackedTasksManager(new File("task.csv"), true);
        assertEquals(0, manager1.getTasks().size());
    }

    @Test
    void saveAndLoadEmptyHistory() {
        assertEquals(0, manager.getHistory().size());
        assertEquals(1, manager.getTasks().size());
        TaskManager manager1 = new FileBackedTasksManager(new File("task.csv"), true);
        assertEquals(0, manager.getHistory().size());
        assertEquals(1, manager1.getTasks().size());
    }
}