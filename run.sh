#!/bin/bash

build="javafx-sdk-15.0.1/lib/"

java --module-path $build --add-modules=javafx.controls --add-modules=javafx.fxml --add-modules=javafx.media -cp out Tetris.Game
