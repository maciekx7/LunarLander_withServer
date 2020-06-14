@ECHO OFF
TITLE LUNARLANDER
::going to previous directory
cd ../

::JAVADOC_FOLDER_GAME="../dokumentacja/game"
::JAVADOC_FOLDER_SERVER="../dokumentacja/server"
::OUT_FOLDER="../out"


::delete all class files
del /s /q *.class

::delete jar files
del Game.jar Server.jar

::delete javadoc
cd ../dokumentacja/game
del /s /q *.*
rmdir resources script-dir game /s /q

cd ../server
del /s /q *.*
rmdir resources script-dir server /s /q

::clear out folder
cd ../../out
del /s /q *.*
rmdir production /s /q