package Tasks;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskTest {
    @Test
    public void TasksShouldBeEqualsIfHasEqualId() {
        Task task1 = new Task("title1", "description1", TaskStatus.NEW);
        Task task2 = new Task("title1", "description1", TaskStatus.NEW);
        task1.setId(1L);
        task2.setId(task1.getId());
        assertEquals(task1, task2);
    }
}
