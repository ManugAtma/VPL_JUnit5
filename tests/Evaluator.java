import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import java.io.OutputStream;
import java.io.PrintStream;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

/**
 * This class evaluates the tests of one or multiple given test classes using JUnit5.
 * Its output should be passed to vpl_evaluate.sh which in turn should pass it on to vpl_execution.sh.
 * Since the output meets the syntax requested by vpl, vpl can then
 * use the generated tests results internally, e.g. display them to the students.
 *
 * Test method identifiers of given test classes must follow this syntax:
 * <name>_<x$y> Here "name" is the actual method name.
 * x$y represent a decimal number with "x" and "y" being integers. This decimal number must be > 0.
 * "x" represents the digits before the decimal point, y the ones after it. $ stands for the decimal point.
 * If this syntax is not met, a sysout will indicate the cause and the program will terminate prematurely.
 *
 * For each test method is output:
 * Comment :=>> testMethodName, SUCCESSFUL or FAILED, achieved points / max points.
 * E.g. if testOne_4&5() succeeds the output will be: Comment :=>> testOne SUCCESSFUL 4.5/4.5 points
 * "Comment :=>>" is part of the vpl syntax.
 *
 * At the end the total amount of achieved for this task is output, e.g. Grade :=>> 15.5
 * Again this follows vpl syntax.
 * To check the maximum total points for the given test classes, the teacher can temporarily uncomment the
 * sysouts at the end of the main method.
 * All standard output of the given test classes is suppressed so that students cannot see it.
 * This class requires class TestParser and JUnit5 (e.g. junit-platform-console-standalone-1.11.2.jar).
 */
public class Evaluator {

    public static void main(String[] args) {

        // launcher to execute tests
        Launcher launcher = LauncherFactory.create();

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                // insert test class here, possible to add multiple test classes like this
                // .selectors(selectClass(MyTest1.class), selectClass(MyTest2.class), ...)
                .selectors(selectClass(MyTest.class))
                .build();
        TestPlan plan = launcher.discover(request);

        // to suppress all output from test class
        PrintStream originalOut = System.out;

        float[] totalMaxPoints = {0}; // total max points for this student task
        float[] totalAchievedPoints = {0}; // total achieved points for this task
        
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
                        System.out.println( "Comment :=>> " +
                                formattedTest.getMethodName() + " "
                                + testExecutionResult.getStatus() + " "
                                + formattedTest.getAchievedPoints() + " / "
                                + formattedTest.getMaxPoints() + " points"
                                );

                        // to suppress output for test class again
                        System.setOut(new PrintStream(OutputStream.nullOutputStream()));

                        totalMaxPoints[0] += formattedTest.getMaxPoints();
                        totalAchievedPoints[0] += formattedTest.getAchievedPoints();

                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage() + " [abort]");
                        // exit after first malformed test
                        System.exit(1);
                    }
                }
            }
        });

        System.setOut(new PrintStream(OutputStream.nullOutputStream())); // to suppress output for test class again

        launcher.execute(plan); // run tests

        System.setOut(originalOut); // enable output again
        //System.out.println("<--------------------->");
        // System.out.println("Total: " + totalAchievedPoints[0] + "/" + totalMaxPoints[0] + " points");
        System.out.println("Grade :=>> " + totalAchievedPoints[0]); // this line is to be passed to vpl_execution.sh
    }
}