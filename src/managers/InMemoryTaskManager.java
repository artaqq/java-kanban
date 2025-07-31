package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    //1. Хранение задач всех типов
    protected HashMap<Long, Task> tasks = new HashMap<>();
    protected HashMap<Long, Epic> epics = new HashMap<>();
    protected HashMap<Long, SubTask> subtasks = new HashMap<>();
    protected long generatorId = 1;
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    //2. Методы для каждого из типов задач
    //a. Получение списка всех задач
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<SubTask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getEpicSubTasks(Epic epic) {
        return epic.getSubtasks();
    }

    //b. Удаление всех задач
    @Override
    public void deleteAllTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
            if (prioritizedTasks.contains(task)) {
                prioritizedTasks.remove(task);
            }
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (SubTask subTask : subtasks.values()) {
            historyManager.remove(subTask.getId());
            if (prioritizedTasks.contains(subTask)) {
                prioritizedTasks.remove(subTask);
            }
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
        }
        epics.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (SubTask subTask : new ArrayList<>(subtasks.values())) {
            historyManager.remove(subTask.getId());
            Epic epic = epics.get(subTask.getEpicId());
            subtasks.remove(subTask.getId());
            epic.removeSubTask(subTask);
            updateEpicStatus(epic);
            updateEpicTime(epic);
            if (prioritizedTasks.contains(subTask)) {
                prioritizedTasks.remove(subTask);
            }
        }
    }

    //c. Получение задач по идентификатору
    @Override
    public Task getTaskById(Long id) {
        Task task = tasks.get(id);
        addHistory(task);
        return task;
    }

    @Override
    public SubTask getSubTaskById(Long id) {
        SubTask subTask = subtasks.get(id);
        addHistory(subTask);
        return subTask;
    }

    @Override
    public Epic getEpicById(Long id) {
        Epic epic = epics.get(id);
        addHistory(epic);
        return epic;
    }

    //d. Создание. Сам объект должен передаваться в качестве параметра
    @Override
    public Task createTask(Task task) {
        task.setId(getNextId());
        tasks.put(task.getId(), task);
        addToPrioritizedTasks(task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(getNextId());
        subtasks.put(subTask.getId(), subTask);
        addToPrioritizedTasks(subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTask(subTask);
        updateEpicStatus(epic);
        updateEpicTime(epic);
        return subTask;
    }

    //e. Обновление. Новая версия объекта с верным id передается в виде параметра
    @Override
    public Task updateTask(Task task) {
        tasks.put(task.getId(), task);
        prioritizedTasks.remove(task);
        addToPrioritizedTasks(task);
        return task;
    }

    @Override
    public SubTask updateSubTask(SubTask subTask) {
        subtasks.put(subTask.getId(), subTask);
        prioritizedTasks.remove(subTask);
        addToPrioritizedTasks(subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.removeSubTask(subTask);
        epic.addSubTask(subTask);
        updateEpicStatus(epic);
        updateEpicTime(epic);
        return subTask;
    }

    @Override
    public Epic updateEpic(Epic newEpic) {
        Epic epic = epics.get(newEpic.getId());
        epic.setTitle(newEpic.getTitle());
        epic.setDescription(newEpic.getDescription());
        return epic;
    }


    //f. Удаление по идентификатору
    @Override
    public Task deleteTask(Long id) {
        historyManager.remove(id);
        if (prioritizedTasks.contains(getTaskById(id))) {
            prioritizedTasks.remove(getTaskById(id));
        }
        return tasks.remove(id);
    }

    @Override
    public Epic deleteEpic(Long id) {
        Epic epic = epics.get(id);
        ArrayList<SubTask> epicSubTasks = epic.getSubtasks();
        for (SubTask epicSubTask : epicSubTasks) {
            historyManager.remove(epicSubTask.getId());
            if (prioritizedTasks.contains(epicSubTask)) {
                prioritizedTasks.remove(epicSubTask);
            }
            subtasks.remove(epicSubTask.getId());
        }
        historyManager.remove(id);
        return epics.remove(id);
    }

    @Override
    public SubTask deleteSubTask(Long id) {
        historyManager.remove(id);
        if (prioritizedTasks.contains(getSubTaskById(id))) {
            prioritizedTasks.remove(getSubTaskById(id));
        }
        SubTask deletedSubTask = subtasks.remove(id);
        Epic epic = epics.get(deletedSubTask.getEpicId());
        epic.removeSubTask(deletedSubTask);
        updateEpicStatus(epic);
        updateEpicTime(epic);
        return deletedSubTask;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    public boolean hasTimeOverlap(Task task) {
        return prioritizedTasks.stream().anyMatch(existingTask -> isOverlapping(task, existingTask));
    }

    protected void addToPrioritizedTasks(Task task) {
        if (task.getStartTime() != null) {
            if (hasTimeOverlap(task)) {
                throw new ManagerSaveException("Задача с именем: '" + task.getTitle() + "' пересекается по времени с другой задачей.");
            }
            prioritizedTasks.add(task);
        }
    }

    protected boolean isOverlapping(Task task1, Task task2) {
        if (task1.getStartTime() == null || task1.getEndTime() == null || task2.getStartTime() == null || task2.getEndTime() == null) {
            return false;
        }

        return (task1.getStartTime().isBefore(task2.getEndTime()) && task2.getStartTime().isBefore(task1.getEndTime()));
    }

    private void addHistory(Task task) {
        historyManager.add(task);
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

    private void updateEpicTime(Epic epic) {
        epic.setStartTime(epic.calculateEpicStartTime());
        epic.setDuration(epic.calculateEpicDuration());
        epic.setEndTime(epic.calculateEpicEndTime());
    }

    private long getNextId() {
        return generatorId++;
    }
}