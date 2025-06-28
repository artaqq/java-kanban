package managers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    private static TaskManager tm;

    @BeforeEach
    public void beforeEach() {
        tm = Managers.getDefault();
    }


    @Test
    public void createTaskTest() {
        Task task = tm.createTask(new Task("CreateTaskTest", "CreateTaskTestDesc", TaskStatus.IN_PROGRESS));
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
        SubTask subTask1 = tm.createSubTask(new SubTask("CreateEpicTestSub1", "CreateEpicTestSubDesc1", TaskStatus.IN_PROGRESS, epic.getId()));
        SubTask subTaskAfterSave1 = tm.getSubTaskById(subTask1.getId());
        SubTask subTask2 = tm.createSubTask(new SubTask("CreateEpicTestSub2", "CreateEpicTestSubDesc2", TaskStatus.IN_PROGRESS, epic.getId()));
        assertNotNull(subTaskAfterSave1, "Подзадача не найдена");
        assertEquals(subTask1, subTaskAfterSave1, "Подзадачи с одинаковым Id не совпадают.");

        ArrayList<SubTask> subTasks = tm.getSubtasks();

        assertNotNull(subTasks, "Не возвращается список подзадач.");
        assertEquals(2, subTasks.size());
        assertEquals(subTask2, subTasks.get(1));
    }

    @Test
    public void updateTaskTest() {
        Task task = tm.createTask(new Task("title", "description", TaskStatus.NEW));
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
        SubTask subTask = new SubTask("SubTitle", "SubDescription", TaskStatus.NEW, epic.getId());
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
        Task task = tm.createTask(new Task("Title", "Description", TaskStatus.IN_PROGRESS));
        int count = tm.getTasks().size();
        tm.deleteTask(task.getId());

        assertEquals(1, count, "Задача не была добавлена в список задач.");
        assertNull(tm.getTaskById(task.getId()), "Задача не была удалена из списка задач.");
    }

    @Test
    public void deleteSubTaskByIdTest() {
        Epic epic = tm.createEpic(new Epic("EpicTitle", "EpicDescription"));
        SubTask subTask = tm.createSubTask(new SubTask("SubTitle", "SubDescription", TaskStatus.IN_PROGRESS, epic.getId()));
        int count = tm.getSubtasks().size();
        tm.deleteSubTask(subTask.getId());

        assertEquals(1, count, "Подзадача не была добавлена в список подзадач");
        assertNull(tm.getSubTaskById(subTask.getId()), "Подзадача не была удалена из списка подзадач.");
    }

    @Test
    public void deleteEpicByIdTest() {
        Epic epic = tm.createEpic(new Epic("EpicTitle", "EpicDescription"));
        SubTask subTask = tm.createSubTask(new SubTask("SubTitle", "SubDescription", TaskStatus.IN_PROGRESS, epic.getId()));
        int epicCount = tm.getEpics().size();
        int subTaskCount = tm.getSubtasks().size();
        tm.deleteEpic(epic.getId());

        assertEquals(1, epicCount, "Эпик не был добавлен в список эпиков.");
        assertEquals(1, subTaskCount, "Подзадача не была добавлена в список подзадач.");
        assertNull(tm.getEpicById(epic.getId()), "Эпик не был удален из списка эпиков.");
        assertNull(tm.getSubTaskById(subTask.getId()), "При удалении эпика должны быть удалены все его подзадачи.");
    }

    @Test
    public void updateEpicStatusTest() {
        Epic epic = tm.createEpic(new Epic("EpicTitle", "EpicDescription"));
        SubTask subTask1 = tm.createSubTask(new SubTask("SubTask1Title", "SubTask1Description", TaskStatus.IN_PROGRESS, epic.getId()));
        SubTask subTask2 = tm.createSubTask(new SubTask("SubTask2Title", "SubTask2Description", TaskStatus.DONE, epic.getId()));
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
        SubTask subTask = new SubTask("title", "description", TaskStatus.IN_PROGRESS, epic.getId());
        tm.createSubTask(subTask);
        for (SubTask sub : epic.getSubtasks()) {
            if (Objects.equals(sub.getId(), epic.getId())) {
                isContainSelf = true;
                break;
            }
        }

        assertFalse(isContainSelf, "Эпик не должен содержать себя в качестве подзадачи.");
    }


}
