@ECHO OFF
::Going back from UnixScripts folder to src folder
cd ../

::Setting path for game javadocFiles ->
::now going back to previous directory and going to javadoc/game folder
::JAVADOC_FOLDER_GAME="../dokumentacja/game"

::Setting path for server javadocFiles ->
::now going back to previous directory and going to javadoc/server folder
::JAVADOC_FOLDER_SERVER="../dokumentacja/server"

::creating javadoc in specyfic folder for all game files
javadoc -d ../dokumentacja/game game game.physic game.Constant game.controller game.data game.interfaces game.launcher game.view game.window game.entities game.configReader game.manager game.serverConnection

::creating javadoc in specyfic folder for all server files
javadoc -d ../dokumentacja/server server server.configReader
