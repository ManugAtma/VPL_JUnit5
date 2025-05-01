import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MyTest {

    String s;

    @BeforeEach
    public void setUp(){
        s = "test";
        System.out.println("Hi from before each");
    }

    @Test
    public void testOne_2(){
        Assertions.assertTrue(true);
    }

    @Test
    public void testOTwo_6(){
       Assertions.assertEquals(1, 2);
    }
}
