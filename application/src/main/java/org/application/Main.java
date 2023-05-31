package org.application;

import org.application.dataProvider.DataProviderService;
import org.application.settings.AppProperties;
import org.dataLoader.DatabaseConnection;

public class Main {
    public static void main(String[] args) {


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

//        var result = new ClientService(DataSource.JSON_FILE)
//                .getClientsThatBoughtTheMostProductsBasedOnCategory();
//        System.out.println(result);

//        var test = DataSource.getSource("json f");
//        var test2= DataSource.JSON_FILE;
        AppProperties.getInstance().initialize("D:\\Sandbox\\KMShop\\shop\\application\\src\\main\\resources\\app.properties");
         DataProviderService.createDatabaseEntries();
        System.out.println("  sdxs  ");
    }
}