#!/bin/bash

#This script creating jar files

#going back to previous folder
cd ../

#game and server jar files names
GAME_JAR_NAME="Game.jar"
SERVER_JAR_NAME="Server.jar"

#path to meta-inf folders
METAINF_GAME_PATH="game/META-INF/MANIFEST.MF"
METAINF_SERVER_PATH="server/META-INF/MANIFEST.MF"

#This command compiles game
javac game/physic/*.java game/Constant/*.java game/controller/*.java game/data/*.java game/launcher/*.java game/view/*.java game/window/*.java game/entities/*.java game/configReader/*.java game/serverConnection/*.java game/interfaces/*.java game/manager/*.java game/*.java

#This command compiles server
javac server/configReader/*.java server/*.java

#this command creating Game jar file
jar cvfm $GAME_JAR_NAME $METAINF_GAME_PATH game/*.class game/physic/*.class game/Constant/*.class game/controller/*.class game/data/*.class game/launcher/*.class game/view/*.class game/window/*.class game/entities/*.class game/configReader/*.class game/serverConnection/*.class game/interfaces/*.class game/manager/*.class

#This command creating Server jar file
jar cvfm $SERVER_JAR_NAME $METAINF_SERVER_PATH server/configReader/*.class server/*.class

