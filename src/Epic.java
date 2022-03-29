import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;
    public Epic(String name, String description, int id, String status) {
        super(name, description, id, status);
    }

    public Epic(String name, String description, int id, String status, ArrayList<Subtask> subtask) {
        super(name, description, id, status);
        this.subtasks = subtask;
    }

    public ArrayList<Subtask> getSubtask() {
        return subtasks;
    }

    public void setSubtask(ArrayList<Subtask> subtask) {
        this.subtasks = subtask;
    }
}
