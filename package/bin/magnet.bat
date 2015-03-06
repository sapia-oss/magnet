@echo off

if "%MAGNET_HOME%" == "" goto noMagnetHome

@setlocal
set JAVA_HOME_OVERRIDE=
set JAVA_VM_TYPE=
set JAVA_X_OPTIONS=
set JAVA_VM_ARGS=-Dmagnet.home="%MAGNET_HOME%"
set MAGNET_CMD_LINE_ARGS=

:setupVmOptions
if "%1" == "-javahome" (
    set JAVA_HOME_OVERRIDE=%2
    shift
    shift
    goto setupJavaHomeOverride

    :setupJavaHomeOverride
    echo %1 | find "-" > nul
    if not errorlevel 1 (
        goto setupVmOptions
    ) else (
        set JAVA_HOME_OVERRIDE=%JAVA_HOME_OVERRIDE% %1
        shift
        goto setupJavaHomeOverride
    )    
)
if "%1" == "-client" (
    set JAVA_VM_TYPE=%1
    shift
    goto setupVmOptions
)
if "%1" == "-server" (
    set JAVA_VM_TYPE=%1
    shift
    goto setupVmOptions
)

echo %1 | find "-X" > nul
if not errorlevel 1 (
    set JAVA_X_OPTIONS=%JAVA_X_OPTIONS% %1
    shift
    goto setupVmOptions
)

echo %1 | find "-D" > nul
if not errorlevel 1 (
    set JAVA_VM_ARGS=%JAVA_VM_ARGS% %1=%2
    shift
    shift
    goto setupVMOptions
)


:testJavaHome
if "%JAVA_HOME%" == "" (
    goto testJavaHomeOverride
) else (
    goto setupMagnetArgs
)


:testJavaHomeOverride
if "%JAVA_HOME_OVERRIDE%a" == "a" goto noJavaHome



:setupMagnetArgs
if %1a==a goto setupJava
set MAGNET_CMD_LINE_ARGS=%MAGNET_CMD_LINE_ARGS% %1
shift
goto setupMagnetArgs


:setupJava
set LOCAL_CLASSPATH=%CLASSPATH%
for %%i in (%MAGNET_HOME%\lib\magnet\*.jar) do call %MAGNET_HOME%\bin\cpappend.bat %%i
set MAGNET_CLASSPATH=%LOCAL_CLASSPATH%

set LOCAL_CLASSPATH=
for %%i in (%MAGNET_HOME%\lib\bootstrap\*.jar) do call %MAGNET_HOME%\bin\cpappend.bat %%i
set STARTER_CLASSPATH=%LOCAL_CLASSPATH%

set STARTER_MAINCLASS=org.sapia.util.ApplicationStarter
set MAGNET_MAINCLASS=org.sapia.magnet.MagnetRunner

if not "%JAVA_HOME_OVERRIDE%a" == "a" (
  set JAVACMD=%JAVA_HOME_OVERRIDE%\bin\java
  goto runJava
)
set JAVACMD=%JAVA_HOME%\bin\java

:runJava
"%JAVACMD%" %JAVA_VM_TYPE% %JAVA_X_OPTIONS% %JAVA_VM_ARGS% -cp "%STARTER_CLASSPATH%" %STARTER_MAINCLASS% -ascp "%MAGNET_CLASSPATH%" %MAGNET_MAINCLASS% %MAGNET_CMD_LINE_ARGS%

if errorlevel 1 (
    echo %JAVACMD% %JAVA_VM_TYPE% %JAVA_X_OPTIONS% %JAVA_VM_ARGS% -cp "%STARTER_CLASSPATH%" %STARTER_MAINCLASS% -ascp "%MAGNET_CLASSPATH%" %MAGNET_MAINCLASS% %MAGNET_CMD_LINE_ARGS%
    pause
)
goto end


:noJavaHome
echo JAVA_HOME not defined; cannot start.
echo Set your JAVA_HOME environment variable or use the -javahome parameter.
goto end


:noMagnetHome
echo MAGNET_HOME not defined; cannot start.
echo Set your MAGNET_HOME environment variable.
goto end

:end
set JAVA_HOME_OVERRIDE=
set JAVA_VM_TYPE=
set JAVA_X_OPTIONS=
set JAVA_VM_ARGS=
set STARTER_CLASSPATH=
set MAGNET_CLASSPATH=
set JAVACMD=
set MAGNET_CMD_LINE_ARGS=
set MAGNET_MAINCLASS=
set STARTER_MAINCLASS=

if "%OS%"=="Windows_NT" @endlocal

