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

    public void addSubTask (SubTask subTask) {
        subtasks.add(subTask);
    }

    public void removeSubTask (SubTask subTask) {
        subtasks.remove(subTask);
    }

    public void updateEpicStatus() {
        boolean allDone = true;
        boolean allNew = true;
        for (SubTask subTask : subtasks) {
            if (subTask.getStatus() != TaskStatus.NEW) allNew = false;
            else if (subTask.getStatus() != TaskStatus.DONE) allDone = false;
        }
        if (allNew) {
            setStatus(TaskStatus.NEW);
        } else if (allDone) {
            setStatus(TaskStatus.DONE);
        } else {
            setStatus(TaskStatus.IN_PROGRESS);
        }
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
