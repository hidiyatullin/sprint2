package Manager;

import Model.Epic;
import Model.Subtask;
import Model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    /*
     * Возвращает список всех задач
     */
    List<Task> getTasks();

    /*
     * Удаляет все задачи
     */
    void deleteTasks();

    /*
     * Возвращает задачу по идентификатору
     */
    Task getTask(int id);

    /*
     * Создаёт новую задачу
     */
    Task newTask(Task task);

    /*
     * Обновляет задачу
     */
    void updateTask(Task task);

    /*
     * Удаляет задачу по её идентификатору
     */
    void deleteTask(int id);

    /*
     * Возвращает список всех подзадач
     */
    List<Subtask> getSubtasks();

    /*
     * Удаляет все подзадачи
     */
    void deleteSubtasks();

    /*
     * Возвращает подзадачу по идентификатору
     */
    Subtask getSubtask(int id);

    /*
     * Добавляет подзадачу
     */
    Subtask newSubtask(Subtask subtask);

    /*
     * Обновляет подзадачу
     */
    void updateSubtask(Subtask subtask);

    /*
     * Удаляет подзадачу по идентификатору
     */
    void deleteSubtask(int id);

    /*
     * Возвращает список всех эпиков
     */
    List<Epic> getEpics();

    /*
     * Удаляет все эпики
     */
    void deleteEpics();

    /*
     * Возвращает подзадачи конкретного эпика
     */
    ArrayList<Subtask> getSubtasksFormEpic(Epic epic);

    /*
     * Возвращает эпик по идентификатору
     */
    Epic getEpic(int id);

    /*
     * Создаёт новый эпик
     */
    Epic newEpic(Epic epic);

    /*
     * Обновляет эпик
     */
    void updateEpic(Epic epic);

    /*
     * Удаляет эпик по идентификатору
     */
    void deleteEpic(int epicId);

    /*
     * Генерирует id
     */
    int generatorId();

    /*
     * Возвращает список истории
     */
    List<Task> getHistory();

    TreeSet<Task> getPrioritizedTasks();
}
