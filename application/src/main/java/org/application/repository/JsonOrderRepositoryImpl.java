package org.application.repository;

import org.application.model.Category;
import org.application.model.Client;
import org.application.model.Order;
import org.application.model.Product;
import org.dataLoader.jsonMapper.AbstractJsonMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class JsonOrderRepositoryImpl
        extends AbstractJsonMapper<Order>
        implements OrderRepository<Order, Long>, ClientRepository{

    private final String PATH_TO_JSON_FILE_LOCATION;
    public JsonOrderRepositoryImpl(Class<Order> entityType, String jsonFilePath) {
        super(entityType);
        this.PATH_TO_JSON_FILE_LOCATION = jsonFilePath;
    }
    @Override
    public List<Order> saveAllOrders(List<Order> orders) {
        super.save(orders, PATH_TO_JSON_FILE_LOCATION);
        return orders;
    }

    @Override
    public Optional<Client> getHighestPayingClient() {
        var data = super.getFromJsonFile(PATH_TO_JSON_FILE_LOCATION);
        return data.stream()
                .collect(Collectors.groupingBy(
                        Order::getClient,
                        Collectors.summingDouble(
                                order -> order
                                        .getProducts()
                                        .stream()
                                        .mapToDouble(
                                                Product::getPrice)
                                        .sum())))
                .entrySet()
                .stream()
                .filter(client -> client.getValue()>0)
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey);
    }

    @Override
    public Optional<Client> getHighestPayingClientInCategory(Category category) {
        var data = super.getFromJsonFile(PATH_TO_JSON_FILE_LOCATION);
        return data.stream()
                .collect(Collectors.groupingBy(
                        Order::getClient,
                        Collectors.summingDouble(
                                order -> order
                                        .getProducts()
                                        .stream()
                                        .filter((product->
                                                product.getCategory().equals(category.getName())))
                                        .mapToDouble(
                                                Product::getPrice)
                                        .sum())))
                .entrySet()
                .stream()
                .filter(client -> client.getValue()>0)
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey);
    }

    @Override
    public Map<Client, BigDecimal> checkClientsDebt() {
        var data = super.getFromJsonFile(PATH_TO_JSON_FILE_LOCATION);
        return data.stream()
                .collect(Collectors.groupingBy(
                        Order::getClient,
                        Collectors.summingDouble(
                                order -> order
                                        .getProducts()
                                        .stream()
                                        .mapToDouble(
                                                Product::getPrice)
                                        .sum())))
                .entrySet()
                .stream()
                .filter(client -> client.getValue()>0)
                .collect(Collectors.toMap(Map.Entry::getKey,
                entry -> BigDecimal.valueOf(entry.getKey().getCash() - entry.getValue())));
    }

    @Override
    public Map<Integer, String> getMostBoughtProductCategoryBasedOnAge() {
        var data = super.getFromJsonFile(PATH_TO_JSON_FILE_LOCATION);
        var resultStructure = data.stream().collect(Collectors.groupingBy(
                entry-> entry.getClient().getAge(),
                Collectors.toList()))
                .entrySet().stream().map(
                        entry -> {
                            var resultMap = new HashMap<Product, Integer>();
                            var list = entry.getValue();
                            for(Order order: list){
                                for(Product product: order.getProducts()){
                                    resultMap.compute(product, (key, value) -> value!=null?value+1:1);
                                }
                            }
                            var resultProduct = resultMap.entrySet().stream()
                                    .max(Map.Entry.comparingByValue());
                            return new AbstractMap.SimpleEntry<Integer, Map.Entry<Product, Integer>>(entry.getKey(), new AbstractMap.SimpleEntry<Product, Integer>(
                                    resultProduct.get().getKey(), resultProduct.get().getValue()

                            ));
                        }
                ).toList();


        return resultStructure.stream().map(entity -> {
            return new AbstractMap.SimpleEntry<Integer, String>(entity.getKey(), entity.getValue().getKey().getName());
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, Map<String, Product>> getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(Category category) {
        var data = super.getFromJsonFile(PATH_TO_JSON_FILE_LOCATION);
        var products = new HashMap<String, Product>();
        products.put("max", new Product());
        products.put("min", new Product());
        var counter = new AtomicReference<Integer>(0);
        var sum = new AtomicReference<Double>(0d);
        data.stream().forEach(element -> {
            for(Product product: element.getProducts()){
                if(product.getCategory().equals(category.getName())) {
                    if (products.get("max").getPrice() == null || product.getPrice() > products.get("max").getPrice()) {
                        products.compute("max", (key, value) -> value = product);
                    }
                    if (products.get("min").getPrice() == null || product.getPrice() < products.get("min").getPrice()) {
                        products.compute("min", (key, value) -> value = product);
                    }
                    var tempCounter = counter.get() + 1;
                    var tempSum = sum.get() + product.getPrice();
                    counter.set(tempCounter);
                    sum.set(tempSum);
                }
            }
        });
        BigDecimal avg = new BigDecimal(String.valueOf(sum.get()))
                .setScale(2, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(counter.get()),RoundingMode.HALF_UP);
        var resultMap = new HashMap<String, Map<String, Product>>();
        resultMap.put("Lowest value Product",Map.of(
                products.get("min").getPrice()!=null?products.get("min").getPrice().toString(): "Didn't found instance",
                    products.get("min")
        ));
        resultMap.put("Highest value Product",Map.of(
                products.get("max").getPrice()!=null?products.get("max").getPrice().toString(): "Didn't found instance",
                products.get("max")
        ));
        resultMap.put("Average price of products in category %s".formatted(category.getName()),
                Map.of(
                        avg.toString()
                        , new Product())
        );
        return resultMap;
    }

    @Override
    public Map<Category, List<Client>> getClientsThatBoughtTheMostProductsBasedOnCategory() {
        var data = super.getFromJsonFile(PATH_TO_JSON_FILE_LOCATION);

        var clientMap = new HashMap<Client, Map<Category, Integer>>();
        for(Order order: data){
            for(Product product: order.getProducts()) {
                clientMap.compute(order.getClient(), (key, value) -> {
                    if (value == null) {
                        value = new HashMap<>();
                    }
                    value.compute(
                            Category.getCategory(
                                    product.getCategory()), (k, v) -> (v == null) ? 1 : v + 1);
                    return value;
                });
            }
        }

        //todo add logic to include multiple clients, right now only one is returned in case of 2 with same number of purchases

        Map<Category, List<Client>> highestRepetitionByCategory = new HashMap<>();

        clientMap.forEach((client, categoryMap) -> {
            Optional<Map.Entry<Category, Integer>> maxEntry = categoryMap.entrySet().stream()
                    .max(Map.Entry.comparingByValue());

            maxEntry.ifPresent(entry -> highestRepetitionByCategory.put(entry.getKey(), List.of(client)));
        });

        return highestRepetitionByCategory;
    }

    @Override
    public Order saveOrder(Order orderToSave) {
        //todo add this logic
        return null;
    }
}
