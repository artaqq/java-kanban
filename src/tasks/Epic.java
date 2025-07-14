package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subtasks;

    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW);
        this.subtasks = new ArrayList<>();
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
    public String toString() {
        String result = "Epic{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id='" + getId() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", subtasksId='";
        for (SubTask subTask : subtasks) {
            result += subTask.getId() + " ";
        }
        result += "'}";
        return result;
    }
}
