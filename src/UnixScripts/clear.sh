#!/bin/bash

#going to previous directory
cd ../

JAVADOC_FOLDER_GAME="../dokumentacja/game"
JAVADOC_FOLDER_SERVER="../dokumentacja/server"
OUT_FOLDER="../out"


#delete game class files
rm -f game/*.class game/physic/*.class game/Constant/*.class game/controller/*.class game/data/*.class game/launcher/*.class game/view/*.class game/window/*.class game/entities/*.class game/configReader/*.class game/serverConnection/*.class game/interfaces/*.class game/manager/*.class

#delete server class files
rm -f server/configReader/*.class server/*.class

#delete jar files
rm -f Game.jar Server.jar

#delete game javadoc
rm -r $JAVADOC_FOLDER_GAME/* $JAVADOC_FOLDER_GAME/*/

#delete server javadoc
rm -r $JAVADOC_FOLDER_SERVER/* $JAVADOC_FOLDER_SERVER/*/

#clear out folder
rm -r $OUT_FOLDER/*/