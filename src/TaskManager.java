import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    //1. Хранение задач всех типов
    private HashMap<Long, Task> tasks = new HashMap<>();
    private HashMap<Long, Epic> epics = new HashMap<>();
    private HashMap<Long, SubTask> subtasks = new HashMap<>();
    private long generatorId = 1;

    private long getNextId() {
        return generatorId++;
    }

    //2. Методы для каждого из типов задач
    //a. Получение списка всех задач
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    //b. Удаление всех задач
    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    public void deleteAllSubTasks() {
        subtasks.clear();
    }

    //c. Получение задач по идентификатору
    public Task getTaskById(Long id) {
        return tasks.get(id);
    }

    public SubTask getSubTaskById(Long id) {
        return subtasks.get(id);
    }

    public Epic getEpicById(Long id) {
        return epics.get(id);
    }

    //d. Создание. Сам объект должен передаваться в качестве параметра
    public Task createTask(Task task) {
        task.setId(getNextId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(getNextId());
        subtasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTask(subTask);
        updateEpicStatus(epic);
        return subTask;
    }

    //e. Обновление. Новая версия объекта с верным id передается в виде параметра
    public Task updateTask(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    public SubTask updateSubTask(SubTask subTask) {
        subtasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.removeSubTask(subTask);
        epic.addSubTask(subTask);
        updateEpicStatus(epic);
        return subTask;
    }

    public Epic updateEpic(Epic newEpic) {
        Epic epic = epics.get(newEpic.getId());
        epic.setTitle(newEpic.getTitle());
        epic.setDescription(newEpic.getDescription());
        return epic;
    }


    //f. Удаление по идентификатору
    public Task deleteTask(Long id) {
        return tasks.remove(id);
    }

    public Epic deleteEpic(Long id) {
        Epic epic = epics.get(id);
        ArrayList<SubTask> epicSubTasks = epic.getSubtasks();
        for (SubTask epicSubTask : epicSubTasks) {
            subtasks.remove(epicSubTask.getId());
        }
        return epics.remove(id);
    }

    public SubTask deleteSubTask(Long id) {
        SubTask deletedSubTask = subtasks.remove(id);
        Epic epic = epics.get(deletedSubTask.getEpicId());
        epic.removeSubTask(deletedSubTask);
        updateEpicStatus(epic);
        return deletedSubTask;
    }


    //3. Управление статусами
    private void updateEpicStatus(Epic epic) {
        boolean allDone = true;
        boolean allNew = true;
        for (SubTask subTask : epic.getSubtasks()) {
            if (subTask.getStatus() != TaskStatus.NEW) allNew = false;
            if (subTask.getStatus() != TaskStatus.DONE) allDone = false;
        }
        if (allNew) {
            epic.setStatus(TaskStatus.NEW);
        } else if (allDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}