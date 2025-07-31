package managers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVFormatterTest {

    private TaskManager tm;

    @BeforeEach
    public void setUp() {
        tm = Managers.getDefault();
    }

    @Test
    public void toStringTest() {
        Task task = new Task("Shopping", "Shopping in shopping centre", TaskStatus.NEW, LocalDateTime.of(2025, 10, 10, 10, 10), Duration.ofMinutes(60));
        Epic epic = new Epic("Epic", "EpicDescription");
        tm.createTask(task);
        tm.createEpic(epic);

        SubTask subTask = new SubTask("SubTask", "SubTusk Description", TaskStatus.NEW, LocalDateTime.of(2025, 11, 11, 11, 11), Duration.ofMinutes(60), epic.getId());
        tm.createSubTask(subTask);

        String taskToString = CSVFormatter.toString(task);
        String subTaskToString = CSVFormatter.toString(subTask);
        assertEquals("1,TASK,Shopping,NEW,Shopping in shopping centre,2025-10-10T10:10,60", taskToString, "Неверная конвертация задачи в строку.");
        assertEquals("3,SUBTASK,SubTask,NEW,SubTusk Description,2025-11-11T11:11,60,2", subTaskToString, "Неверная конвертация подзадачи в строку.");
    }

    @Test
    public void fromStringTest() {
        Task task = new Task("Shopping", "Shopping in shopping centre", TaskStatus.NEW, LocalDateTime.of(2025, 10, 10, 10, 10), Duration.ofMinutes(60));
        Task taskFromString = CSVFormatter.fromString("1,TASK,Shopping,NEW,Shopping in shopping centre,2025-10-10T10:10,60");
        tm.createTask(task);

        assertEquals(task, taskFromString, "Неверная конвертация задачи из строки.");
    }

}
