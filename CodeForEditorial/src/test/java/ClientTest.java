import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testClientClassExists() {
        try {
            Class.forName("Client");
        } catch (ClassNotFoundException e) {
            fail("Client class not found");
        }
    }

    @Test
    public void testAdderClassExists() {
        try {
            Class.forName("Adder");
        } catch (ClassNotFoundException e) {
            fail("Adder class not found");
        }
    }

    @Test
    public void testSubtractorClassExists() {
        try {
            Class.forName("Subtractor");
        } catch (ClassNotFoundException e) {
            fail("Subtractor class not found");
        }
    }

    @Test
    public void testClientMainMethod() throws ClassNotFoundException {
        Class<?> clientClass = Class.forName("Client");
        boolean hasMainMethod = false;
        for (java.lang.reflect.Method method : clientClass.getDeclaredMethods()) {
            if (method.getName().equals("main")) {
                hasMainMethod = true;
                break;
            }
        }
        assertTrue(hasMainMethod);
    }

    @Test
    public void testAdderRunnableInterface() throws Exception{
        assertTrue(Runnable.class.isAssignableFrom(Class.forName("Adder")));
    }

    @Test
    public void testSubtractorRunnableInterface() throws Exception{
        assertTrue(Runnable.class.isAssignableFrom(Class.forName("Subtractor")));
    }

    @Test
    public void testAdderOutput() {
        try {
            Class<?> adderClass = Class.forName("Adder");
            Object adderInstance = adderClass.getDeclaredConstructor().newInstance();
            java.lang.reflect.Method runMethod = adderClass.getDeclaredMethod("run");
            runMethod.invoke(adderInstance);
            assertEquals("I am the Adder class\n", outContent.toString());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSubtractorOutput() {
        try {
            Class<?> subtractorClass = Class.forName("Subtractor");
            Object subtractorInstance = subtractorClass.getDeclaredConstructor().newInstance();
            java.lang.reflect.Method runMethod = subtractorClass.getDeclaredMethod("run");
            runMethod.invoke(subtractorInstance);
            assertEquals("I am the Subtractor class\n", outContent.toString());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAdderAndSubtractorThreads() throws InterruptedException {
        try {
            Method mainMethod = Class.forName("Client").getDeclaredMethod("main", String[].class);
            mainMethod.invoke(null, (Object)new String[0]);
            HashMap<String, String> map = ScalerThread.map;

            String adderThreadName = null;
            String subtractorThreadName = null;

            if(!map.containsKey("Adder")){
                fail("Adder class not invoked");
            }
            adderThreadName = map.get("Adder");

            if(!map.containsKey("Subtractor")){
                fail("Subtractor class not invoked");
            }
            subtractorThreadName = map.get("Subtractor");

            assertNotEquals(adderThreadName, subtractorThreadName, "Adder and Subtractor should be invoked on separate threads");
            assertNotEquals(adderThreadName, Thread.currentThread().getName(), "Adder should be invoked on separate thread");
            assertNotEquals(subtractorThreadName, Thread.currentThread().getName(), "Subtractor should be invoked on separate thread");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}