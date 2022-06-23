package Http;


import Manager.HistoryManager;
import Manager.Managers;
import Manager.TaskManager;
import Model.Epic;
import Model.Subtask;
import Model.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    TaskManager taskManager = Managers.getDefault();
    private static Gson gson;
    HttpServer server;
    int PORT = 8080;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handleTasks);
        server.createContext("/tasks/subtask", this::handleSubtask);
        server.createContext("/tasks/task", this::handleTask);
        server.createContext("/tasks/epic", this::handleEpic);
        server.createContext("/tasks/history", this::handleHistory);
    }


    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();


    }

    public static void main(String[] args) throws IOException, InterruptedException {
    }

    private void handleTask(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET": {
                if (query == null) {
                    final List<Task> tasks = taskManager.getTasks();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получили все задачи");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);// ?id=
                final int id = Integer.parseInt(idParam);
                final Task task = taskManager.getTask(id);
                final String response = gson.toJson(task);
                System.out.println("Получили задачу id=" + id);
                sendText(h, response);
            }
            case "POST": {
                if (query == null) {
                    final List<Task> tasks = taskManager.getTasks();
                    final String response = gson.toJson(tasks);
                    System.out.println("Сохранили все задачи");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);// ?id=
                final int id = Integer.parseInt(idParam);
                final Task task = taskManager.getTask(id);
                final String response = gson.toJson(task);
                System.out.println("Сохранили задачу id=" + id);
                sendText(h, response);
            }
            case "DELETE": {
                if (query == null) {
                    final List<Task> tasks = taskManager.getTasks();
                    final String response = gson.toJson(tasks);
                    System.out.println("Удалили все задачи");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);// ?id=
                final int id = Integer.parseInt(idParam);
                final Task task = taskManager.getTask(id);
                final String response = gson.toJson(task);
                System.out.println("Удалили задачу id=" + id);
                sendText(h, response);
            }
        }
    }

    private void handleSubtask(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET": {
                if (query == null) {
                    final List<Subtask> subtasks = taskManager.getSubtasks();
                    final String response = gson.toJson(subtasks);
                    System.out.println("Получили все подзадачи");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);// ?id=
                final int id = Integer.parseInt(idParam);
                final Subtask subtask = taskManager.getSubtask(id);
                final String response = gson.toJson(subtask);
                System.out.println("Получили подзадачу id=" + id);
                sendText(h, response);
            }
            case "POST": {
                if (query == null) {
                    final List<Subtask> subtasks = taskManager.getSubtasks();
                    final String response = gson.toJson(subtasks);
                    System.out.println("Сохранили все подзадачи");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);// ?id=
                final int id = Integer.parseInt(idParam);
                final Subtask subtask = taskManager.getSubtask(id);
                final String response = gson.toJson(subtask);
                System.out.println("Сохранили подзадачу id=" + id);
                sendText(h, response);
            }
            case "DELETE": {
                if (query == null) {
                    final List<Subtask> subtasks = taskManager.getSubtasks();
                    final String response = gson.toJson(subtasks);
                    System.out.println("Удалили все подзадачи");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);// ?id=
                final int id = Integer.parseInt(idParam);
                final Subtask subtask = taskManager.getSubtask(id);
                final String response = gson.toJson(subtask);
                System.out.println("Удалили подзадачу id=" + id);
                sendText(h, response);
            }
        }
    }

    private void handleEpic(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET": {
                if (query == null) {
                    final List<Epic> epics = taskManager.getEpics();
                    final String response = gson.toJson(epics);
                    System.out.println("Получили все эпики");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);// ?id=
                final int id = Integer.parseInt(idParam);
                final Epic epic = taskManager.getEpic(id);
                final String response = gson.toJson(epic);
                System.out.println("Получили эпик id=" + id);
                sendText(h, response);
            }
            case "POST": {
                if (query == null) {
                    final List<Epic> epics = taskManager.getEpics();
                    final String response = gson.toJson(epics);
                    System.out.println("Сохранили все эпики");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);// ?id=
                final int id = Integer.parseInt(idParam);
                final Epic epic = taskManager.getEpic(id);
                final String response = gson.toJson(epic);
                System.out.println("Сохранили эпик id=" + id);
                sendText(h, response);
            }
            case "DELETE": {
                if (query == null) {
                    final List<Epic> epics = taskManager.getEpics();
                    final String response = gson.toJson(epics);
                    System.out.println("Удалили все эпики");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);// ?id=
                final int id = Integer.parseInt(idParam);
                final Epic epic = taskManager.getEpic(id);
                final String response = gson.toJson(epic);
                System.out.println("Удалили эпик id=" + id);
                sendText(h, response);
            }
        }
    }

    private void handleTasks(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET": {
                if (query == null) {
                    final List<Task> tasks = taskManager.getTasks();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получили все задачи");
                    sendText(h, response);
                    final List<Subtask> subtasks = taskManager.getSubtasks();
                    final String response2 = gson.toJson(subtasks);
                    System.out.println("Получили все подзадачи");
                    sendText(h, response);
                    final List<Epic> epics = taskManager.getEpics();
                    final String response3 = gson.toJson(epics);
                    System.out.println("Получили все эпики");
                    sendText(h, response);
                    break;
                }
            }
            default:
                System.out.println("Ожидался запрос GET");
        }
    }

    private void handleHistory(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET": {
                if (query == null) {
                    final List<Task> history = taskManager.getHistory();
                    final String response = gson.toJson(history);
                    System.out.println("Получили историю");
                    sendText(h, response);
                    break;
                }
            }
        }
    }

    private void handlePrioritet(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET": {
                if (query == null) {
                    final List<Task> history = taskManager.getHistory();
                    final String response = gson.toJson(history);
                    System.out.println("Получили историю");
                    sendText(h, response);
                    break;
                }
            }
        }
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
