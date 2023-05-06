package com.example.ui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.application.api.ResponseApi;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    private ResponseApi api = new ResponseApi();

    @FXML
    private Label label;

    @FXML
    private ChoiceBox<String> checkCategory;

    @FXML
    private TextArea resultArea;

    @FXML
    protected void onHighestPayingCustomerClick() {

        resultArea.setText(ResponseApi.checkClientsDebt());
    }
    @FXML
    protected void onHighestPayingCustomerInCategoryClick(ActionEvent event){
        resultArea.setText(ResponseApi.getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(getItem(event)));
        }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resultArea.setWrapText(true);
        checkCategory.getItems().addAll(
                FXCollections.observableList(
                        ResponseApi.getItems()));
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