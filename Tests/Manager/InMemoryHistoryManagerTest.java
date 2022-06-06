package Manager;

import Model.Task;
import Status.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest{
    protected HistoryManager history = Managers.getDefaultHistory();
    Task task;

    @BeforeEach
    void initInMemoryHistoryManager() {
        task = new Task("Задача1", "тест", 1, Status.NEW, LocalDateTime.now(), 15);
    }

    @Test
    void getEmptyHistory() {
        assertEquals(0, history.getHistory().size());
    }

    @Test
    void add() {
        assertEquals(0, history.getHistory().size());
        history.add(task);
        assertEquals(1, history.getHistory().size());
    }

    @Test
    void remove() {
        assertEquals(0, history.getHistory().size());
        history.add(task);
        assertEquals(1, history.getHistory().size());
        history.remove(1);
        assertEquals(0, history.getHistory().size());
    }

    @Test
    void addDouble() {
        assertEquals(0, history.getHistory().size());
        history.add(task);
        history.add(task);
        assertEquals(1, history.getHistory().size());
    }

    @Test
    void removeFromBegin() {
        history.add(task);
        Task task2 = new Task("Задача2", "тест", 2, Status.NEW, LocalDateTime.now(), 15);
        history.add(task2);
        Task task3 = new Task("Задача3", "тест", 3, Status.NEW, LocalDateTime.now(), 15);
        history.add(task3);
        history.remove(1);
        assertEquals("Задача2", history.getHistory().get(0).getName());
        assertEquals("Задача3", history.getHistory().get(1).getName());
    }

    @Test
    void removeFromEnd() {
        history.add(task);
        Task task2 = new Task("Задача2", "тест", 2, Status.NEW, LocalDateTime.now(), 15);
        history.add(task2);
        Task task3 = new Task("Задача3", "тест", 3, Status.NEW, LocalDateTime.now(), 15);
        history.add(task3);
        history.remove(3);
        assertEquals("Задача1", history.getHistory().get(0).getName());
        assertEquals("Задача2", history.getHistory().get(1).getName());
    }

    @Test
    void removeFromMiddle() {
        history.add(task);
        Task task2 = new Task("Задача2", "тест", 2, Status.NEW, LocalDateTime.now(), 15);
        history.add(task2);
        Task task3 = new Task("Задача3", "тест", 3, Status.NEW, LocalDateTime.now(), 15);
        history.add(task3);
        history.remove(2);
        assertEquals("Задача1", history.getHistory().get(0).getName());
        assertEquals("Задача3", history.getHistory().get(1).getName());
    }
}