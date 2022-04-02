import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Manager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private int id = 0; // переменная для создания идентификатора

    /*
     * Возвращает список всех задач
     */
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    /*
     * Удаляет все задачи
     */
    public void deleteTasks() {
        tasks.clear();
    }

    /*
     * Возвращает задачу по идентификатору
     */
    public Task getTask(int id) {
        return tasks.get(id);
    }

    /*
     * Создаёт новую задачу
     */
    public Task newTask(Task task) {
        task.setId(generatorId());
        tasks.put(task.getId(), task);
        return task;
    }

    /*
     * Обновляет задачу
     */
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return;
        }
        tasks.put(task.getId(), task);
    }

    /*
     * Удаляет задачу по её идентификатору
     */
    public void deleteTask(int id) {
        if (!tasks.containsKey(id)) {
            return;
        }
        tasks.remove(id);
    }

    /*
     * Возвращает список всех подзадач
     */
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    /*
     * Удаляет все подзадачи
     */
    public void deleteSubtasks() {
        subtasks.clear();
    }

    /*
     * Возвращает подзадачу по идентификатору
     */
    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    /*
     * Добавляет подзадачу
     */
    public Subtask newSubtask(Subtask subtask) {
        subtask.setId(generatorId());
        subtasks.put(subtask.getId(), subtask);
        if (epics.get(subtask.getEpicId()) == null) {
            System.out.println("Сначала требуется создать Эпик");
        } else {
            Epic epic = epics.get(subtask.getEpicId());
            ArrayList<Subtask> epicsSubtasks = epic.getSubtask(); // получает список подзадач эпика
            epicsSubtasks.add(subtask); // добавляет доп. подзадачу в список
            epic.setSubtask(epicsSubtasks); // сохраняет подзадачу в эпик
            statusOfEpic(subtask); // пересчитывает статус эпика
        }
        return subtask;
    }

    /*
     * Обновляет подзадачу
     */
    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            System.out.println("Нет подзадачи с id " + subtask.getId());
            return;
        }
        subtasks.put(subtask.getId(), subtask);
        updateSubtaskInEpic(subtask);
        statusOfEpic(subtask); // пересчитывает статус эпика
    }

    /*
     * Удаляет подзадачу по идентификатору
     */
    public void deleteSubtask(int id) {
        if (!subtasks.containsKey(id)) {
            return;
        }
        subtasks.remove(id);
    }

    /*
     * Возвращает список всех эпиков
     */
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    /*
     * Удаляет все эпики
     */
    public void deleteEpics() {
        epics.clear();
    }

    /*
     * Возвращает эпик по идентификатору
     */
    public Epic getEpic(int id) {
        return epics.get(id);
    }

    /*
     * Создаёт новый эпик
     */
    public Epic newEpic(Epic epic) {
        epic.setId(generatorId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    /*
     * Обновляет эпик
     */
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return;
        }
        Epic savedEpic = epics.get(epic.getId());
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }

    /*
     * Удаляет эпик по идентификатору
     */
    public void deleteEpic(int epicId) {
        if (!epics.containsKey(epicId)) {
            return;
        }
        ArrayList<Subtask> epicsSubtasks = (epics.get(epicId)).getSubtask(); // получает список подзадач эпика
        for (Subtask subtask : epicsSubtasks) {
            subtasks.remove(subtask.getId());
        }
        epics.remove(epicId);
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
        int countNew = 0; // переменная для подсчёта статусов NEW
        int countDone = 0; // переменная для подсчёта статусов DONE
        Epic epic = epics.get(subtask.getEpicId()); // нашли эпик, который содержит полученную подзадачу
        for (Subtask checkedSubtask : epic.getSubtask()) { // прошлись по всем подзадачам и получили их статусы
            if (checkedSubtask.getStatus().equals(Status.NEW)) { // проверили статус NEW
                countNew++; // если да, то считаем кол-во таких статусов
            } else if (checkedSubtask.getStatus().equals(Status.done)) { // проверили статус DONE
                countDone++; // если да, то считаем кол-во таких статусов
            }
        }
        if (countNew == (epic.getSubtask()).size()) { // сравниваем кол-во статусов NEW с кол-вом подзадач
            epic.setStatus(Status.NEW); // если они совпадают, то присвааиваем эпику статус NEW
        } else if (countDone == (epic.getSubtask()).size()) { // сравниваем кол-во статусов DONE с кол-вом подзадач
            epic.setStatus(Status.done); // если они совпадают, то присвааиваем эпику статус DONE
        } else {
            epic.setStatus(Status.inProgress); // если ни то ни другое, то присваиваем статус IN_PROGRESS
        }
    }

    /*
     * Генерирует id
     */
    public int generatorId() {
        return ++id;
    }

    /*
     * Обновляет Подзадачу внутри Эпика
     */
    private void updateSubtaskInEpic(Subtask subtask) {
        int numberOfSubtask;
        Epic epic = epics.get(subtask.getEpicId()); // находит эпик, который содержит полученную подзадачу
        for (int i = 0; i < (epic.getSubtask()).size(); i++) {
            if (((epic.getSubtask()).get(i)).getId() == subtask.getId()) { // находит порядковый номер Подзадачи
                numberOfSubtask = i;
                epic.getSubtask().remove(numberOfSubtask); // удаляет старую подазадачу
                } (epic.getSubtask()).add(subtask); // добавляет новую подзадачу
        }
    }
}
