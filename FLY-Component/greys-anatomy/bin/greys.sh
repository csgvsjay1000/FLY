
GREYS_HOME="$( cd "$( dirname "$0" )" && pwd )"

GREYS_LIB_DIR=${GREYS_HOME}/../lib

# exit shell with err_code
# $1 : err_code
# $2 : err_msg
exit_on_err()
{
    [[ ! -z "${2}" ]] && echo "${2}" 1>&2
    exit ${1}
}



# parse the argument
parse_arguments()
{

     TARGET_PID=$(echo ${1}|awk '/^[0-9]*@/;/^[^@]+:/{print "@"$1};/^[0-9]+$/'|awk -F "@"   '{print $1}');
      TARGET_IP=$(echo ${1}|awk '/^[0-9]*@/;/^[^@]+:/{print "@"$1};/^[0-9]+$/'|awk -F "@|:" '{print $2}');
    TARGET_PORT=$(echo ${1}|awk '/^[0-9]*@/;/^[^@]+:/{print "@"$1};/^[0-9]+$/'|awk -F ":"   '{print $2}');

    # check pid
    if [ -z ${TARGET_PID} ];then
        # echo "illegal arguments, the <PID> is required." 1>&2
        # return 1
        OPTION_ATTACH_JVM=0
    fi

    return 0

}

# the usage
usage()
{
    echo "
greys usage:
    the format was <PID>[@IP:PORT]
     <PID> : the target Java Process ID
      [IP] : the target's IP
    [PORT] : the target's PORT

example:
    ./greys.sh <PID>
    ./greys.sh <PID>@[IP]
    ./greys.sh <PID>@[IP:PORT]
"
}

attach_jvm()
{
    local greys_lib_dir=${GREYS_LIB_DIR}
	BOOT_CLASSPATH=-Xbootclasspath/a:${greys_lib_dir}/tools.jar
    java \
            ${BOOT_CLASSPATH} ${JVM_OPTS} \
            -jar ${greys_lib_dir}/greys-core.jar \
                -pid ${TARGET_PID} \
                -core "${greys_lib_dir}/greys-core.jar" \
                -agent "${greys_lib_dir}/greys-agent.jar"
}

attach_console()
{
    local greys_lib_dir=${GREYS_LIB_DIR}

    # use default console
    java \
        -cp ${greys_lib_dir}/greys-core.jar \
        ivg.cn.core.GreysConsole1 \
            127.0.0.1 \
            6530
}

# the main
main()
{
## 参数校验
	parse_arguments "${@}" \
        || exit_on_err 1 "$(usage)"
## 侵入目标JVM
	attach_jvm \
            || exit_on_err 1 "attach to target jvm(${TARGET_PID}) failed."
## 开启一个客户端进行命令交互
	attach_console \
            || exit_on_err 1 "active console failed."
}

main "${@}"