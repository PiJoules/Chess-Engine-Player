#!/usr/bin/env bash

compile() {
	mkdir -p build/classes
	javac -d build/classes src/chess/*.java
}

makeJar() {
	compile
	mkdir -p build/jars
	jar cvf chess.jar -C build/classes/ chess
	mv chess.jar build/jars
}

test() {
	makeJar
	mkdir -p Battlefield/build/classes
	javac -d Battlefield/build/classes -cp build/jars/chess.jar Battlefield/*.java
	java -cp Battlefield/build/classes:build/jars/chess.jar Board
	rm -rf Battlefield/build
}

if [ "$1" == "clean" ]; then
	rm -rf build
fi

if [ "$1" == "compile" ]; then
	compile
fi

if [ "$1" == "jar" ]; then
	makeJar
fi

if [ "$1" == "test" ]; then
	test
fi