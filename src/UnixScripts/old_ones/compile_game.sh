#!/bin/bash

#This script compiles Lunar Lander game 

cd ../
javac game/physic/*.java game/Constant/*.java game/controller/*.java game/data/*.java game/launcher/*.java game/view/*.java game/window/*.java game/entities/*.java game/configReader/*.java game/serverConnection/*.java game/interfaces/*.java game/manager/*.java game/*.java

echo "Game compilled"