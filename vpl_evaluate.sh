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




# cat common_script.sh > vpl_execution

output=$(java Evaluator)

# Print all but the last line to stdout
echo "$output" | awk 'NR > 1 { print prev } { prev = $0 }'

# Get the last line and write it as argument for an echo command to vpl_execution
last_line=$(echo "$output" | tail -n 1)

echo '#!/bin/bash' > vpl_execution
echo "echo \"$last_line\"" >> vpl_execution
chmod +x vpl_execution
#cat vpl_execution


#echo 'echo "Grade :=>> 99.5"' >> vpl_execution


# run java class to evaluate tests and output for students. Note: can only be seen in student role (change role to check) 
# java Evaluator | tee -a vpl_execution
