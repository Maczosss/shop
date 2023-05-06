package org.application.api;

import lombok.NoArgsConstructor;
import org.application.model.Category;
import org.application.repository.DataSource;
import org.application.service.ClientService;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class ResponseApi {

    private static ClientService service = new ClientService(DataSource.DATABASE);

    public static String getHighestPayingCustomer(){
        return service.getHighestPayingCustomer();
    }

    public static String getHighestPayingClientInCategory(String category){
        return service.getHighestPayingClientInCategory(category);
    }

    public static String checkClientsDebt(){
        return service.checkClientsDebt();
    }

    public static String getMostBoughtProductCategoryBasedOnAge(){
        return service.getMostBoughtProductCategoryBasedOnAge();
    }

    public static String getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(String category){
        return service.getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(category);
    }

    public static String getClientsThatBoughtTheMostProductsBasedOnCategory(){
        return service.getClientsThatBoughtTheMostProductsBasedOnCategory();
    }

    public static List<String> getItems(){
        return Arrays.stream(Category
                        .values())
                .map(Category::getName)
                .toList();
    }
}
