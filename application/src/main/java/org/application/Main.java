package org.application;

import org.application.dataProvider.DataProviderService;
import org.application.model.Category;
import org.application.repository.DataSource;
import org.application.service.ClientService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        //properties download
        Properties appProperties = new Properties();
        try {
            appProperties
                    .load(new FileInputStream("PATH_TO_PROPERTIES"));
        } catch (IOException e) {
//            throw new RuntimeException(e);
            System.out.println("well");
        }
        //end of properties

//        DataProviderService.createDatabaseEntries(appProperties);
//        System.out.println(new ClientService(DataSource.DATABASE)
//                .getMostBoughtProductCategoryBasedOnAge());
//        System.out.println(new ClientService(DataSource.DATABASE)
//                .getHighestPayingClientInCategory(Category.COMPUTER));
//        System.out.println(new ClientService(DataSource.DATABASE)
//                .getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(Category.COMPUTER));
        System.out.println(new ClientService(DataSource.DATABASE)
                .getClientsThatBoughtTheMostProductsBasedOnCategory());



    }
}