import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestIdentifier;

public class TestParser {

    /**
     * This method parses a test method identifier by extracting the achieved
     * and max points for this test as floats as well as the name up to the underscore.
     *
     * @param test a test represented by a JUnit5 TestIdentifier object
     * @param result the same test method represented by a JUnit5 TestExecutionResult object
     * @return a TestParserResult object containing the achieved and max points as integers
     * as well as the test method name up to the underscore
     * @throws IllegalStateException when parsed method identifier doesn't meet the required format
     *                               <name>_<x$y>
     *                               See class Evaluator for details regrading the required syntax.
     *
     */
    TestParserResult parse(TestIdentifier test, TestExecutionResult result) throws IllegalStateException {

        TestParserResult testParserResult = new TestParserResult();

        // get method name including ()
        String methodName = test.getDisplayName();

        // get indices of "_", "(" and "$"
        int underscore = methodName.indexOf("_");
        if (underscore < 0) throw new IllegalStateException("An die Dozent*in: missing underscore '_' in method " + methodName);
        testParserResult.methodName = methodName.substring(0, underscore);

        int parentheses = methodName.indexOf("(");

        int delimiter$ = methodName.indexOf("$");
        if (delimiter$ < 0) throw new IllegalStateException("An die Dozent*in: missing delimiter '$' in method " + methodName);

        String beforeDecimalPoint = methodName.substring(underscore + 1, delimiter$);
        String afterDecimalPoint = methodName.substring(delimiter$ + 1, parentheses);

        try {
            if (beforeDecimalPoint.isEmpty() || afterDecimalPoint.isEmpty()) throw new NumberFormatException();
            testParserResult.maxPoints = Float.parseFloat(beforeDecimalPoint + "." + afterDecimalPoint);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("An die Dozent*in: points must follow syntax x$y with x,y being an int > 0 in method " + methodName);
        }

        if (testParserResult.getMaxPoints() == 0)
            throw new IllegalStateException("An die Dozent*in: missing points in method " + methodName);

        if (result.getStatus().equals(TestExecutionResult.Status.SUCCESSFUL))
            testParserResult.achievedPoints = testParserResult.maxPoints;

        return testParserResult;
    }

    static class TestParserResult {
        private float maxPoints;
        private float achievedPoints;
        private String methodName;

        public float getMaxPoints() {
            return maxPoints;
        }

        public float getAchievedPoints() {
            return achievedPoints;
        }

        public String getMethodName() {
            return methodName;
        }
    }
}