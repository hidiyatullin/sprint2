package Manager;

import Model.*;
import Status.*;
import com.sun.source.tree.Tree;

import java.time.LocalDateTime;
import java.util.*;



public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HistoryManager history = Managers.getDefaultHistory();
    protected int id = 0; // переменная для создания идентификатора
    protected TreeSet<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        Comparator<Task> comparator = new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                if (o1.getStartTime().isBefore(o2.getStartTime())) {
                    return -1;
                } else if (o1.getStartTime().isAfter(o2.getStartTime())) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
        prioritizedTasks = new TreeSet<>(comparator);
    }

    /*
     * Возвращает список всех задач
     */
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    /*
     * Удаляет все задачи
     */
    @Override
    public void deleteTasks() {
        for (Task task : tasks.values()) {
            history.remove(task.getId());
            prioritizedTasks.remove(task);
        }
        tasks.clear();
    }

    /*
     * Возвращает задачу по идентификатору
     */
    @Override
    public Task getTask(int id) {
        history.add(tasks.get(id));
        return tasks.get(id);
    }

    /*
     * Создаёт новую задачу
     */
    @Override
    public Task newTask(Task task) {
        task.setId(generatorId());
        checkCrossing(task);
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
        return task;
    }

    /*
     * Обновляет задачу
     */
    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return;
        }
        checkCrossing(task);
        tasks.put(task.getId(), task);
    }

    /*
     * Удаляет задачу по её идентификатору
     */
    @Override
    public void deleteTask(int id) {
        if (!tasks.containsKey(id)) {
            return;
        }
        prioritizedTasks.remove(tasks.get(id));
        history.remove(id);
        tasks.remove(id);
    }

    /*
     * Возвращает список всех подзадач
     */
    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    /*
     * Удаляет все подзадачи
     */
    @Override
    public void deleteSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            history.remove(subtask.getId());
            prioritizedTasks.remove(subtask);
            subtasks.remove(subtask.getId());
            statusOfEpic(subtask);
            durationOfEpic(subtask.getEpicId());
        }
    }

    /*
     * Возвращает подзадачу по идентификатору
     */
    @Override
    public Subtask getSubtask(int id) {
        history.add(subtasks.get(id));
        return subtasks.get(id);
    }

    /*
     * Добавляет подзадачу
     */
    @Override
    public Subtask newSubtask(Subtask subtask) {
        checkCrossing(subtask);
        subtask.setId(generatorId());
        if (epics.get(subtask.getEpicId()) == null) {
            System.out.println("Сначала требуется создать Эпик");
        } else {
            subtasks.put(subtask.getId(), subtask);
            prioritizedTasks.add(subtask);
            Epic epic = epics.get(subtask.getEpicId());
            ArrayList<Subtask> epicsSubtasks = epic.getSubtask(); // получает список подзадач эпика
            epicsSubtasks.add(subtask); // добавляет доп. подзадачу в список
//            epic.setSubtask(epicsSubtasks); // сохраняет подзадачу в эпик
            statusOfEpic(subtask); // пересчитывает статус эпика
            durationOfEpic(subtask.getEpicId());
        }
        return subtask;
    }

    /*
     * Обновляет подзадачу
     */
    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            System.out.println("Нет подзадачи с id " + subtask.getId());
            return;
        }
        checkCrossing(subtask);
        subtasks.put(subtask.getId(), subtask);
        updateSubtaskInEpic(subtask);
        statusOfEpic(subtask);// пересчитывает статус эпика
        durationOfEpic(subtask.getEpicId());
    }

    /*
     * Удаляет подзадачу по идентификатору
     */
    @Override
    public void deleteSubtask(int id) {
        if (!subtasks.containsKey(id)) {
            return;
        }
        history.remove(id);
        prioritizedTasks.remove(subtasks.get(id));
        Subtask subtaskForRemove = subtasks.get(id);
        int epicIdOfRemoveSubtask = subtaskForRemove.getEpicId();
        subtasks.remove(id);
        deleteSubtaskFromEpic(subtaskForRemove);
        statusOfEpic(subtaskForRemove);
        durationOfEpic(epicIdOfRemoveSubtask);
    }

    /*
     * Возвращает список всех эпиков
     */
    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    /*
     * Удаляет все эпики
     */
    @Override
    public void deleteEpics() {
        for (int epicId : epics.keySet()) {
            ArrayList<Subtask> epicsSubtasks = epics.get(epicId).getSubtask(); // получает список подзадач эпика
            for (Subtask subtask : epicsSubtasks) {
                subtasks.remove(subtask.getId());
                history.remove(subtask.getId());
                prioritizedTasks.remove(subtask);
            }
            history.remove(epicId);
        }
        epics.clear();
    }

    /*
     * Возвращает эпик по идентификатору
     */
    @Override
    public Epic getEpic(int id) {
        history.add(epics.get(id));
        return epics.get(id);
    }

    /*
     * Создаёт новый эпик
     */
    @Override
    public Epic newEpic(Epic epic) {
        epic.setId(generatorId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    /*
     * Обновляет эпик
     */
    @Override
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
    @Override
    public void deleteEpic(int epicId) {
        if (!epics.containsKey(epicId)) {
            return;
        }
        ArrayList<Subtask> epicsSubtasks = (epics.get(epicId)).getSubtask(); // получает список подзадач эпика
        for (Subtask subtask : epicsSubtasks) {
            subtasks.remove(subtask.getId());
            history.remove(subtask.getId());
            prioritizedTasks.remove(subtask);
        }
        history.remove(epicId);
        epics.remove(epicId);
    }

    /*
     * Возвращает подзадачи конкретного эпика
     */
    @Override
    public ArrayList<Subtask> getSubtasksFormEpic(Epic epic) {
        return epic.getSubtask();
    }

    /*
     * Рассчитывает статус эпика
     */
    private void statusOfEpic(Subtask subtask) {
        int countNew = 0; // переменная для подсчёта статусов NEW
        int countDONE = 0; // переменная для подсчёта статусов DONE
        Epic epic = epics.get(subtask.getEpicId()); // нашли эпик, который содержит полученную подзадачу
        for (Subtask checkedSubtask : epic.getSubtask()) { // прошлись по всем подзадачам и получили их статусы
            if (checkedSubtask.getStatus().equals(Status.NEW)) { // проверили статус NEW
                countNew++; // если да, то считаем кол-во таких статусов
            } else if (checkedSubtask.getStatus().equals(Status.DONE)) { // проверили статус DONE
                countDONE++; // если да, то считаем кол-во таких статусов
            }
        }
        if (countNew == (epic.getSubtask()).size()) { // сравниваем кол-во статусов NEW с кол-вом подзадач
            epic.setStatus(Status.NEW); // если они совпадают, то присвааиваем эпику статус NEW
        } else if (countDONE == (epic.getSubtask()).size()) { // сравниваем кол-во статусов DONE с кол-вом подзадач
            epic.setStatus(Status.DONE); // если они совпадают, то присвааиваем эпику статус DONE
        } else {
            epic.setStatus(Status.IN_PROGRESS); // если ни то ни другое, то присваиваем статус IN_PROGRESS
        }
    }

    /*
     * Генерирует id
     */
    @Override
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
            }
            (epic.getSubtask()).add(subtask); // добавляет новую подзадачу
        }
        durationOfEpic(subtask.getEpicId());
    }

    /*
     * Удаляет Подзадачу из списка в Эпике
     */
    private void deleteSubtaskFromEpic(Subtask subtask) {
        int numberOfSubtask;
        Epic epic = epics.get(subtask.getEpicId()); // находит эпик, который содержит полученную подзадачу
        for (int i = 0; i < (epic.getSubtask()).size(); i++) {
            if (((epic.getSubtask()).get(i)).getId() == subtask.getId()) { // находит порядковый номер Подзадачи
                numberOfSubtask = i;
                epic.getSubtask().remove(numberOfSubtask); // удаляет подзадачу
            }
        }
        durationOfEpic(subtask.getEpicId());
    }

    /*
     * Возвращает список истории
     */
    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    /*
     * Считает старт и продолжительность Эпика
     */
    private void durationOfEpic(int id) {
        int durationSum = 0;
        LocalDateTime startEpic = LocalDateTime.MAX;
        LocalDateTime endEpic = LocalDateTime.MIN;
        Epic epic = epics.get(id); // находит эпик, который содержит полученную подзадачу
        for (Subtask subtask : epic.getSubtask()) {
            durationSum = durationSum + subtask.getDuration();
            if (subtask.getStartTime().isBefore(startEpic)) {
                startEpic = subtask.getStartTime();
            }
            if (subtask.getEndTime().isAfter(endEpic)) {
                endEpic = subtask.getEndTime();
            }
        }
        epic.setDuration(durationSum);
        epic.setStartTime(startEpic);
        epic.setEndTime(endEpic);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<Task>(prioritizedTasks);
    }

    public void checkCrossing(Task task) {
        List<Task> prioritizedTasksList = new ArrayList<Task>(prioritizedTasks);
        for (int i = 0; i < prioritizedTasksList.size(); i++) {
            if ((task.getStartTime().isAfter(prioritizedTasksList.get(i).getStartTime()) && task.getStartTime().isBefore(prioritizedTasksList.get(i).getEndTime())) ||
                    (task.getStartTime().isBefore(prioritizedTasksList.get(i).getStartTime()) && task.getEndTime().isAfter(prioritizedTasksList.get(i).getStartTime()))) {
                System.out.println("Нужно изменить время задачи");
            }
        }
    }
}

