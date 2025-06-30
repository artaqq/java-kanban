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
    public void taskShouldBeAddedToTheEndOfTheHistory() {
        Task task1 = new Task("Title1", "Description1", TaskStatus.NEW);
        Task task2 = new Task("Title2", "Description2", TaskStatus.NEW);
        Task task3 = new Task("Title3", "Description3", TaskStatus.NEW);
        tm.createTask(task1);
        tm.createTask(task2);
        tm.createTask(task3);

        hm.add(task1);
        hm.add(task2);
        hm.add(task3);
        hm.add(task2);
        hm.add(task1);

        List<Task> history = hm.getHistory();

        assertEquals(task3, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task1, history.get(2));
    }

    @Test
    public void removeTest() {
        Task task = new Task("Title", "Description", TaskStatus.NEW);
        tm.createTask(task);
        hm.add(task);

        List<Task> historySizeShouldBeOne = hm.getHistory();

        hm.remove(task.getId());

        List<Task> historySizeShouldBeZero = hm.getHistory();

        assertEquals(1, historySizeShouldBeOne.size(), "После добавления задачи история не должна быть пустой.");
        assertEquals(0, historySizeShouldBeZero.size(), "После удаления задачи история должна быть пустой.");
    }

    @Test
    public void shouldReturnSizeOfHistoryOneIfAddTaskTwice() {
        Task task = new Task("Title", "Description", TaskStatus.NEW);
        hm.add(task);
        hm.add(task);
        List<Task> history = hm.getHistory();
        assertEquals(1, history.size(), "В истории не должно быть повторений.");
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
}
