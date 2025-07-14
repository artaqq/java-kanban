package managers;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTaskManager(new File("C:\\Intellij IDEA\\java-kanban\\resources\\data.txt"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
