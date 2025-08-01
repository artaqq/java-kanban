package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private long epicId;

    public SubTask(String title, String description, TaskStatus status, long epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public SubTask(String title, String description, TaskStatus status, LocalDateTime startTime, Duration duration, long epicId) {
        super(title, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id='" + getId() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", startTime='" + getStartTime() + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                ", duration='" + getDuration() + '\'' +
                ", epicId='" + epicId +
                "'}";
    }
}
