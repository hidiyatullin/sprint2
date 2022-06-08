package Manager;

import Model.Epic;
import Model.Subtask;
import Model.Task;
import Status.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class ManagerTest {
    protected TaskManager manager = new InMemoryTaskManager();
    Task task;
    ArrayList<Subtask> subtasks;
    Epic epic;


    void init() {
        task = new Task("Задача1", "тест", 1, Status.NEW, LocalDateTime.now(), 15);
        manager.newTask(task);
        subtasks = new ArrayList<>();
        epic = new Epic("Эпик1", "тест", 0, Status.NEW, subtasks);
        manager.newEpic(epic);
    }

    @Test
    void getTasks() {
        final List<Task> tasks = manager.getTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size(), "Одна задача");
        assertEquals(task, tasks.get(0));
    }

    @Test
    void deleteTasks() {
        manager.deleteTasks();
        assertEquals(0, manager.getTasks().size());
    }

    @Test
    void getTask() {
        assertEquals(task, manager.getTask(1));
    }

    @Test
    void newTask() {
        Task task2 = new Task("Задача2", "тест", 1, Status.NEW, LocalDateTime.now(), 15);
        manager.newTask(task2);
        assertEquals(task2, manager.getTask(3));
    }

    @Test
    void updateTask() {
        Task task2 = new Task("Задача2", "тест", 1, Status.NEW, LocalDateTime.now(), 15);
        manager.updateTask(task2);
        assertEquals("Задача2", manager.getTask(1).getName());
    }

    @Test
    void deleteTask() {
        Task task2 = new Task("Задача2", "тест", 1, Status.NEW, LocalDateTime.now(), 15);
        manager.newTask(task2);
        manager.deleteTask(1);
        assertEquals(1, manager.getTasks().size());
    }

    @Test
    void getSubtasksFormEpic() {
        Subtask test4 = new Subtask("Подзадача1", "тест", 0, Status.NEW, LocalDateTime.now(), 15, 2);
        manager.newSubtask(test4);
        assertEquals(1, manager.getSubtasksFormEpic(epic).size());
    }

    @Test
    void deleteSubtasks() {
        Subtask test4 = new Subtask("Подзадача1", "тест", 0, Status.NEW, LocalDateTime.now(), 15, 2);
        manager.newSubtask(test4);
        manager.deleteSubtasks();
        assertEquals(0, manager.getSubtasks().size());
    }

    @Test
    void getSubtask() {
        Subtask test4 = new Subtask("Подзадача1", "тест", 0, Status.NEW, LocalDateTime.now(), 15, 2);
        manager.newSubtask(test4);
        assertEquals("Подзадача1", manager.getSubtask(3).getName());
    }

    @Test
    void newSubtask() {
        Subtask test4 = new Subtask("Подзадача1", "тест", 0, Status.NEW, LocalDateTime.now(), 15, 2);
        manager.newSubtask(test4);
        assertEquals("Подзадача1", manager.getSubtask(3).getName());
    }

    @Test
    void newSubtaskFalse() {
        Subtask test4 = new Subtask("Подзадача1", "тест", 0, Status.NEW, LocalDateTime.now(), 15, 5);
        manager.newSubtask(test4);
        assertEquals(0, manager.getSubtasks().size());
    }

    @Test
    void updateSubtask() {
        Subtask test4 = new Subtask("Подзадача1", "тест", 0, Status.NEW, LocalDateTime.now(), 15, 2);
        manager.newSubtask(test4);
        Subtask test5 = new Subtask("Подзадача2", "тест", 3, Status.NEW, LocalDateTime.now(), 15, 2);
        manager.updateSubtask(test5);
        assertEquals("Подзадача2", manager.getSubtask(3).getName());
    }

    @Test
    void deleteSubtask() {
        Subtask test4 = new Subtask("Подзадача1", "тест", 0, Status.NEW, LocalDateTime.now(), 15, 2);
        manager.newSubtask(test4);
        Subtask test5 = new Subtask("Подзадача2", "тест", 3, Status.NEW, LocalDateTime.now(), 15, 2);
        manager.newSubtask(test5);
        manager.deleteSubtask(3);
        assertEquals(1, manager.getSubtasks().size());
    }

    @Test
    void getEpics() {
        assertEquals(1, manager.getEpics().size());
        Epic epic2 = new Epic("Эпик2", "тест", 0, Status.NEW, subtasks);
        manager.newEpic(epic2);
        assertEquals(2, manager.getEpics().size());
    }

    @Test
    void deleteEpics() {
        Epic epic2 = new Epic("Эпик2", "тест", 0, Status.NEW, subtasks);
        manager.newEpic(epic2);
        manager.deleteEpics();
        assertEquals(0, manager.getEpics().size());
    }

    @Test
    void getEpic() {
        assertEquals(2, manager.getEpic(2).getId());
    }

    @Test
    void newEpic() {
        assertEquals(1, manager.getEpics().size());
        Epic epic2 = new Epic("Эпик2", "тест", 0, Status.NEW, subtasks);
        manager.newEpic(epic2);
        assertEquals(2, manager.getEpics().size());
    }

    @Test
    void updateEpic() {
        assertEquals("Эпик1", manager.getEpic(2).getName());
        Epic epic2 = new Epic("Эпик2", "тест", 2, Status.NEW, subtasks);
        manager.updateEpic(epic2);
        assertEquals(1, manager.getEpics().size());
        assertEquals("Эпик2", manager.getEpic(2).getName());
    }

    @Test
    void deleteEpic() {
        assertEquals(1, manager.getEpics().size());
        Epic epic2 = new Epic("Эпик2", "тест", 0, Status.NEW, subtasks);
        manager.newEpic(epic2);
        Subtask test4 = new Subtask("Подзадача1", "тест", 0, Status.NEW, LocalDateTime.now(), 15, 3);
        manager.newSubtask(test4);
        assertEquals(2, manager.getEpics().size());
        manager.deleteEpic(3);
        assertEquals(1, manager.getEpics().size());
    }


    @Test
    void generatorId() {
        Task task2 = new Task("Задача2", "тест", 1, Status.NEW, LocalDateTime.now(), 15);
        manager.newTask(task2);
        int taskIdFromTask2 = (manager.getTask(task2.getId())).getId();
        Task task3 = new Task("Задача3", "тест", 1, Status.NEW, LocalDateTime.now(), 15);
        manager.newTask(task3);
        int taskIdFromTask3 = (manager.getTask(task3.getId())).getId();
        assertEquals(taskIdFromTask2 + 1, taskIdFromTask3);
    }

    @Test
    void getHistory() {
        manager.getTask(1);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    void epicWithEmptySubtasks() {
    assertEquals(0, manager.getEpic(2).getSubtask().size());
    assertEquals(Status.NEW, manager.getEpic(2).getStatus());
    }

    @Test
    void epicWithNewSubtasks() {
        Subtask test4 = new Subtask("Подзадача1", "тест", 0, Status.NEW, LocalDateTime.now(), 15, 2);
        manager.newSubtask(test4);
        assertEquals(Status.NEW, manager.getEpic(2).getStatus());
    }

    @Test
    void epicWithInProgressSubtasks() {
        Subtask test4 = new Subtask("Подзадача1", "тест", 0, Status.IN_PROGRESS, LocalDateTime.now(), 15, 2);
        manager.newSubtask(test4);
        assertEquals(Status.IN_PROGRESS, manager.getEpic(2).getStatus());
    }

    @Test
    void epicWithDoneAndNewSubtasks() {
        Subtask test4 = new Subtask("Подзадача1", "тест", 0, Status.NEW, LocalDateTime.now(), 15, 2);
        manager.newSubtask(test4);
        Subtask test5 = new Subtask("Подзадача2", "тест", 0, Status.DONE, LocalDateTime.now(), 15, 2);
        manager.newSubtask(test5);
        assertEquals(Status.IN_PROGRESS, manager.getEpic(2).getStatus());
    }

    @Test
    void epicWithDoneSubtasks() {
        Subtask test4 = new Subtask("Подзадача1", "тест", 0, Status.DONE, LocalDateTime.now(), 15, 2);
        manager.newSubtask(test4);
        assertEquals(Status.DONE, manager.getEpic(2).getStatus());
    }
}