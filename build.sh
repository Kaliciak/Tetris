#!/bin/bash

build="javafx-sdk-15.0.1/lib/"

javac --module-path $build --add-modules=javafx.controls --add-modules=javafx.fxml --add-modules=javafx.media -d out $(find src -name "*.java")
cp -r Resources/* out/
