import Http.KVServer;
import Http.KVTaskClient;
import Manager.*;
import Model.*;
import Status.*;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        /*
         */
        Task test1 = new Task("Задача1", "тест", 0, Status.NEW, LocalDateTime.now(), 15);
        manager.newTask(test1);
        Task test2 = new Task("Задача2", "тест", 0, Status.NEW, LocalDateTime.now(), 15);
        manager.newTask(test2);
        System.out.println("Созданы 2 задачи " + manager.getTasks());
        System.out.println();
/*
        ArrayList<Subtask> subtasks = new ArrayList<>();
        Epic test3 = new Epic("Эпик1", "тест", 0, Status.NEW, subtasks);
        manager.newEpic(test3);
        System.out.println("Создан Эпик1 " + manager.getEpic(test3.getId()) + " с id " + test3.getId());

//        Subtask test4 = new Subtask("Подзадача1", "тест", 0, Status.NEW, 3);
        Subtask test4 = new Subtask("Подзадача1", "тест", 0, Status.NEW, LocalDateTime.now(), 15, 3);
        manager.newSubtask(test4);
//        Subtask test5 = new Subtask("Подзадача2", "тест", 0, Status.NEW, 3);
        Subtask test5 = new Subtask("Подзадача2", "тест", 0, Status.NEW, LocalDateTime.now(), 15, 3);
        manager.newSubtask(test5);
        System.out.println("В Эпик1 добавили 2 Подзадачи " + manager.getEpic(test3.getId()) + "с id " + test4.getId() + " и " + test5.getId());
        System.out.println("Статус Эпик1 " + test3.getStatus());
        System.out.println();

//        TaskManager manager1 = new FileBackedTasksManager(new File("task.csv"), true);


        ArrayList<Subtask> subtasks2 = new ArrayList<>();
        Epic test6 = new Epic("Эпик2", "тест", 0, Status.NEW, subtasks2);
        manager.newEpic(test6);
        System.out.println("Создан Эпик2 " + manager.getEpic(test6.getId()) + " с id " + test6.getId());

//        Subtask test7 = new Subtask("Подзадача3", "тест", 0, Status.NEW, 6);
        Subtask test7 = new Subtask("Подзадача3", "тест", 0, Status.NEW, LocalDateTime.now(), 15,6);
        manager.newSubtask(test7);
        System.out.println("В Эпик2 добавили 1 Подзадачу " + manager.getSubtask(test7.getId()));
        System.out.println("В Эпике2 стало " + manager.getEpic(test6.getId()));
        System.out.println();
        System.out.println("Получаем историю: " + manager.getHistory());

        System.out.println("В Эпике1 поменяем статус Позадачи1 на DONE");
//        Subtask test8 = new Subtask("Выполненная подзадача", "тест", 4, Status.DONE, 3);
        Subtask test8 = new Subtask("Выполненная подзадача", "тест", 4, Status.DONE,LocalDateTime.now(), 15, 3);
        manager.updateSubtask(test8);
        System.out.println("Статус Подзадачи1 " + (manager.getSubtask(4)).getStatus());
        System.out.println("Статус Подзадачи2 " + (manager.getSubtask(5)).getStatus());
        System.out.println("Статус Эпик1 " + (manager.getEpic(3)).getStatus());
        System.out.println();
        System.out.println("Получаем историю: " + manager.getHistory());


        System.out.println("В Эпике1 поменяем статус Подзадачи2 на DONE");
//        Subtask test9 = new Subtask("Выполненная подзадача", "тест", 5, Status.DONE, 3);
        Subtask test9 = new Subtask("Выполненная подзадача", "тест", 5, Status.DONE, LocalDateTime.now(), 15, 3);
        manager.updateSubtask(test9);
        System.out.println("Статус Подзадачи1 " + (manager.getSubtask(4)).getStatus());
        System.out.println("Статус Подзадачи2 " + (manager.getSubtask(5)).getStatus());
        System.out.println("Статус Эпик1 " + (manager.getEpic(3)).getStatus());

        System.out.println("Получаем историю " + manager.getHistory());
        System.out.println();
        System.out.println(manager.getPrioritizedTasks());

//        manager.getEpic(3);
//        System.out.println();

//        System.out.println("Получаем историю " + manager.getHistory());
//
//        System.out.println("Получаем эпики " + manager.getEpics());
//        System.out.println("Удаляем все эпики");
//        manager.deleteEpics();
//        System.out.println("Получаем эпики после их удаления" + manager.getEpics());
//        System.out.println("Получаем историю после удаления эпиков " + manager.getHistory());

        TaskManager manager1 = new FileBackedTasksManager(new File("task.csv"), true);


        if (manager.getEpics().equals(manager1.getEpics())) {
            System.out.println("Эпики совпали");
        } else {
            System.out.println("Эпики не совпали");
        }
        if (manager.getTasks().equals(manager1.getTasks())) {
            System.out.println("Задачи совпали");
        } else {
            System.out.println("Задачи не совпали");
        }
        if (manager.getHistory().equals(manager1.getHistory())) {
            System.out.println("Истории совпали");
        } else {
            System.out.println("Истории не совпали");
            System.out.println(manager.getHistory());
            System.out.println(manager1.getHistory());
    }*/
        Gson gson = new Gson();
        String json = gson.toJson(manager.getTasks());
        System.out.println(json);


//        KVServer kvServer = null;
//        try {
//            kvServer = new KVServer();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        kvServer.start();
        KVTaskClient kvTaskClient = null;
        try {
            kvTaskClient = new KVTaskClient();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        kvTaskClient.put("tasks", json);

        kvTaskClient.load("tasks");

    }


    }

