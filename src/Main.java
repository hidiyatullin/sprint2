import Manager.Manager;
import Model.*;
import Status.Status;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task test1 = new Task("Задача1", "тест", 0, Status.NEW);
        manager.newTask(test1);
        Task test2 = new Task("Задача2", "тест", 0, Status.NEW);
        manager.newTask(test2);
        System.out.println("Созданы 2 задачи " + manager.getTasks());
        System.out.println();

        ArrayList<Subtask> subtasks = new ArrayList<>();
        Epic test3 = new Epic("Эпик1", "тест", 0, Status.NEW, subtasks);
        manager.newEpic(test3);
        System.out.println("Создан Эпик1 " + manager.getEpic(test3.getId()) + " с id " + test3.getId());

        Subtask test4 = new Subtask("Подзадача1", "тест", 0, Status.NEW, 3);
        manager.newSubtask(test4);
        Subtask test5 = new Subtask("Подзадача2", "тест", 0, Status.NEW, 3);
        manager.newSubtask(test5);
        System.out.println("В Эпик1 добавили 2 Подзадачи " + manager.getEpic(test3.getId()) + "с id " + test4.getId() + " и " + test5.getId());
        System.out.println("Статус Эпик1 " + test3.getStatus());
        System.out.println();

        ArrayList<Subtask> subtasks2 = new ArrayList<>();
        Epic test6 = new Epic("Эпик2", "тест", 0, Status.NEW, subtasks2);
        manager.newEpic(test6);
        System.out.println("Создан Эпик2 " + manager.getEpic(test6.getId()) + " с id " + test6.getId());

        Subtask test7 = new Subtask("Подзадача3", "тест", 0, Status.NEW, 6);
        manager.newSubtask(test7);
        System.out.println("В Эпик2 добавили 1 Подзадачу " + manager.getSubtask(test7.getId()));
        System.out.println("В Эпике2 стало " + manager.getEpic(test6.getId()));
        System.out.println();

        System.out.println("В Эпике1 поменяем статус Позадачи1 на DONE");
        Subtask test8 = new Subtask("Выполненная подзадача", "тест", 4, Status.done, 3);
        manager.updateSubtask(test8);
        System.out.println("Статус Подзадачи1 " + (manager.getSubtask(4)).getStatus());
        System.out.println("Статус Подзадачи2 " + (manager.getSubtask(5)).getStatus());
        System.out.println("Статус Эпик1 " + (manager.getEpic(3)).getStatus());
        System.out.println();

        System.out.println("В Эпике1 поменяем статус Подзадачи2 на DONE");
        Subtask test9 = new Subtask("Выполненная подзадача", "тест", 5, Status.done, 3);
        manager.updateSubtask(test9);
        System.out.println("Статус Подзадачи1 " + (manager.getSubtask(4)).getStatus());
        System.out.println("Статус Подзадачи2 " + (manager.getSubtask(5)).getStatus());
        System.out.println("Статус Эпик1 " + (manager.getEpic(3)).getStatus());
    }
}
