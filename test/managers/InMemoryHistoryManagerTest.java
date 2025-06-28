package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    public static HistoryManager hm;
    private static TaskManager tm;

    @BeforeEach
    public void beforeEach() {
        hm = new InMemoryHistoryManager();
        tm = new InMemoryTaskManager();
    }

    @Test
    public void addTest() {
        Task task = new Task("Title", "Description", TaskStatus.NEW);
        hm.add(task);
        List<Task> history = hm.getHistory();

        assertNotNull(history, "После добавления задачи, история не должна быть пустой.");
        assertEquals(1, history.size(), "После добавления задачи история не должна быть пустой.");
    }

    @Test
    public void getHistoryShouldReturnOldTaskAfterUpdate() {
        Task task = new Task("Title", "Description", TaskStatus.NEW);
        tm.createTask(task);
        hm.add(tm.getTaskById(task.getId()));
        Task taskAfterAddToHistory = hm.getHistory().get(0);
        task.setTitle("UpdatedTitle");
        task.setDescription("UpdatedDescription");
        task.setStatus(TaskStatus.IN_PROGRESS);
        tm.updateTask(task);
        Task taskFromHistoryAfterUpdate = hm.getHistory().get(0);

        assertEquals(taskAfterAddToHistory.getTitle(), taskFromHistoryAfterUpdate.getTitle(), "В истории не сохранилась старая версия задачи.");
        assertEquals(taskAfterAddToHistory.getDescription(), taskFromHistoryAfterUpdate.getDescription(), "В истории не сохранилась старая версия задачи.");
        assertEquals(taskAfterAddToHistory.getStatus(), taskFromHistoryAfterUpdate.getStatus(), "В истории не сохранилась старая версия задачи.");

    }

    @Test
    public void shouldRemoveFirstTaskIfSizeOfHistoryMoreThan10() {
        for (int i = 1; i <= 11; i++) {
            Task task = new Task(("Title" + i), ("Description" + i), TaskStatus.NEW);
            hm.add(task);
        }
        List<Task> history = hm.getHistory();
        Task firstTask = history.get(0);

        assertEquals("Title2", firstTask.getTitle(), "В истории должно храниться не более 10 задач.");
        assertEquals("Description2", firstTask.getDescription(), "В истории должно храниться не более 10 задач.");
    }


}
