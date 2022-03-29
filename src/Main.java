public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task test = new Task("name", "тест", 0, Status.NEW);
        final Task task = manager.newTask(test);
        final Task task1 = manager.getTask(task.getId());
        if (!task.equals(task1)) {
            System.out.println("Нет задачи с id " + task.getId());
        }
        System.out.println(manager.getTasks());
        manager.deleteTask(task.getId());

    }
}
