package Http;

import Manager.FileBackedTasksManager;
import Manager.HistoryManager;
import Manager.InMemoryHistoryManager;
import com.google.gson.Gson;
import Model.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;

public class HttpTaskManager extends FileBackedTasksManager {
    Gson gson;
    KVTaskClient client;
//    int maxId = 0;

    public HttpTaskManager() {
    load();
    }

    @Override
    protected void save() {
        String jsonTasks = gson.toJson(new ArrayList<>(tasks.values()));
        client.put("tasks", jsonTasks);
        String jsonSubtasks = gson.toJson(new ArrayList<>(subtasks.values()));
        client.put("subtasks", jsonSubtasks);
        String jsonEpics = gson.toJson(new ArrayList<>(epics.values()));
        client.put("epics", jsonEpics);
        String jsonHistory = gson.toJson(new ArrayList<>(history.getHistory()));
        client.put("history", jsonHistory);
    }

    @Override
    protected void load() {
        ArrayList<Task> tasksArrayList = gson.fromJson(client.load("tasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        for (Task task : tasksArrayList) {
            tasks.put(task.getId(), task);
            if (id < task.getId()) {
                id = task.getId();
            }
            prioritizedTasks.add(task);
        }
        ArrayList<Epic> epicsArrayList = gson.fromJson(client.load("epics"), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        for (Epic epic : epicsArrayList) {
            epics.put(epic.getId(), epic);
            if (id < epic.getId()) {
                id = epic.getId();
            }
        }
        ArrayList<Subtask> subtasksArrayList = gson.fromJson(client.load("subtasks"), new TypeToken<ArrayList<Subtask>>() {
        }.getType());
        for (Subtask subtask : subtasksArrayList) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.addSubtask(subtask);
            if (id < subtask.getId()) {
                id = subtask.getId();
            }
            prioritizedTasks.add(subtask);
        }
        ArrayList<Task> historyArrayList = gson.fromJson(client.load("history"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        for (Task task : historyArrayList) {
            history.add(task);
            }
    }

    @Override
    public int generatorId() {
        id++;
        return id;
    }




}
