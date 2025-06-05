package Tasks;

import org.junit.jupiter.api.Test;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubTaskTest {
    @Test
    public void SubTasksShouldBeEqualsIfHasEqualId() {
        SubTask subTask1 = new SubTask("title", "description", TaskStatus.NEW, 1L);
        SubTask subTask2 = new SubTask("title", "description", TaskStatus.NEW, 1L);
        subTask1.setId(1L);
        subTask2.setId(subTask1.getId());
        assertEquals(subTask1, subTask2);
    }
}
