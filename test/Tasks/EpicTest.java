package Tasks;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {
    @Test
    public void EpicsShouldBeEqualsIfHasEqualId() {
        Epic epic1 = new Epic("title1", "description1");
        Epic epic2 = new Epic("title1", "description1");
        epic1.setId(1L);
        epic2.setId(epic1.getId());
        assertEquals(epic1, epic2);
    }
}
