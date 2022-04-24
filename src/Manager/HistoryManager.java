package Manager;

import java.util.List;
import Model.Task;

public interface HistoryManager {
        List<Task> getHistory();

        void add(Task task);

        void remove(Task task);
}
