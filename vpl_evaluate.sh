#!/bin/bash
# Load common script and check programs

. common_script.sh
check_program javac
check_program java
get_source_files java


# Compile all .java files with all jars in root
export CLASSPATH=.:junit-platform-console-standalone-1.11.2.jar
javac -J-Xmx16m -Xlint:deprecation -cp "$CLASSPATH" *.java


if [ "$?" -ne "0" ]; then
  echo "Not compiled"
  exit 0
fi


# run Evaluator and save its output to output variable
output=$(java Evaluator)

# create vpl_execution script
echo '#!/bin/bash' > vpl_execution

# append Evaluator output to execution script and make it echo the output
echo "echo \"$output\"" >> vpl_execution

# make execution script executable
chmod +x vpl_execution
