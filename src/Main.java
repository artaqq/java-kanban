import managers.Managers;
import managers.HistoryManager;
import managers.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;


public class Main {

    public static void main(String[] args) {
        TaskManager tm = Managers.getDefault();
        HistoryManager hm = Managers.getDefaultHistory();
        Task walking = new Task("Сходить на прогулку", "Дойти до парка Авиаторов и сделать круг", TaskStatus.NEW, LocalDateTime.of(2025, 7, 30, 18, 48), Duration.ofMinutes(50));
        Task shopping = new Task("Сходить в магазин", "Дойти до магазина и купить баскетбольный мяч", TaskStatus.IN_PROGRESS, LocalDateTime.of(2024, 8, 30, 18, 48), Duration.ofMinutes(50));
        tm.createTask(walking);
        tm.createTask(shopping);
        Epic moving = new Epic("Переезд", "Переезд в свою новую квартиру");
        tm.createEpic(moving);
        SubTask packing = new SubTask("Упакоука", "Упаковать вещи", TaskStatus.IN_PROGRESS, LocalDateTime.of(2025, 7, 30, 16, 48), Duration.ofMinutes(59), moving.getId());
        SubTask unpacking = new SubTask("Распакоука", "Распаковать вещи", TaskStatus.NEW, LocalDateTime.of(2025, 9, 30, 16, 48), Duration.ofMinutes(60), moving.getId());
        tm.createSubTask(packing);
        tm.createSubTask(unpacking);
        Epic traning = new Epic("Тренировка", "Список дел, чтобы начать тренироваться");
        tm.createEpic(traning);
        SubTask clothes = new SubTask("Одежда", "Купить одежду для тренировок", TaskStatus.IN_PROGRESS, LocalDateTime.of(2025, 10, 30, 16, 48), Duration.ofMinutes(60), traning.getId());
        tm.createSubTask(clothes);
        System.out.println("Создали таски");
//        System.out.println("-".repeat(40));
//        tm.getTaskById(walking.getId());
//        tm.getEpicById(traning.getId());
//        tm.getTaskById(walking.getId());
//        tm.getTaskById(shopping.getId());
//        tm.getSubTaskById(packing.getId());
//        tm.getSubTaskById(unpacking.getId());
//        tm.getSubTaskById(clothes.getId());
//        tm.getEpicById(moving.getId());
//        tm.getEpicById(traning.getId());
//        System.out.println(tm.getHistory());
//        System.out.println("-".repeat(40));
//        tm.deleteAllEpics();
//        System.out.println("-".repeat(40));
//        System.out.println(tm.getHistory());
//        System.out.println("-".repeat(40));
//        System.out.println("-".repeat(40));
//        System.out.println(tm.getHistory());
//        System.out.println("-".repeat(40));
//        tm.deleteEpic(moving.getId());
//        System.out.println("-".repeat(40));
//        System.out.println(tm.getHistory());
//        System.out.println("-".repeat(40));
        printAllTasks(tm);
        unpacking.setStatus(TaskStatus.IN_PROGRESS);
        tm.updateSubTask(unpacking);
        clothes.setStatus(TaskStatus.DONE);
        tm.updateSubTask(clothes);
        walking.setStatus(TaskStatus.DONE);
        System.out.println("Изменили статусы");
        printAllTasks(tm);
        System.out.println("Удаляем задачи");
        packing.setTitle("Упаковка");
        packing.setStatus(TaskStatus.DONE);
        tm.updateSubTask(packing);
        tm.deleteSubTask(unpacking.getId());
        System.out.println("-".repeat(40));
        System.out.println(tm.getPrioritizedTasks());
        System.out.println("-".repeat(40));
        //tm.deleteEpic(traning.getId());
        //tm.deleteAllTasks();
        //tm.deleteAllEpics();
        //tm.deleteAllSubTasks();
        printAllTasks(tm);
    }

    public static void printAllTasks(TaskManager taskManager) {
        System.out.println("Epics:");
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println();
        System.out.println("SubTasks:");
        for (SubTask subTask : taskManager.getSubtasks()) {
            System.out.println(subTask);
        }
        System.out.println();
        System.out.println("Tasks:");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }


    }
}
