package hexlet.code;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {

    @Test
    void mainTest() {
        var actual = Main.hello();
        var expected = "Hello world!";
        assertEquals(expected, actual);
    }
}
