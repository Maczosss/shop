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

public class AppController implements Initializable {

    @FXML
    private ChoiceBox<String> checkCategory;

    @FXML
    private TextArea resultArea;

    @FXML
    protected void onHighestPayingCustomerClick() {
        resultArea.setText(ResponseApi.getHighestPayingCustomer());
    }
    @FXML
    protected void onHighestPayingCustomerInSpecifiedCategoryClick(ActionEvent event){
        resultArea.setText(ResponseApi.getHighestPayingClientInCategory(getItem(event)));
    }

    @FXML
    protected void onDisplayClientsWithDebtClick(){
        resultArea.setText(ResponseApi.checkClientsDebt());
    }
    @FXML
    protected void onGetDataOnMostBoughtProductInCategoryBasedOnAgeClick(){
        resultArea.setText(ResponseApi.getMostBoughtProductCategoryBasedOnAge());
    }
    @FXML
    protected void onGetMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategoryClick(ActionEvent event){
        resultArea.setText(ResponseApi.getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(getItem(event)));
    }
    @FXML
    protected void onClientsThatBoughtHighestNumberOfProductsInCategoryClick(){
        resultArea.setText(ResponseApi.getClientsThatBoughtTheMostProductsBasedOnCategory());
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
}