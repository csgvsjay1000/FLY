#!/bin/bash


BASEDIR="$( cd "$( dirname "$0" )" && pwd )"

echo "BASEDIR=$BASEDIR"
# greys's target dir
GREYS_TARGET_DIR=${BASEDIR}/../build

# exit shell with err_code
# $1 : err_code
# $2 : err_msg
exit_on_err()
{
    [[ ! -z "${2}" ]] && echo "${2}" 1>&2
    exit ${1}
}

# maven package the greys
/d/java/soft/apache-maven-3.3.9/bin/mvn clean package -Dmaven.test.skip=true -f ${BASEDIR}/../pom.xml \
|| exit_on_err 1 "package greys failed."

# reset the target dir
mkdir -p ${GREYS_TARGET_DIR}
mkdir -p ${GREYS_TARGET_DIR}/bin
mkdir -p ${GREYS_TARGET_DIR}/lib

# copy jar to TARGET_DIR
cp ${BASEDIR}/../core/target/greys-core-jar-with-dependencies.jar ${GREYS_TARGET_DIR}/lib/greys-core.jar
cp ${BASEDIR}/../agent/target/greys-agent-jar-with-dependencies.jar ${GREYS_TARGET_DIR}/lib/greys-agent.jar

# copy shell to TARGET_DIR
cp ${BASEDIR}/greys.sh ${GREYS_TARGET_DIR}/bin/greys.sh
chmod +x ${GREYS_TARGET_DIR}/bin/*.sh

