package Manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends ManagerTest{

        @BeforeEach
        void initInMemoryTaskManager() {
            manager = new InMemoryTaskManager();
            init();
        }

        @Test
        void InMemoryTaskManager() {
            manager = new InMemoryTaskManager();
            assertEquals(0, manager.getTasks().size(), "Задач нет");
            assertEquals(0, manager.getSubtasks().size(), "Подзадач нет");
            assertEquals(0, manager.getEpics().size(), "Эпиков нет");
            assertEquals(0, manager.getHistory().size(), "Истории нет");

        }

}