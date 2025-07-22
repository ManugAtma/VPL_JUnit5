#!/bin/bash

# note: vpl_run.sh must not be empty!
# otherwise, if students press the "run" button and
# do not submit a java file with main method, by default vpl will run all present java files.
# Since that includes Evaluator.java, the test results are shown to the students
# without counting it as evaluation attempt.


# compile and run Main if present.
if [ -f Main.java ]; then
  javac Main.java && java Main
fi

