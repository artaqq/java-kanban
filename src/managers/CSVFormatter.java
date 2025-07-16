package managers;

import tasks.*;

public class CSVFormatter {

    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }

    public static String toString(Task task) {
        String taskToString;
        if (task.getType() == TaskType.SUBTASK) {
            SubTask subtask = (SubTask) task;
            taskToString = subtask.getId() + "," + subtask.getType() + "," + subtask.getTitle() + "," + subtask.getStatus() + "," + subtask.getDescription() + "," + subtask.getEpicId();
        } else {
            taskToString = task.getId() + "," + task.getType() + "," + task.getTitle() + "," + task.getStatus() + "," + task.getDescription() + ",";
        }
        return taskToString;
    }

    public static Task fromString(String value) {
        String[] taskFields = value.split(",");

        if (taskFields[1].equals("SUBTASK")) {
            SubTask loadedSubTask = new SubTask(taskFields[2], taskFields[4], TaskStatus.valueOf(taskFields[3]), Long.parseLong(taskFields[5]));
            loadedSubTask.setId(Long.parseLong(taskFields[0]));
            return loadedSubTask;
        } else if (taskFields[1].equals("EPIC")) {
            Epic loadedEpic = new Epic(taskFields[2], taskFields[4]);
            loadedEpic.setId(Long.parseLong(taskFields[0]));
            return loadedEpic;

        } else if (taskFields[1].equals("TASK")) {
            Task loadedTask = new Task(taskFields[2], taskFields[4], TaskStatus.valueOf(taskFields[3]));
            loadedTask.setId(Long.parseLong(taskFields[0]));
            return loadedTask;
        }
        throw new ManagerSaveException("Неизвестный тип задачи.");
    }
}
