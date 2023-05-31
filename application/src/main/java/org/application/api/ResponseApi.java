package org.application.api;

import lombok.NoArgsConstructor;
import org.application.dataProvider.DataProviderService;
import org.application.model.Category;
import org.application.repository.DataSource;
import org.application.service.ClientService;
import org.application.settings.AppProperties;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class ResponseApi {

    private static ClientService service;
    private static boolean propertiesInitialized = false;


    private static final String INITIALIZE_INFO_MESSAGE = "Please provide properties, with info needed to operate";

    public static String getHighestPayingCustomer() {
        return propertiesInitialized ?
                service.getHighestPayingCustomer() :
                INITIALIZE_INFO_MESSAGE;
    }

    public static String getHighestPayingClientInCategory(String category) {
        return propertiesInitialized ?
                service.getHighestPayingClientInCategory(category) :
                INITIALIZE_INFO_MESSAGE;
    }

    public static String checkClientsDebt() {
        return propertiesInitialized ?
                service.checkClientsDebt() :
                INITIALIZE_INFO_MESSAGE;
    }

    public static String getMostBoughtProductCategoryBasedOnAge() {
        return propertiesInitialized ?
                service.getMostBoughtProductCategoryBasedOnAge() :
                INITIALIZE_INFO_MESSAGE;
    }

    public static String getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(String category) {
        return propertiesInitialized ?
                service.getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(category) :
                INITIALIZE_INFO_MESSAGE;
    }

    public static String getClientsThatBoughtTheMostProductsBasedOnCategory() {
        return propertiesInitialized ?
                service.getClientsThatBoughtTheMostProductsBasedOnCategory() :
                INITIALIZE_INFO_MESSAGE;
    }

    public static List<String> getPossibleCategories() {
        return Arrays.stream(Category
                        .values())
                .map(Category::getName)
                .toList();
    }

    public static List<String> getPossibleDataSources() {
        return Arrays.stream(DataSource
                        .values())
                .map(DataSource::getName)
                .toList();
    }

    public static void initializeProperties(CharSequence characters, String dataSource) {
        if (!propertiesInitialized) {
            AppProperties.getInstance().initialize(characters.toString());
            propertiesInitialized = true;
        }
        if(AppProperties.getInstance().checkDataSource(dataSource)){
            service = new ClientService(DataSource.getSource(dataSource));
        }
        else{
            initializeData(characters, dataSource);
        }
    }

    public static void initializeData(CharSequence characters, String dataSource) {
        if (!propertiesInitialized) {
            AppProperties.getInstance().initialize(characters.toString());
            propertiesInitialized = true;
        }
        if(AppProperties.getInstance().checkDataSource(dataSource)) {
            switch (DataSource.getSource(dataSource)) {
                case DATABASE -> DataProviderService.createDatabaseEntries();
                case TXT_FILE -> DataProviderService.createFileEntries();//todo add repository
                case JSON_FILE, EMPTY -> DataProviderService.createJsonEntries();// in case empty data source, create json file
            }
        }
        service = new ClientService(DataSource.getSource(dataSource));
    }
}
