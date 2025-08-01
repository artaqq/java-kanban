package managers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    private static InMemoryTaskManager tm;

    @BeforeEach
    public void beforeEach() {
        tm = new InMemoryTaskManager();
    }

    @AfterEach
    public void afterEach() {
        tm.deleteAllTasks();
        tm.deleteAllSubTasks();
        tm.deleteAllEpics();
    }


    @Test
    public void createTaskTest() {
        Task task = tm.createTask(new Task("CreateTaskTest", "CreateTaskTestDesc", TaskStatus.IN_PROGRESS, LocalDateTime.now(), Duration.ofMinutes(60)));
        Task taskAfterSave = tm.getTaskById(task.getId());
        assertNotNull(taskAfterSave, "Задача не найдена.");
        assertEquals(task, taskAfterSave, "Задачи с одинаковым Id не совпадают.");

        ArrayList<Task> tasks = tm.getTasks();
        assertNotNull(tasks, "Не возвращается список задач.");
        assertEquals(1, tasks.size(), "Неправильный размер списка задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void createEpicTest() {
        Epic epic = tm.createEpic(new Epic("CreateEpicTest", "CreateEpicTestDesc"));
        Epic epicAfterSave = tm.getEpicById(epic.getId());
        assertNotNull(epicAfterSave, "Эпик не найден.");
        assertEquals(epic, epicAfterSave, "Эпики с одинаковым Id не совпадают.");

        ArrayList<Epic> epics = tm.getEpics();

        assertNotNull(epics, "Не возвращается список эпиков.");
        assertEquals(1, epics.size(), "Неправильный размер списка эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    public void createSubTaskTest() {
        Epic epic = tm.createEpic(new Epic("CreateEpicTest", "CreateEpicTestDesc"));
        SubTask subTask1 = tm.createSubTask(new SubTask("CreateEpicTestSub1", "CreateEpicTestSubDesc1", TaskStatus.IN_PROGRESS, LocalDateTime.now(), Duration.ofMinutes(60), epic.getId()));
        SubTask subTaskAfterSave1 = tm.getSubTaskById(subTask1.getId());
        SubTask subTask2 = tm.createSubTask(new SubTask("CreateEpicTestSub2", "CreateEpicTestSubDesc2", TaskStatus.IN_PROGRESS, LocalDateTime.of(2025, 10, 10, 10, 10), Duration.ofMinutes(60), epic.getId()));
        assertNotNull(subTaskAfterSave1, "Подзадача не найдена");
        assertEquals(subTask1, subTaskAfterSave1, "Подзадачи с одинаковым Id не совпадают.");

        ArrayList<SubTask> subTasks = tm.getSubtasks();

        assertNotNull(subTasks, "Не возвращается список подзадач.");
        assertEquals(2, subTasks.size());
        assertEquals(subTask2, subTasks.get(1));
    }

    @Test
    public void updateTaskTest() {
        Task task = tm.createTask(new Task("title", "description", TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(60)));
        Task taskAfterSave = tm.getTaskById(task.getId());
        task.setTitle("UpdatedTitle");
        task.setDescription("UpdatedDescription");
        task.setStatus(TaskStatus.IN_PROGRESS);
        tm.updateTask(task);

        assertEquals(taskAfterSave.getId(), task.getId(), "Id задачи меняется после её обновления.");
        assertEquals("UpdatedTitle", tm.getTaskById(task.getId()).getTitle(), "Некорректное обновление названия задачи.");
        assertEquals("UpdatedDescription", tm.getTaskById(task.getId()).getDescription(), "Некорректное обновление описания задачи.");
        assertEquals(TaskStatus.IN_PROGRESS, tm.getTaskById(task.getId()).getStatus(), "Некорректное обновление статуса задачи.");

    }

    @Test
    public void updateEpicTest() {
        Epic epic = new Epic("Title", "Description");
        tm.createEpic(epic);
        Epic epicAfterSave = tm.getEpicById(epic.getId());
        epic.setTitle("UpdatedTitle");
        epic.setDescription("UpdatedDescription");
        tm.updateEpic(epic);

        assertEquals("UpdatedTitle", tm.getEpicById(epic.getId()).getTitle(), "Некорректное обновление названия эпика.");
        assertEquals("UpdatedDescription", tm.getEpicById(epic.getId()).getDescription(), "Некорректное обновление описания эпика.");
        assertEquals(epicAfterSave.getId(), epic.getId(), "Id эпика меняется после его обновления.");
    }

    @Test
    public void updateSubtaskTest() {
        Epic epic = new Epic("Title", "Description");
        tm.createEpic(epic);
        SubTask subTask = new SubTask("SubTitle", "SubDescription", TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(60), epic.getId());
        tm.createSubTask(subTask);
        SubTask subTaskAfterSave = tm.getSubTaskById(subTask.getId());
        subTask.setTitle("UpdatedSubTitle");
        subTask.setDescription("UpdatesSubDescription");
        subTask.setStatus(TaskStatus.IN_PROGRESS);
        tm.updateSubTask(subTask);

        assertEquals(subTaskAfterSave.getId(), subTask.getId(), "Id подзадачи меняется после её обновления.");
        assertEquals("UpdatedSubTitle", tm.getSubTaskById(subTask.getId()).getTitle(), "Некорректное обновление названия подзадачи");
        assertEquals("UpdatesSubDescription", tm.getSubTaskById(subTask.getId()).getDescription(), "Некорректное обновление описания подзадачи.");
        assertEquals(TaskStatus.IN_PROGRESS, tm.getSubTaskById(subTask.getId()).getStatus(), "Некорректное обновление статуса подзадачи.");
    }

    @Test
    public void deleteTaskByIdTest() {
        Task task = tm.createTask(new Task("Title", "Description", TaskStatus.IN_PROGRESS, LocalDateTime.now(), Duration.ofMinutes(60)));
        int count = tm.getTasks().size();
        tm.deleteTask(task.getId());
        int count1 = tm.getTasks().size();


        assertEquals(1, count, "Задача не была добавлена в список задач.");
        assertEquals(0, count1, "Задача не была удалена из списка задач.");
    }

    @Test
    public void deleteSubTaskByIdTest() {
        Epic epic = tm.createEpic(new Epic("EpicTitle", "EpicDescription"));
        SubTask subTask = tm.createSubTask(new SubTask("SubTitle", "SubDescription", TaskStatus.IN_PROGRESS, LocalDateTime.now(), Duration.ofMinutes(60), epic.getId()));
        int count = tm.getSubtasks().size();
        tm.deleteSubTask(subTask.getId());
        int count1 = tm.getSubtasks().size();

        assertEquals(1, count, "Подзадача не была добавлена в список подзадач");
        assertEquals(0, count1, "Подзадача не была удалена из списка подзадач.");
    }

    @Test
    public void deleteEpicByIdTest() {
        Epic epic = tm.createEpic(new Epic("EpicTitle", "EpicDescription"));
        SubTask subTask = tm.createSubTask(new SubTask("SubTitle", "SubDescription", TaskStatus.IN_PROGRESS, LocalDateTime.now(), Duration.ofMinutes(60), epic.getId()));
        int epicCount = tm.getEpics().size();
        int subTaskCount = tm.getSubtasks().size();
        tm.deleteEpic(epic.getId());
        int epicCount1 = tm.getEpics().size();
        int subTaskCount1 = tm.getSubtasks().size();

        assertEquals(1, epicCount, "Эпик не был добавлен в список эпиков.");
        assertEquals(1, subTaskCount, "Подзадача не была добавлена в список подзадач.");
        assertEquals(0, epicCount1, "Эпик не был удален из списка эпиков.");
        assertEquals(0, subTaskCount1, "При удалении эпика должны быть удалены все его подзадачи.");
    }

    @Test
    public void updateEpicStatusTest() {
        Epic epic = tm.createEpic(new Epic("EpicTitle", "EpicDescription"));
        SubTask subTask1 = tm.createSubTask(new SubTask("SubTask1Title", "SubTask1Description", TaskStatus.IN_PROGRESS, LocalDateTime.now(), Duration.ofMinutes(60), epic.getId()));
        SubTask subTask2 = tm.createSubTask(new SubTask("SubTask2Title", "SubTask2Description", TaskStatus.DONE, LocalDateTime.now().plusDays(3), Duration.ofMinutes(60), epic.getId()));
        TaskStatus epicStatusBeforeUpdate = epic.getStatus();
        subTask1.setStatus(TaskStatus.DONE);
        tm.updateSubTask(subTask1);
        tm.updateEpic(epic);

        assertEquals(TaskStatus.IN_PROGRESS, epicStatusBeforeUpdate, "Если хотя бы одна подзадача имеет статус IN_PROGRESS, тогда статус эпика - IN_PROGRESS");
        assertEquals(TaskStatus.DONE, epic.getStatus(), "Если все подзадачи эпика имеют статус DONE - эпик имеет статус DONE");
    }

    @Test
    public void epicCannotAddItselfAsSubtask() {
        boolean isContainSelf = false;
        Epic epic = new Epic("title", "description");
        tm.createEpic(epic);
        SubTask subTask = new SubTask("title", "description", TaskStatus.IN_PROGRESS, LocalDateTime.now(), Duration.ofMinutes(60), epic.getId());
        tm.createSubTask(subTask);
        for (SubTask sub : epic.getSubtasks()) {
            if (Objects.equals(sub.getId(), epic.getId())) {
                isContainSelf = true;
                break;
            }
        }

        assertFalse(isContainSelf, "Эпик не должен содержать себя в качестве подзадачи.");
    }

    @Test
    public void isOverlappingTest() {

        Task task1 = new Task("Task1", "Descr", TaskStatus.NEW,
                LocalDateTime.of(2025, 1, 1, 10, 0), Duration.ofHours(2));
        Task task2 = new Task("Task2", "Descr", TaskStatus.NEW,
                LocalDateTime.of(2025, 1, 1, 13, 0), Duration.ofHours(2));
        assertFalse(tm.isOverlapping(task1, task2));

        Task task3 = new Task("Task3", "Descr", TaskStatus.NEW,
                LocalDateTime.of(2025, 1, 1, 11, 0), Duration.ofMinutes(30));
        Task task4 = new Task("Task4", "Descr", TaskStatus.NEW,
                LocalDateTime.of(2025, 1, 1, 10, 0), Duration.ofHours(3));
        assertTrue(tm.isOverlapping(task3, task4));

        Task task5 = new Task("Task5", "Descr", TaskStatus.NEW,
                LocalDateTime.of(2025, 1, 1, 10, 0), Duration.ofHours(2));
        Task task6 = new Task("Task6", "Descr", TaskStatus.NEW,
                LocalDateTime.of(2025, 1, 1, 12, 0), Duration.ofHours(2));
        assertFalse(tm.isOverlapping(task5, task6));

        Task task7 = new Task("Task7", "Descr", TaskStatus.NEW);
        Task task8 = new Task("Task8", "Descr", TaskStatus.NEW);
        assertFalse(tm.isOverlapping(task7, task8));
    }

    @Test
    public void hasTimeOverlapTest() {

        Task existing = new Task("Existing", "Descr", TaskStatus.NEW,
                LocalDateTime.of(2025, 1, 1, 10, 0), Duration.ofHours(2));
        tm.createTask(existing);

        Task nonOverlapping = new Task("NonOverlap", "Descr", TaskStatus.NEW,
                LocalDateTime.of(2025, 1, 1, 13, 0), Duration.ofHours(1));
        assertFalse(tm.hasTimeOverlap(nonOverlapping));

        Task overlapping = new Task("Overlap", "Descr", TaskStatus.NEW,
                LocalDateTime.of(2025, 1, 1, 11, 0), Duration.ofHours(1));
        assertTrue(tm.hasTimeOverlap(overlapping));

        Task noTime = new Task("NoTime", "Descr", TaskStatus.NEW);
        assertFalse(tm.hasTimeOverlap(noTime));
    }

    @Test
    void addToPrioritizedTasksTest() {

        Task validTask = new Task("Valid", "Descr", TaskStatus.NEW,
                LocalDateTime.of(2025, 1, 1, 10, 0), Duration.ofHours(1));
        assertDoesNotThrow(() -> tm.addToPrioritizedTasks(validTask));
        assertEquals(1, tm.getPrioritizedTasks().size());

        Task overlapping = new Task("Overlap", "Descr", TaskStatus.NEW,
                LocalDateTime.of(2025, 1, 1, 10, 30), Duration.ofHours(1));
        ManagerSaveException exception = assertThrows(ManagerSaveException.class,
                () -> tm.addToPrioritizedTasks(overlapping));
        assertEquals("Задача с именем: 'Overlap' пересекается по времени с другой задачей.", exception.getMessage());
        assertEquals(1, tm.getPrioritizedTasks().size());

        Task noTime = new Task("NoTime", "Descr", TaskStatus.NEW);
        assertDoesNotThrow(() -> tm.addToPrioritizedTasks(noTime));
        assertEquals(1, tm.getPrioritizedTasks().size());
    }

}
