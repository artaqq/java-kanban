package managers;

import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class CSVFormatter {

    public static String getHeader() {
        return "id,type,name,status,description,startTime,duration,epic/endTime";
    }

    public static String toString(Task task) {
        String taskToString;
        if (task.getType() == TaskType.SUBTASK) {
            SubTask subtask = (SubTask) task;
            taskToString = subtask.getId() + "," + subtask.getType() + "," + subtask.getTitle() + ","
                    + subtask.getStatus() + "," + subtask.getDescription() + ","
                    + subtask.getStartTime() + "," + subtask.getDuration().toMinutes() + "," + subtask.getEpicId();
        } else if (task.getType() == TaskType.TASK) {
            taskToString = task.getId() + "," + task.getType() + "," + task.getTitle() + "," + task.getStatus() + ","
                    + task.getDescription() + "," + task.getStartTime() + "," + task.getDuration().toMinutes();
        } else {
            Epic epic = (Epic) task;
            taskToString = epic.getId() + "," + epic.getType() + "," + epic.getTitle() + "," + epic.getStatus() + ","
                    + epic.getDescription() + "," + epic.getStartTime() + "," + epic.getDuration().toMinutes() + "," + epic.getEndTime();
        }
        return taskToString;
    }

    public static Task fromString(String value) {
        String[] taskFields = value.split(",");

        if (taskFields[1].equals("SUBTASK")) {
            SubTask loadedSubTask = new SubTask(taskFields[2], taskFields[4], TaskStatus.valueOf(taskFields[3]),
                    LocalDateTime.parse(taskFields[5]), Duration.ofMinutes(Long.parseLong(taskFields[6])), Long.parseLong(taskFields[7]));
            loadedSubTask.setId(Long.parseLong(taskFields[0]));
            return loadedSubTask;
        } else if (taskFields[1].equals("EPIC")) {
            Epic loadedEpic = new Epic(taskFields[2], taskFields[4]);
            loadedEpic.setStartTime(LocalDateTime.parse(taskFields[5]));
            loadedEpic.setDuration(Duration.ofMinutes(Long.parseLong(taskFields[6])));
            loadedEpic.setEndTime(LocalDateTime.parse(taskFields[7]));
            loadedEpic.setId(Long.parseLong(taskFields[0]));
            return loadedEpic;

        } else if (taskFields[1].equals("TASK")) {
            Task loadedTask = new Task(taskFields[2], taskFields[4], TaskStatus.valueOf(taskFields[3]),
                    LocalDateTime.parse(taskFields[5]), Duration.ofMinutes(Long.parseLong(taskFields[6])));
            loadedTask.setId(Long.parseLong(taskFields[0]));
            return loadedTask;
        }
        throw new ManagerSaveException("Неизвестный тип задачи.");
    }
}
