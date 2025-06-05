package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    //2. Методы для каждого из типов задач
    //a. Получение списка всех задач
    ArrayList<Task> getTasks();

    ArrayList<SubTask> getSubtasks();

    ArrayList<Epic> getEpics();

    ArrayList<SubTask> getEpicSubTasks(Epic epic);

    //b. Удаление всех задач
    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubTasks();

    //c. Получение задач по идентификатору
    Task getTaskById(Long id);

    SubTask getSubTaskById(Long id);

    Epic getEpicById(Long id);

    //d. Создание. Сам объект должен передаваться в качестве параметра
    Task createTask(Task task);

    Epic createEpic(Epic epic);

    SubTask createSubTask(SubTask subTask);

    //e. Обновление. Новая версия объекта с верным id передается в виде параметра
    Task updateTask(Task task);

    SubTask updateSubTask(SubTask subTask);

    Epic updateEpic(Epic newEpic);

    //f. Удаление по идентификатору
    Task deleteTask(Long id);

    Epic deleteEpic(Long id);

    SubTask deleteSubTask(Long id);

    List<Task> getHistory();
}
