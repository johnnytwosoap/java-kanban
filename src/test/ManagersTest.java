package test;

import org.junit.jupiter.api.Test;
import ru.practicum.tasks.service.*;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void checkDefault() {
        Managers managers = new Managers();
        TaskManager taskManager = new InMemoryTaskManager();
        HistoryManager historyManager = new InMemoryHistoryManager();
        assertInstanceOf(taskManager.getClass(), managers.getDefault());
        assertInstanceOf(historyManager.getClass(), managers.getDefaultHistory());
    }
}