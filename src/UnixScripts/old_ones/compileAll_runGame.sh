#!/bin/bash
cd ../
echo "Server compilling..."

javac server/configReader/*.java server/*.java

echo "Server compilled"

echo "Game compilling..."

javac game/physic/*.java game/Constant/*.java game/controller/*.java game/data/*.java game/launcher/*.java game/view/*.java game/window/*.java game/entities/*.java game/configReader/*.java game/serverConnection/*.java game/interfaces/*.java game/manager/*.java game/*.java

echo "Game compilled"

echo "Launching game..."

java game.GameLauncher

echo "Game closed"