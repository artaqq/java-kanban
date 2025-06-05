package managers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagersTest {

    @Test
    public void getDefaultTest() {
        TaskManager taskManager = Managers.getDefault();

        assertNotNull(taskManager, "Метод getDefault() должен возвращать инициализированный и готовый к работе экземпляр класса InMemoryTaskManager");
    }

    @Test
    public void getDefaultHistoryTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(historyManager, "Метод getDefaultHistory() должен возвращать инициализированный и готовый к работе экземпляр класса InMemoryHistoryManager");
    }
}
