package com.example.ui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import org.application.api.ResponseApi;
import org.application.model.Category;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private ChoiceBox<String> checkCategory;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHighestPayingCustomerClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    protected void onHighestPayingCustomerInCategoryClick(ActionEvent event){
        ResponseApi.get().getHighestPayingCustomer();
        welcomeText.setText(ResponseApi.getHighestPayingCustomerInCategory(getItem(event)));}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkCategory.getItems().addAll(
                FXCollections.observableList(
                        Arrays.stream(Category
                                        .values())
                                .map(Category::getName)
                                .toList()));
        checkCategory.setOnAction(this::getItem);
    }

    public String getItem(ActionEvent event){
        return checkCategory.getValue();
    }

//    @FXML
//    protected void onTestButtonClick(){
//        ResponseApi api = null;
//    }
}