package org.application;

import org.application.dataProvider.DataProviderService;
import org.application.model.Category;
import org.application.repository.DataSource;
import org.application.service.ClientService;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
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
//        var result = new ClientService(DataSource.DATABASE)
//                .getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(Category.COMPUTER.getName());
//
//        System.out.println(result);


//       var test = new ClientService(DataSource.DATABASE)
//               .getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory("computer");
//        .checkClientsDebt();
//               .getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory("graphics card");

//        System.out.println(test);

        var result = new ClientService(DataSource.JSON_FILE)
                .getClientsThatBoughtTheMostProductsBasedOnCategory();
        System.out.println(result);
    }
}