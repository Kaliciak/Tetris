javac --module-path $PATH_TO_FX --add-modules=javafx.controls --add-modules=javafx.fxml --add-modules=javafx.media -d out $(find src -name "*.java")
cp -r Resources/* out/