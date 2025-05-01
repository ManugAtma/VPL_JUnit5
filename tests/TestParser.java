import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestIdentifier;

public class TestParser {

    /**
     * Parses a test by extracting the achieved and max points as integers
     * as well as the name until the underscore
     * @param test a test represented by a TestIdentifier object
     * @param result the same test method reoresented by a TestExecutionResult object
     * @return a TestParserResult object containing the achieved and max points as integers
     *          as well as the name until the underscore
     * @throws IllegalStateException when parsed method identifier doesn't meet the required format
     *          <name>_<integer number of points>
     */
    TestParserResult parse(TestIdentifier test, TestExecutionResult result) throws IllegalStateException {

        TestParserResult testParserResult = new TestParserResult();

        String s = test.getDisplayName();

        int underscore = s.indexOf("_");
        if (underscore < 0) throw new IllegalStateException("An die Dozent*in: missing underscore in method ");
        testParserResult.methodName = s.substring(0, underscore);


        int parentheses = s.indexOf("(");
        testParserResult.maxPoints = Integer.parseInt(s.substring(underscore + 1, parentheses));
        if (testParserResult.getMaxPoints() == 0) throw new IllegalStateException("An die Dozent*in: missing points in method ");
        if (result.getStatus().equals(TestExecutionResult.Status.SUCCESSFUL)) testParserResult.achievedPoints = testParserResult.maxPoints;

        return testParserResult;
    }


    static class TestParserResult {
        private int maxPoints;
        private int achievedPoints;
        private String methodName;

        public int getMaxPoints(){
            return maxPoints;
        }

        public int getAchievedPoints(){
            return achievedPoints;
        }

        public String getMethodName() {
            return methodName;
        }
    }
}