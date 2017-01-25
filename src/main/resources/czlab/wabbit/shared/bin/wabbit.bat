@echo off

set CWD=%CD%
set ARGC=0
for %%x in (%*) do set /A ARGC+=1

set wabbit_HOME=%~dp0..
set BINDIR=%~dp0
set PATCHDIR=%wabbit_HOME%\patch\*
set DISTRO=%wabbit_HOME%\dist\exec\*
set BOOT=%wabbit_HOME%\dist\boot\*
set LIBDIR=%wabbit_HOME%\lib\*

set BCP=%BOOT%;%LIBDIR%;%CLASSPATH%
set LOG4J=etc\log\logback.xml
set L4JFILE=%CD%\%LOG4J%
set L4J=file:/%L4JFILE%
set LOGCFG=%L4J:\=/%
set LOGREF=-Dlogback.configurationFile=%LOGCFG%
set BASEDIR=-Dwabbit.home=%wabbit_HOME%
set BG=false
set DBGOPTS=
set ECODE=0
set KPORT=4444
set KILLPORT=-Dwabbit.kill.port=%KPORT%
set LIBP=-Djava.library.path=$wabbit_HOME/bin
set NETTY=-Dio.netty.eventLoopThreads=16

set JPROF=-agentpath:/Applications/jprofiler7/bin/macos/libjprofilerti.jnilib=port=8849
set VMXRGS=-XX:+CMSClassUnloadingEnabled -XX:+UseConcMarkSweepGC -XX:MaxPermSize=256m
set CLDR=-Djava.system.class.loader=com.zotohlab.wabbit.loaders.ExecClassLoader
set MAINCZ=czlab.wabbit.etc.core

if NOT EXIST %L4JFILE% SET LOGREF=

REM 48G
REM VMARGS=-Xms8192m -Xmx49152m
REM 36G
REM VMARGS=-Xms8192m -Xmx36864m
REM 32G
REM VMARGS=-Xms8192m -Xmx32768m
set VMARGS=-Xms512m -Xmx4096m %VMXRGS%

if "%JAVA_HOME%" == "" goto noJavaHome
:b0
set JAVA_CMD=%JAVA_HOME%\bin\java.exe

if %ARGC% EQU 2 goto testStartBG
:b1

if %ARGC% EQU 1 goto testDebug
:b2

REM ********************************************************
REM run in foreground
REM ********************************************************
cd %BINDIR%
:appfg
REM CMDLINE="%JAVA_CMD%" -cp "%BCP%" "%LIBP%" %DBGOPTS% "%LOGREF%" "%NETTY%" "%KILLPORT%" "%BASEDIR%" %CLDR% %MAINCZ% "%wabbit_HOME%" %*
if %BG% == "true" goto runcmd
call :splash
:runcmd
"%JAVA_CMD%" -cp "%BCP%" "%LIBP%" %DBGOPTS% "%LOGREF%" "%NETTY%" "%KILLPORT%" "%BASEDIR%" %CLDR% %MAINCZ% "%wabbit_HOME%" %*
set ECODE=%ERRORLEVEL%
goto end

REM ********************************************************
REM run in background
REM ********************************************************
:appbg
goto end


REM ********************************************************
REM test for start in background
REM ********************************************************
:testStartBG
if "%1%2" == "startbg" set BG=true
goto b1

REM ********************************************************
REM test for debug mode
REM ********************************************************
:testDebug
if "%1" == "debug" set DBGOPTS=-agentlib:jdwp=transport=dt_socket,server=y,address=8787,suspend=n
goto b2


REM ********************************************************
REM set java_home
REM ********************************************************
:j764
set JAVA_HOME=C:\Program Files\Java\jre7
goto b0
:j664
set JAVA_HOME=C:\Program Files\Java\jre6
goto b0
:j732
set JAVA_HOME=C:\Program Files (x86)\Java\jre7
goto b0
:j632
set JAVA_HOME=C:\Program Files (x86)\Java\jre6
goto b0


REM ********************************************************
REM test for java_home
REM ********************************************************
:noJavaHome
echo No JAVA_HOME set, attempt to reference standard java location.
if exist "C:\Program Files\Java\jre7" goto j764
if exist "C:\Program Files\Java\jre6" goto j664
if exist "C:\Program Files (x86)\Java\jre7" goto j732
if exist "C:\Program Files (x86)\Java\jre6" goto j632
echo Please set JAVA_HOME


:splash
echo "  _____ __  _   ____  ____   ___"
echo " / ___/|  |/ ] /    ||    \ /   \\"
echo "(   \_ |  ' / |  o  ||  D  )     |"
echo " \__  ||    \ |     ||    /|  O  |"
echo " /  \ ||     ||  _  ||    \|     |"
echo " \    ||  .  ||  |  ||  .  \     |"
echo "  \___||__|\_||__|__||__|\_|\___/"
goto runcmd


REM ********************************************************
REM eof
REM ********************************************************
:end
cd %CWD%
exit /B %ECODE%



