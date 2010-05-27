#! /bin/sh

# Look for environment variables
if [ -z "${MAGNET_HOME}" ] ; then
    echo "ERROR: The variable MAGNET_HOME is not define; cannot start."
    echo "       Please set your MAGNET_HOME environment variable."
    exit 1
fi

IS_LOOP_DONE=false
JAVA_VM_ARGS="-Dmagnet.home=${MAGNET_HOME}"
while [ "${IS_LOOP_DONE}" = "false" ] ; do
    IS_LOOP_DONE=true

    #Define the java home
    if [ "'OPTION'$1" = "'OPTION'-javahome" ] ; then
        IS_LOOP_DONE=false
        JAVA_HOME_OVERRIDE=$2
        shift 2
    fi

    # Define the java VM type that will be used
    if [ "'VM'$1" = "'VM'-client" -o "'VM'$1" = "'VM'-server" ] ; then
        IS_LOOP_DONE=false
        JAVA_VM_TYPE=$1
        shift 1
    fi

    # Define the java X non-standard options passed
    if [ -n "`echo "$1" | grep -e -X.*`" ] ; then
        IS_LOOP_DONE=false
        JAVA_X_OPTIONS="${JAVA_X_OPTIONS} $1"
        shift 1
    fi

    # Define the java VM arguments passed
    if [ -n "`echo "$1" | grep -e -D.*`" ] ; then
        IS_LOOP_DONE=false
        JAVA_VM_ARGS="${JAVA_VM_ARGS} $1"
        shift 1
    fi
done

# Look for JAVA HOME environment variables
if [ -z "${JAVA_HOME}${JAVA_HOME_OVERRIDE}" ] ; then
    echo "ERROR: The variable JAVA_HOME is not define; cannot start."
    echo "       Please set your JAVA_HOME environment variable or use the -javahome parameter."
    exit 1
fi

# OS specific support.  $var _must_ be set to either true or false.
cygwin=false;
darwin=false;
case "`uname`" in
    CYGWIN*) cygwin=true ;;
    Darwin*) darwin=true ;;
esac


# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
    [ -n "${MAGNET_HOME}" ] &&
        MAGNET_HOME=`cygpath --unix "${MAGNET_HOME}"`
    [ -n "${JAVA_HOME}" ] &&
        JAVA_HOME=`cygpath --unix "${JAVA_HOME}"`
    [ -n "${JAVA_HOME_OVERRIDE}" ] &&
        JAVA_HOME_OVERRIDE=`cygpath --unix "${JAVA_HOME_OVERRIDE}"`
    [ -n "${CLASSPATH}" ] &&
        CLASSPATH=`cygpath --path --unix "${CLASSPATH}"`
fi

if [ -n "${JAVA_HOME_OVERRIDE}" ] ; then
    if [ -x "${JAVA_HOME_OVERRIDE}/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD=${JAVA_HOME_OVERRIDE}/jre/sh/java
    else
        JAVACMD=${JAVA_HOME_OVERRIDE}/bin/java
    fi
else
    if [ -x "${JAVA_HOME}/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD=${JAVA_HOME}/jre/sh/java
    else
        JAVACMD=${JAVA_HOME}/bin/java
    fi
fi

if [ ! -x "${JAVACMD}" ] ; then
    echo "Error: JAVA_HOME is not defined correctly."
    echo "  We cannot execute ${JAVACMD}"
    exit 1
fi

STARTER_MAINCLASS=org.sapia.util.ApplicationStarter
STARTER_CLASSPATH=
for JAR in ${MAGNET_HOME}/lib/bootstrap/*.jar
do
    # if the directory is empty, then it will return the input string
    # this is stupid, so case for it
    if [ "${JAR}" != "${MAGNET_HOME}/lib/bootstrap/*.jar" ] ; then
        if [ -z "${STARTER_CLASSPATH}" ] ; then
            STARTER_CLASSPATH=${JAR}
        else
            STARTER_CLASSPATH="${JAR}":${STARTER_CLASSPATH}
        fi
    fi
done

MAGNET_MAINCLASS=org.sapia.magnet.MagnetRunner
MAGNET_CLASSPATH=${CLASSPATH}
for JAR in ${MAGNET_HOME}/lib/magnet/*.jar
do
    # if the directory is empty, then it will return the input string
    # this is stupid, so case for it
    if [ "${JAR}" != "${MAGNET_HOME}/lib/magnet/*.jar" ] ; then
        if [ -z "${MAGNET_CLASSPATH}" ] ; then
            MAGNET_CLASSPATH=${JAR}
        else
            MAGNET_CLASSPATH="${JAR}":${MAGNET_CLASSPATH}
        fi
    fi
done

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
    MAGNET_HOME=`cygpath --path --windows "${MAGNET_HOME}"`
    JAVA_HOME=`cygpath --path --windows "${JAVA_HOME}"`
    JAVA_HOME_OVERRIDE=`cygpath --path --windows "${JAVA_HOME_OVERRIDE}"`
    STARTER_CLASSPATH=`cygpath --path --windows "${STARTER_CLASSPATH}"`
    MAGNET_CLASSPATH=`cygpath --path --windows "${MAGNET_CLASSPATH}"`
fi

"${JAVACMD}" ${JAVA_VM_TYPE} ${JAVA_X_OPTIONS} ${JAVA_VM_ARGS} -cp ${STARTER_CLASSPATH} ${STARTER_MAINCLASS} -ascp "${MAGNET_CLASSPATH}" ${MAGNET_MAINCLASS} "$@"

