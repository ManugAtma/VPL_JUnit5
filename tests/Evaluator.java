import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import java.io.OutputStream;
import java.io.PrintStream;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

/**
 * This class evaluates the tests of one or multiple given test classes. Test method identifiers must follow this syntax:
 * <name>_<integer number of points> Otherwise unexpected behaviour might occur. For each test method is displayed:
 * name without number of points, SUCCESSFUL or FAILED, achieved points / max points.
 * At the end the total amounts of achieved and maximum points for this task are displayed.
 * All standard output of the test class is suppressed so that students cannot see it. This class requires class TestParser.
 */
public class Evaluator {

    public static void main(String[] args) {

        // launcher to execute tests
        Launcher launcher = LauncherFactory.create();

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                // insert test class here, possible to add multiple test classes like this
                // .selectors(selectClass(MyTest1.class), selectClass(MyTest2.class))
                .selectors(selectClass(MyTest.class), selectClass(MyTest2.class))
                .build();
        TestPlan plan = launcher.discover(request);

        // to suppress all output from test class
        PrintStream originalOut = System.out;

        int[] totalMaxPoints = {0}; // total max points for this student task
        int[] totalAchievedPoints = {0}; // total achieved points for this task
        
        launcher.registerTestExecutionListeners(new TestExecutionListener() {
            @Override
            public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
                TestExecutionListener.super.executionFinished(testIdentifier, testExecutionResult);

                if (testIdentifier.isTest()) {
                    TestParser testParser = new TestParser();
                    // temporarily enable output to display test results from this class
                    System.setOut(originalOut);
                    try {
                        TestParser.TestParserResult formattedTest = testParser.parse(testIdentifier, testExecutionResult);
                        System.out.println(formattedTest.getMethodName() + " "
                                + testExecutionResult.getStatus() + " "
                                + formattedTest.getAchievedPoints() + "/"
                                + formattedTest.getMaxPoints() + " points"
                                );
                        // to suppress output for test class again
                        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
                        totalMaxPoints[0] += formattedTest.getMaxPoints();
                        totalAchievedPoints[0] += formattedTest.getAchievedPoints();
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage() + testIdentifier.getDisplayName() + " [abort]");
                        // exit after first malformed test
                        System.exit(1);
                    }
                }
            }
        });

        System.setOut(new PrintStream(OutputStream.nullOutputStream())); // to suppress output for test class again

        launcher.execute(plan); // run tests

        System.setOut(originalOut); // enable output again
        System.out.println("<--------------------->");
        System.out.println("Total: " + totalAchievedPoints[0] + "/" + totalMaxPoints[0] + " points");
        System.out.println("Grade :=>> " + totalAchievedPoints[0]);
    }
}