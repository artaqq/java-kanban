package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager tm = new FileBackedTaskManager(file);
        try {

            String fromFile = Files.readString(file.toPath());
            String[] lines = fromFile.split(System.lineSeparator());
            Long maxID = 1L;

            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                Task task = CSVFormatter.fromString(line);
                if (task.getId() > maxID) {
                    maxID = task.getId();
                }

                tm.generatorId = task.getId();
                if (task instanceof Epic) {
                    tm.createEpic((Epic) task);
                } else if (task instanceof SubTask) {
                    tm.createSubTask((SubTask) task);
                } else {
                    tm.createTask(task);
                }
            }

            tm.generatorId = maxID + 1;

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось загрузить задачи из файла");
        }

        return tm;
    }

    @Override
    public Task createTask(Task task) {
        Task createdTask = super.createTask(task);
        save();
        return createdTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic createdEpic = super.createEpic(epic);
        save();
        return createdEpic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask createdSubTask = super.createSubTask(subTask);
        save();
        return createdSubTask;
    }

    @Override
    public SubTask updateSubTask(SubTask subTask) {
        SubTask updatedSubTask = super.updateSubTask(subTask);
        save();
        return updatedSubTask;
    }

    @Override
    public Task updateTask(Task task) {
        Task updatedTask = super.updateTask(task);
        save();
        return updatedTask;
    }

    @Override
    public Epic updateEpic(Epic newEpic) {
        Epic updatedEpic = super.updateEpic(newEpic);
        save();
        return updatedEpic;
    }

    @Override
    public Task deleteTask(Long id) {
        Task deletedTask = super.deleteTask(id);
        save();
        return deletedTask;
    }

    @Override
    public Epic deleteEpic(Long id) {
        Epic deletedEpic = super.deleteEpic(id);
        save();
        return deletedEpic;
    }

    @Override
    public SubTask deleteSubTask(Long id) {
        SubTask deletedSubTask = super.deleteSubTask(id);
        save();
        return deletedSubTask;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    private void save() {
        try {
            if (Files.exists(file.toPath())) {
                Files.delete(file.toPath());
            }
            Files.createFile(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось найти файл для записи данных.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            //Пишем в файл
            writer.write(CSVFormatter.getHeader());
            writer.newLine();
            for (Epic epic : getEpics()) {
                writer.write(CSVFormatter.toString(epic));
                writer.newLine();
            }

            for (SubTask subtask : getSubtasks()) {
                writer.write(CSVFormatter.toString(subtask));
                writer.newLine();
            }

            for (Task task : getTasks()) {
                writer.write(CSVFormatter.toString(task));
                writer.newLine();
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи данных в файл");
        }
    }
}
