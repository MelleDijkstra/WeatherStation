import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This is an example test, use this as a template
 */
class ExampleTest {

    /**
     * Code that runs before a test
     */
    @BeforeEach
    void setUp() {
        System.out.println("This code runs before test");
    }

    /**
     * Code that runs after a test
     */
    @AfterEach
    void tearDown() {
        System.out.println("This code runs after test");
    }

    /**
     * Example test uses the @Test annotation
     */
    @Test
    void exampleTest() {
        System.out.println("This is the actual test");
    }

    /**
     * An example test, see link below for options inside a test and how to assert
     * @link http://junit.org/junit5/docs/current/user-guide/
     */
    @Test
    void timeTest() {
        System.out.println("This is an example test");
        System.out.println("Use http://junit.org/junit5/docs/current/user-guide/ for options" +
                "to complete the test");
        // example assert
        assertTrue("test".length() == 4, "Assertion messages can be lazily evaluated");
        try {
            // wait a second
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
