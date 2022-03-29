import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    HashMap<Integer, Task> Tasks = new HashMap<>();
    HashMap<Integer, Subtask> Subtasks = new HashMap<>();
    HashMap<Integer, Epic> Epics = new HashMap<>();
    private int generatorId = 0; // переменная для создания идентификатора

    /*
     * Возвращает список всех задач
     */
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(Tasks.values());
    }

    /*
     * Удаляет все задачи
     */
    public void deleteTasks() {
        Tasks.clear();
    }

    /*
     * Возвращает задачу по идентификатору
     */
    public Task getTask(int id) {
        return Tasks.get(id);
    }

    /*
     * Создаёт новую задачу
     */
    public Task newTask(Task task) {
        task.setId(++generatorId);
        Tasks.put(task.getId(), task);
        return task;
    }

    /*
     * Обновляет задачу
     */
    public void updateTask(Task task) {
        if (!Tasks.containsKey(task.getId())) {
            return;
        }
        Tasks.put(task.getId(), task);
    }

    /*
     * Удаляет задачу по её идентификатору
     */
    public void deleteTask(int id) {
        if (!Tasks.containsKey(id)) {
            return;
        }
        Tasks.remove(id);
    }

    /*
     * Возвращает список всех подзадач
     */
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(Subtasks.values());
    }

    /*
     * Удаляет все подзадачи
     */
    public void deleteSubtasks() {
        Subtasks.clear();
    }

    /*
     * Возвращает подзадачу по идентификатору
     */
    public Subtask getSubtask(int id) {
        return Subtasks.get(id);
    }

    /*
     * Добавляет подзадачу
     */
    public Subtask newSubtask(Subtask subtask) {
        subtask.setId(++generatorId);
        Subtasks.put(subtask.getId(), subtask);
        statusOfEpic(subtask); // пересчитывает статус эпика
        return subtask;
    }

    /*
     * Обновляет подзадачу
     */
    public void updateSubtask(Subtask subtask) {
        if (!Subtasks.containsKey(subtask.getId())) {
            return;
        }
        Subtasks.put(subtask.getId(), subtask);
        statusOfEpic(subtask); // пересчитывает статус эпика
    }

    /*
     * Удаляет подзадачу по идентификатору
     */
    public void deleteSubtask(int id) {
        if (!Subtasks.containsKey(id)) {
            return;
        }
        Subtasks.remove(id);
    }

    /*
     * Возвращает список всех эпиков
     */
    public ArrayList<Epic> getEpic() {
        return new ArrayList<>(Epics.values());
    }

    /*
     * Удаляет все эпики
     */
    public void deleteEpics() {
        Epics.clear();
    }

    /*
     * Возвращает эпик по идентификатору
     */
    public Epic getEpic(int id) {
        return Epics.get(id);
    }

    /*
     * Создаёт новый эпик
     */
    public Epic newEpic(Epic epic) {
        Epic epic1 = new Epic(epic.getName(), epic.getDescription(), ++generatorId, Status.NEW, epic.getSubtask());
        Epics.put(epic1.getId(), epic1);
        return epic1;
    }

    /*
     * Обновляет эпик
     */
    public void updateEpic(Epic epic) {
        if (!Epics.containsKey(epic.getId())) {
            return;
        }
        Epic epic1 = new Epic(epic.getName(), epic.getDescription(), epic.getId(), Status.NEW, epic.getSubtask());
        Epics.put(epic1.getId(), epic1);
    }

    /*
     * Удаляет эпик по идентификатору
     */
    public void deleteEpic(int id) {
        if (!Epics.containsKey(id)) {
            return;
        }
        Epics.remove(id);
    }

    /*
     * Возвращает подзадачи конкретного эпика
     */
    public ArrayList<Subtask> getSubtasks(Epic epic) {
        return epic.getSubtask();
    }

    /*
     * Рассчитывает статус эпика
     */
    private void statusOfEpic(Subtask subtask) {
        int countN = 0; // переменная для подсчёта статусов NEW
        int countD = 0; // переменная для подсчёта статусов DONE
        Epic epic1 = Epics.get(subtask.getEpicId()); // нашли эпик, который содержит полученную подзадачу
        for (Subtask check : (epic1.getSubtask())) { // прошлись по всем подзадачам и получили их статусы
            if (check.getStatus().equals(Status.NEW)) { // проверили статус NEW
                countN++; // если да, то считаем кол-во таких статусов
            } else if (check.getStatus().equals(Status.done)) { // проверили статус DONE
                countD++; // если да, то считаем кол-во таких статусов
            }
        }
        if (countN == (epic1.getSubtask()).size()) { // сравниваем кол-во статусов NEW с кол-вом подзадач
            epic1.setStatus(Status.NEW); // если они совпадают, то присвааиваем эпику статус NEW
        } else if (countD == (epic1.getSubtask()).size()) { // сравниваем кол-во статусов DONE с кол-вом подзадач
            epic1.setStatus(Status.done); // если они совпадают, то присвааиваем эпику статус DONE
        } else {
            epic1.setStatus(Status.inProgress); // если ни то ни другое, то присваиваем статус IN_PROGRESS
        }
    }
}
