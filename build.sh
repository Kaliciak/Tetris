#!/bin/bash

build="$PATH_TO_FX"

if [ "$#" -gt "0" ]; then
    build="$1";
else
    if [ "$PATH_TO_FX" = "" ]; then
        echo "You need to set \$PATH_TO_FX variable or pass path to javafx library as argument";
        exit
    fi
fi

javac --module-path $build --add-modules=javafx.controls --add-modules=javafx.fxml --add-modules=javafx.media -d out $(find src -name "*.java")
cp -r Resources/* out/