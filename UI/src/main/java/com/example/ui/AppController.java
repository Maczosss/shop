package com.example.ui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.application.api.ResponseApi;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    @FXML
    private TextField propertiesPath;

    @FXML
    private ChoiceBox<String> checkCategory;

    @FXML
    private ChoiceBox<String> dataSource;

    @FXML
    private TextArea resultArea;

    @FXML
    public void setConnection(ActionEvent event) {
        if (checkRequiredFields()) return;
        ResponseApi.initializeProperties(propertiesPath.getCharacters(), dataSource.getValue());
    }

    @FXML
    protected void initializeData() {
        if (checkRequiredFields()) return;
        ResponseApi.initializeData(propertiesPath.getCharacters(), dataSource.getValue());
    }

    @FXML
    protected void onHighestPayingCustomerClick() {
        resultArea.setText(ResponseApi.getHighestPayingCustomer());
    }

    @FXML
    protected void onHighestPayingCustomerInSpecifiedCategoryClick(ActionEvent event) {
        if (getItem(event, checkCategory) == null) {
            resultArea.setText("First pick category");
        } else {
            resultArea.setText(ResponseApi.getHighestPayingClientInCategory(getItem(event, checkCategory)));
        }
    }

    @FXML
    protected void onDisplayClientsWithDebtClick() {
        resultArea.setText(ResponseApi.checkClientsDebt());
    }

    @FXML
    protected void onGetDataOnMostBoughtProductInCategoryBasedOnAgeClick() {
        resultArea.setText(ResponseApi.getMostBoughtProductCategoryBasedOnAge());
    }

    @FXML
    protected void onGetMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategoryClick(ActionEvent event) {
        if (getItem(event, checkCategory) == null) {
            resultArea.setText("First pick category");
        } else {
            resultArea.setText(ResponseApi.getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(getItem(event, checkCategory)));
        }
    }

    @FXML
    protected void onClientsThatBoughtHighestNumberOfProductsInCategoryClick() {
        resultArea.setText(ResponseApi.getClientsThatBoughtTheMostProductsBasedOnCategory());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        resultArea.setWrapText(true);

        dataSource.getItems().addAll(
                FXCollections.observableList(
                        ResponseApi.getPossibleDataSources()));

        checkCategory.getItems().addAll(
                FXCollections.observableList(
                        ResponseApi.getPossibleCategories()));


        dataSource.setOnAction(event -> getItem(event, dataSource));

        checkCategory.setOnAction(event -> getItem(event, checkCategory));
    }

    public String getItem(ActionEvent event, ChoiceBox cb) {
        return cb.getValue()==null?null:cb.getValue().toString();
//        return cb.getValue().toString();
    }

    private boolean checkRequiredFields() {
        if (propertiesPath == null || propertiesPath.getCharacters().length() == 0) {
            resultArea.setText("please provide path to your properties file");
            return true;
        }
        if (dataSource.getValue() == null || dataSource.getValue().equals("")) {
            resultArea.setText("Please choose what should be data source type");
            return true;
        }
        return false;
    }

}