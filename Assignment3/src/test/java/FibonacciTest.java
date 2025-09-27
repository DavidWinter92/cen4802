import org.assignment3.Fibonacci;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class FibonacciTest {

    @Test
    void testBaseInt() {
        assertEquals(10, Fibonacci.base_int,
                "base_int should be 10");
    }

    @Test
    void testFibonacciOf4() {
        assertEquals(3, Fibonacci.fibonacci(4),
                "fibonacci(4) should be 3");
    }

    @Test
    void testFibonacciOf7() {
        assertEquals(13, Fibonacci.fibonacci(7),
                "fibonacci(7) should be 13");
    }

}
