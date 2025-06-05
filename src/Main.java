import managers.HistoryManager;
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {
        TaskManager tm = Managers.getDefault();
        HistoryManager hm = Managers.getDefaultHistory();
        Task walking = new Task("Сходить на прогулку", "Дойти до парка Авиаторов и сделать круг", TaskStatus.NEW);
        Task shopping = new Task("Сходить в магазин", "Дойти до магазина и купить баскетбольный мяч", TaskStatus.IN_PROGRESS);
        tm.createTask(walking);
        tm.createTask(shopping);
        Epic moving = new Epic("Переезд", "Переезд в свою новую квартиру");
        tm.createEpic(moving);
        SubTask packing = new SubTask("Упакоука", "Упаковать вещи", TaskStatus.IN_PROGRESS, moving.getId());
        SubTask unpacking = new SubTask("Распакоука", "Распаковать вещи", TaskStatus.NEW, moving.getId());
        tm.createSubTask(packing);
        tm.createSubTask(unpacking);
        Epic traning = new Epic("Тренировка", "Список дел, чтобы начать тренироваться");
        tm.createEpic(traning);
        SubTask clothes = new SubTask("Одежда", "Купить одежду для тренировок", TaskStatus.IN_PROGRESS, traning.getId());
        tm.createSubTask(clothes);
        System.out.println("Создали таски");
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
        tm.deleteEpic(traning.getId());
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
