package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> historyOfViewedTasks;

    public InMemoryHistoryManager() {
        historyOfViewedTasks = new ArrayList<>();
    }

    @Override
    public List<Task> getHistory() {
        return historyOfViewedTasks;
    }

    @Override
    public void add(Task task) {
        if (historyOfViewedTasks.size() < 10) {
            historyOfViewedTasks.add(task);
        } else {
            historyOfViewedTasks.removeFirst();
            historyOfViewedTasks.add(task);
        }
    }
}
