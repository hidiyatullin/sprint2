package Manager;

public class Managers {
    private static InMemoryHistoryManager inMemoryHistoryManager;

    static {
        inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return new FileBackedTasksManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
