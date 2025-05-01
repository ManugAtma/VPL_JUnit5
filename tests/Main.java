import org.junit.platform.engine.TestExecutionResult;

public class Main {



    public static void main(String[] args) {
       String s = "Hallo";
        s = s.substring(2, 2);
        System.out.println(s);
        System.out.println(TestExecutionResult.Status.SUCCESSFUL);
    }
}