#!/bin/bash

MAVEN_SKIPS="-Drat.skip=true -Dmaven.javadoc.skip=true \
-Djacoco.skip=true -Dcheckstyle.skip=true \
-Dfindbugs.skip=true -Dcobertura.skip=true \
-Dpmd.skip=true -Dcpd.skip=true -DfailIfNoTests=false"

mvn clean
mvn -Dtest=org.apache.accumulo.master.replication.MasterReplicationCoordinatorTest#randomServer,org.apache.accumulo.master.replication.MasterReplicationCoordinatorTest#randomServerFromMany $MAVEN_SKIPS test

########################################################################
# Parse maven output files to extract a list of test cases to run. This does not necessarily corresponds to the maven order, but it's a possible order nevertheless

find target -iname "TEST*.xml" -type f | \
while read -r TEST_FILE
do

# TODO CHECK THE SCOPE OF THE LOOP AND VARIABLES HERE
cat ${TEST_FILE} | \
grep "testcase" | grep "time" | grep "name" | tr "<" " " | tr ">" " " | tr " " "\n" | \
while read -r LINE; do

#echo "--- ${LINE}"

if [ "${LINE}" == "testcase" ]; then
#    (>&2 echo "New Test CASE")
    CLASS= # Reset
    METHOD= # Reset
elif [[ ${LINE} == classname* ]]; then
    CLASS=`echo ${LINE} | cut -d= -f2 | sed 's|"||g'`
#    (>&2 echo "CLASS ${CLASS}")
elif [[ ${LINE} == name* ]]; then
    METHOD=`echo ${LINE} | cut -d= -f2 | sed 's|"||g'`
#    (>&2 echo "METHOD ${METHOD}")
fi

if [ ! -z "${CLASS}" -a ! -z "${METHOD}" ]; then
    echo "${CLASS}.${METHOD}"
    CLASS= # Reset
    METHOD= # Reset
fi

done

done | awk '!seen[$0]++' > maven_test_execution_order
