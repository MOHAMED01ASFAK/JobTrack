<# : batch portion
@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script, version 3.3.2
@REM
@REM Required ENV Vars:
@REM JAVA_HOME - location of a JDK home dir
@REM
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0_NAME__%"=="" (SET __MVNW_ARG0_NAME__=%~nx0)
@SET __MVNW_CMD__=%~dp0mvnw.cmd
@SET __BASE_DIR__=%~dp0
@SET WDIR=%%~dp0
@IF NOT "%WDIR:~-1%"=="\" SET WDIR=%WDIR%\

@IF NOT "%JAVA_HOME%"=="" goto OkJHome
@FOR %%i in (java.exe) do @(set "JAVACMD=%%~$PATH:i")
@IF NOT "%JAVACMD%"=="" goto checkJCmd

@echo Error: JAVA_HOME not found in your environment. >&2
@echo Please set the JAVA_HOME variable in your environment to match the >&2
@echo location of your Java installation. >&2
@goto error

:OkJHome
@SET "JAVACMD=%JAVA_HOME%\bin\java.exe"

:checkJCmd
@IF EXIST "%JAVACMD%" goto chkMHome

@echo Error: JAVA_HOME is set to an invalid directory. >&2
@echo JAVA_HOME = "%JAVA_HOME%" >&2
@echo Please set the JAVA_HOME variable in your environment to match the >&2
@echo location of your Java installation. >&2
@goto error

:chkMHome
@SET "WRAPPER_JAR=%__BASE_DIR__%.mvn\wrapper\maven-wrapper.jar"

@IF EXIST "%WRAPPER_JAR%" goto runWrapper

@REM Download Maven distribution directly if wrapper jar does not exist
@powershell -NoProfile -ExecutionPolicy Bypass -Command "& { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; & '%~dp0.mvn\wrapper\mvnw.ps1' %* }"
@goto :EOF

:runWrapper
@SET "MAVEN_PROJECTBASEDIR=%__BASE_DIR__%"
"%JAVACMD%" %MAVEN_OPTS% -jar "%WRAPPER_JAR%" %*
@if ERRORLEVEL 1 goto error
@goto end

:error
@SET ERROR_CODE=1

:end
@cmd /C exit /B %ERROR_CODE%
#>
