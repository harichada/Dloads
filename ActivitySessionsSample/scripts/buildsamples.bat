rem  COPYRIGHT LICENSE: This information contains sample code provided in source 
rem  code form. You may copy, modify, and distribute these sample programs in any
rem  form without payment to IBM for the purposes of developing, using, marketing
rem  or distributing application programs conforming to the application programming
rem  interface for the operating platform for which the sample code is written. 
rem  Notwithstanding anything to the contrary, IBM PROVIDES THE SAMPLE SOURCE CODE
rem  ON AN "AS IS" BASIS AND IBM DISCLAIMS ALL WARRANTIES, EXPRESS OR IMPLIED,
rem  INCLUDING, BUT NOT LIMITED TO, ANY IMPLIED WARRANTIES OR CONDITIONS OF
rem  MERCHANTABILITY, SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE,
rem  AND ANY WARRANTY OR CONDITION OF NON-INFRINGEMENT. IBM SHALL NOT BE LIABLE FOR
rem  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT
rem  OF THE USE OR OPERATION OF THE SAMPLE SOURCE CODE. IBM HAS NO OBLIGATION TO
rem  PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS OR MODIFICATIONS TO THE
rem  SAMPLE SOURCE CODE.

@echo off
@setlocal
set SAMPLES_HOME=%~dp0/..
set ANT_ARGS=-Dbasedir="%SAMPLES_HOME%" -Dwas_home="%WAS_HOME%"

:setArgs
if "%1"=="" goto others
set ANT_ARGS=%ANT_ARGS% %1
shift
goto setArgs

:others
set ANTCLASSPATH="%JAVA_HOME%\lib\tools.jar";"%WAS_HOME%\lib\j2ee.jar";"%WAS_HOME%\plugins\com.ibm.ws.runtime.jar";"%WAS_HOME%\plugins\com.ibm.ws.runtime.dist.jar";"%WAS_HOME%\plugins\com.ibm.ws.emf.jar";"%WAS_HOME%\plugins\com.ibm.ws.wccm.jar";"%WAS_HOME%\lib\bootstrap.jar";"%WAS_HOME%\plugins\com.ibm.ws.bootstrap.jar";"%WAS_HOME%\plugins\org.eclipse.core.runtime_.jar";

:run
"%JAVA_HOME%\bin\java" %WAS_LOGGING% -classpath %ANTCLASSPATH% org.apache.tools.ant.Main %ANT_ARGS%

@endlocal
