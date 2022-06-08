package Model;

import Status.Status;

import java.time.LocalDateTime;

import static Model.TypeOfTask.SUBTASK;
import static Model.TypeOfTask.TASK;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int id, Status status) {
        super(name, description, id, status);
    }

    public Subtask(String name, String description, int id, Status status, LocalDateTime startTime, int duration, int epicId) {
        super(name, description, id, status, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Tasks.Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", epicId=" + epicId +
                ", start=" + startTime +
                '}';
    }

    @Override
    public TypeOfTask getType() {
        return TypeOfTask.SUBTASK;
    }
}
