package org.application.repository;

import org.application.model.Category;
import org.application.model.Client;
import org.application.model.Product;
import org.dataLoader.jsonMapper.AbstractJsonMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JsonClientRepositoryImpl
        extends AbstractJsonMapper<Client>
        implements BusinessRequirements {

    public JsonClientRepositoryImpl(Class<Client> entityType) {
        super(entityType);
    }

    @Override
    public Optional<Client> getHighestPayingClient() {
        return Optional.empty();
    }

    @Override
    public Optional<Client> getHighestPayingClientInCategory(Category category) {
        return Optional.empty();
    }

    @Override
    public Map<Client, BigDecimal> checkClientsDebt() {
        return null;
    }

    @Override
    public Map<Integer, String> getMostBoughtProductCategoryBasedOnAge() {
        return null;
    }

    @Override
    public Map<String, Map<String, Product>> getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(Category category) {
        return null;
    }

    @Override
    public Map<Category, List<Client>> getClientsThatBoughtTheMostProductsBasedOnCategory() {
        return null;
    }
}
