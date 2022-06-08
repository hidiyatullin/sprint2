package Manager;

import Model.*;
import Status.Status;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Model.TypeOfTask.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager() {
        this(new File("task.csv"), false);
    }

    public FileBackedTasksManager(File file) {
        this(file, false);
    }

    public FileBackedTasksManager(File file, boolean load) {
        this.file = file;
        if (load) {
            load();
        }
    }

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = super.getTasks();
        save();
        return tasks;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Task newTask(Task task) {
        Task task1 = super.newTask(task);
        save();
        return task1;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtasksFormEpic = super.getSubtasks();
        save();
        return subtasksFormEpic;
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public Subtask newSubtask(Subtask subtask) {
        Subtask subtask1 = super.newSubtask(subtask);
        save();
        return subtask1;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epics = super.getEpics();
        save();
        return epics;
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Epic newEpic(Epic epic) {
        Epic epic1 = super.newEpic(epic);
        save();
        return epic1;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(int epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public ArrayList<Subtask> getSubtasksFormEpic(Epic epic) {
        ArrayList<Subtask> subtasksFormEpic = super.getSubtasksFormEpic(epic);
        save();
        return subtasksFormEpic;
    }

    @Override
    public int generatorId() {
        int i = super.generatorId();
        save();
        return i;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = super.getHistory();
        save();
        return history;
    }

    private String toString(Task task) {
        String result = task.getId()
                + "," + task.getType()
                + "," + task.getName()
                + "," + task.getStatus()
                + "," + task.getDescription()
                + "," + task.getStartTime()
                + "," + task.getDuration();
//                + "," + task.getEndTime();
        if (task.getType() == TypeOfTask.SUBTASK) {
            Subtask subtask = (Subtask) task;
            result = result + "," + subtask.getEpicId();
        }
        return result;
    }

    private Task fromString(String value) {
        final String[] key = value.split(",");
        switch (valueOf(key[1])) {
            case TASK:
                Task task = new Task(key[2], key[4], Integer.parseInt(key[0]), Status.valueOf(key[3]), LocalDateTime.parse(key[5]), Integer.parseInt(key[6]));
//                Task task = new Task(key[2], key[4], Integer.parseInt(key[0]), Status.valueOf(key[3]));
                return task;
            case SUBTASK:
                Subtask subtask = new Subtask(key[2], key[4], Integer.parseInt(key[0]), Status.valueOf(key[3]), LocalDateTime.parse(key[5]), Integer.parseInt(key[6]), Integer.parseInt(key[7]));
                return subtask;
            case EPIC:
                Epic epic = new Epic(key[2], key[4], Integer.parseInt(key[0]), Status.valueOf(key[3]));
                return epic;
            default:
                return null;
        }
    }

    static String toString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getId());
            sb.append(",");
        }
        return sb.toString();
    }

    static List<Integer> historyFromString(String value) {
        final String[] id = value.split(",");
        List<Integer> history = new ArrayList<>();
        for (String v : id) {
            history.add(Integer.parseInt(v));
        }
        return history;
    }

    private void save() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file, UTF_8))) {
            writer.append("id,type,name,status,description,startTime,duration,epicId" + "\n");
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            writer.newLine();
            writer.append(toString(history));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения");
        }
    }

    private void load() {
        int maxId = 0;
        try (final BufferedReader reader = new BufferedReader(new FileReader(file, UTF_8))) {
            reader.readLine(); // Пропускаем заголовок
            while (true) {
                String line = reader.readLine();
                if (line.isEmpty()) {
                    break;
                }
                final Task task = fromString(line);
                final int id = task.getId();
                if (task.getType() == TypeOfTask.TASK) {
                    tasks.put(id, task);
                } else if (task.getType() == TypeOfTask.EPIC) {
                    epics.put(id, (Epic) task);
                } else if (task.getType() == TypeOfTask.SUBTASK) {
                    Subtask subtask = (Subtask) task;
                    subtasks.put(id, subtask);
                    Epic epic = epics.get(subtask.getEpicId());
                    epic.addSubtask(subtask);
                }
                if (maxId < id) {
                    maxId = id;
                }
            }
            String line = reader.readLine();
            if (!(line == null)) {
                for (Integer id : historyFromString(line)) {
                    if (tasks.containsKey(id)) {
                        history.add(tasks.get(id));
                    } else if (subtasks.containsKey(id)) {
                        history.add(subtasks.get(id));
                    } else if (epics.containsKey(id)) {
                        history.add(epics.get(id));
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки файла: " + file.getName());
        }
        this.id = maxId;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager manager = new FileBackedTasksManager(file, true);
        return manager;
    }
}
