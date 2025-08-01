package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<SubTask> subtasks;
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW, LocalDateTime.MAX, Duration.ZERO);
        this.subtasks = new ArrayList<>();
        this.endTime = LocalDateTime.MAX;
    }

    public ArrayList<SubTask> getSubtasks() {
        return subtasks;
    }

    public void addSubTask(SubTask subTask) {
        subtasks.add(subTask);

    }

    public void removeSubTask(SubTask subTask) {
        subtasks.remove(subTask);
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime calculateEpicStartTime() {
        if (subtasks.isEmpty()) {
            return LocalDateTime.MAX;
        }
        return subtasks.stream().map(SubTask::getStartTime).filter(Objects::nonNull).min(LocalDateTime::compareTo).orElse(LocalDateTime.MAX);
    }


    public Duration calculateEpicDuration() {
        if (subtasks.isEmpty()) {
            return Duration.ZERO;
        }
        return subtasks.stream().map(SubTask::getDuration).reduce(Duration.ZERO, Duration::plus);
    }

    public LocalDateTime calculateEpicEndTime() {
        if (subtasks.isEmpty()) {
            return LocalDateTime.MAX;
        }
        return subtasks.stream().map(SubTask::getEndTime).filter(Objects::nonNull).max(LocalDateTime::compareTo).orElse(LocalDateTime.MAX);
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id='" + getId() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", startTime='" + getStartTime() + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                ", duration='" + getDuration() + '\'' +
                ", subtasksId='";
        for (SubTask subTask : subtasks) {
            result += subTask.getId() + " ";
        }
        result += "'}";
        return result;
    }
}
