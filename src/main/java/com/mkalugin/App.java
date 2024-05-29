package com.mkalugin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static File file;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("ui"), 1000, 800);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));       
        stage.setTitle("SGO Tool by MKalugin");
        stage.setScene(scene);
        stage.show();
    }

    public static Window getWindow()
    {
        return App.scene.getWindow();
    }

    public static boolean setFile(File file) throws IOException {
        App.file = file;
        return(Sgotool.openFile(file));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}