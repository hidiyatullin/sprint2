package Model;
import Status.*;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;
    public Epic(String name, String description, int id, Status status) {
        super(name, description, id, status);
    }

    public Epic(String name, String description, int id, Status status, ArrayList<Subtask> subtask) {
        super(name, description, id, status);
        this.subtasks = subtask;
    }

    public ArrayList<Subtask> getSubtask() {
        return subtasks;
    }

    public void setSubtask(ArrayList<Subtask> subtask) {
        this.subtasks = subtask;
    }

    @Override
    public String toString() {
        return "Tasks.Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", subtasks=" + subtasks +
                '}';
    }
}
