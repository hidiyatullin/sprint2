package Manager;

import Http.HttpTaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTasksManager();
//        return new HttpTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
