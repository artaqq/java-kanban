package tasks;

public class SubTask extends Task {
    private long epicId;

    public SubTask(String title, String description, TaskStatus status, long epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id='" + getId() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", epicId='" + epicId +
                "'}";
    }
}
