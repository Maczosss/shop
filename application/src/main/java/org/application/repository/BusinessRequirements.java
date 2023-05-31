package org.application.repository;

import org.application.model.Category;
import org.application.model.Client;
import org.application.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BusinessRequirements {
    Optional<Client> getHighestPayingClient();

    Optional<Client> getHighestPayingClientInCategory(Category category);

    Map<Client, BigDecimal> checkClientsDebt();

    Map<Integer, String> getMostBoughtProductCategoryBasedOnAge();

    Map<String, Map<String, Product>> getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(Category category);

    Map<Category, List<Client>> getClientsThatBoughtTheMostProductsBasedOnCategory();
}
