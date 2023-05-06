package com.example.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.application.model.Category;

import java.io.IOException;
import java.util.Arrays;

public class ShopApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ShopApplication.class.getResource("app-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 880, 530);
        stage.setTitle("Shop app");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        System.out.println(Arrays.asList(Category.values()).stream().map(Category::getName).toList());
        launch();
    }
}