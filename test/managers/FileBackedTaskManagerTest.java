package managers;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {

    private Path tempFile;
    private FileBackedTaskManager tm;

    @BeforeEach
    public void setUp() {
        try {
            tempFile = Files.createTempFile("FBTMTest", ".txt");
            tm = new FileBackedTaskManager(tempFile.toFile());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка создания временного файла для теста.");
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            Files.deleteIfExists(tempFile);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка удаления временного файла для теста.");
        }
    }

    @Test
    public void saveAndLoadEmptyFileTest() {
        tm.save();

        FileBackedTaskManager loadedFromFile = FileBackedTaskManager.loadFromFile(tempFile.toFile());

        assertTrue(loadedFromFile.getTasks().isEmpty());
        assertTrue(loadedFromFile.getEpics().isEmpty());
        assertTrue(loadedFromFile.getSubtasks().isEmpty());
    }

    @Test
    public void saveAndLoadFileWithTasksTest() {
        Task task = new Task("Shopping", "Shopping in shopping centre", TaskStatus.NEW);
        Epic epic = new Epic("Epic", "EpicDescription");
        tm.createTask(task);
        tm.createEpic(epic);

        SubTask subTask = new SubTask("SubTask", "SubTusk Description", TaskStatus.NEW, epic.getId());
        tm.createSubTask(subTask);

        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile.toFile());

        assertEquals(1, loaded.getTasks().size());
        assertEquals(1, loaded.getEpics().size());
        assertEquals(1, loaded.getSubtasks().size());

        Task loadedTask = loaded.getTaskById(task.getId());
        Epic loadedEpic = loaded.getEpicById(epic.getId());
        SubTask loadedSubTask = loaded.getSubTaskById(subTask.getId());

        assertEquals(task, loadedTask);
        assertEquals(epic, loadedEpic);
        assertEquals(subTask, loadedSubTask);
    }
}
