package Manager;
import Model.*;
import Status.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HistoryManager history = Managers.getDefaultHistory();
    private int id = 0; // переменная для создания идентификатора

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
        tasks.put(task.getId(), task);
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
        history.remove(tasks.get(id));
        tasks.remove(id);
    }

    /*
     * Возвращает список всех подзадач
     */
    @Override
    public ArrayList<Subtask> getSubtasksFormEpic() {
        return new ArrayList<>(subtasks.values());
    }

    /*
     * Удаляет все подзадачи
     */
    @Override
    public void deleteSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            history.remove(subtask);
        }
        subtasks.clear();
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
    @Override
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
    @Override
    public void deleteSubtask(int id) {
        if (!subtasks.containsKey(id)) {
            return;
        }
        history.remove(subtasks.get(id));
        subtasks.remove(id);
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
        for (int EpicId : epics.keySet()) {
            ArrayList<Subtask> epicsSubtasks = epics.get(EpicId).getSubtask(); // получает список подзадач эпика
            for (Subtask subtask : epicsSubtasks) {
                history.remove(subtask);
                subtasks.remove(subtask.getId());
                history.remove(epics.get(EpicId));
            }
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
            history.remove(subtask);
        }
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
                } (epic.getSubtask()).add(subtask); // добавляет новую подзадачу
        }
    }

    /*
     * Возвращает список истории
     */
    @Override
    public List<Task> getHistory() {
        return history.getHistory();
        }


}
