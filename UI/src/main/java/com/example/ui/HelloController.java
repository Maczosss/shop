package com.example.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;
//import org.example.api.ResponseApi;


public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

//    @FXML
//    protected void onTestButtonClick(){
//        ResponseApi api = null;
//    }
}