package org.application.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.application.model.Category;
import org.application.model.Product;
import org.dataLoader.databaseMapper.AbstractCrudRepository;
import org.application.model.Client;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ClientRepositoryImpl
        extends AbstractCrudRepository<Client, Long>
        implements ClientRepository {

    public ClientRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    //TODO przerobić testowanie

    @Override
    public Optional<Client> getHighestPayingClient() {
        var sql = """
                SELECT c.firstName, c.lastName, SUM(p.price) AS total_order_value
                FROM clients c
                JOIN orders o ON c.id = o.client_id
                JOIN order_item oi ON o.id = oi.order_id
                JOIN products p ON oi.product_id = p.id
                GROUP BY c.id
                ORDER BY total_order_value DESC
                """;

        return jdbi.withHandle(handle -> {
                    if (checkIfTableExists(handle, List.of("clients"))) {
                        return handle
                                .createQuery(sql)
                                .mapToBean(Client.class)
                                .findFirst();
                    } else return Optional.empty();
                }
        );
    }


    @Override
    public Optional<Client> getHighestPayingClientInCategory(Category category) {

        var sql = """
                SELECT c.firstName, c.lastName, SUM(p.price) AS total_order_value
                FROM clients c
                JOIN orders o ON c.id = o.client_id
                JOIN order_item oi ON o.id = oi.order_id
                JOIN products p ON oi.product_id = p.id && p.category='%s'
                GROUP BY c.id
                ORDER BY total_order_value DESC
                LIMIT 1;
                """.formatted(category.getName());

        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .mapToBean(Client.class)
                .findFirst()
        );

    }

    @Override
    public Map<Client, BigDecimal> checkClientsDebt() {
        var sql = """
                SELECT c.id, c.firstName, c.lastName, c.cash, total_order_value, (c.cash - total_order_value) AS debt
                FROM clients c
                JOIN (
                    SELECT o.client_id, SUM(p.price) AS total_order_value
                    FROM orders o
                    JOIN order_item oi ON o.id = oi.order_id
                    JOIN products p ON oi.product_id = p.id
                    GROUP BY o.client_id
                ) AS orders_by_client ON c.id = orders_by_client.client_id
                HAVING debt<0
                ORDER BY debt DESC;
                """;
        //todo ogarnąć opcję z where, prawdopodobnie nie da się bez subquery
        var clientsWithDebt = new AtomicReference<List<ClientDebtMapper>>(List.of());

        try (Handle handle = jdbi.open()) {
            if (checkIfTableExists(handle,
                    List.of(
                            "clients",
                            "orders",
                            "order_item",
                            "products"))) {
                clientsWithDebt.set(
                        handle
                                .registerRowMapper(new ClientDebtMapper())
                                .createQuery(sql)
                                .mapTo(ClientDebtMapper.class)
                                .list());
            } else clientsWithDebt.set(List.of());
        }
        return clientsWithDebt
                .get()
                .stream()
                .collect(Collectors
                        .toMap(
                                ClientDebtMapper::getCLient,
                                ClientDebtMapper::getDebt));
    }

    @Override
    public Map<Integer, String> getMostBoughtProductCategoryBasedOnAge() {
        //SQL that creates histogram of product_id based on age
        var sql = """
                SELECT c.age, p.id AS most_frequent_product_id, COUNT(*) AS repetitions, p.category
                FROM clients c
                JOIN orders o ON c.id = o.client_id
                JOIN order_item oi ON o.id = oi.order_id
                JOIN products p ON oi.product_id = p.id
                WHERE p.id IN (
                SELECT p2.id
                FROM products p2
                JOIN order_item oi2 ON p2.id = oi2.product_id
                GROUP BY p2.id
                HAVING COUNT(*) > 1
                )
                GROUP BY c.age, p.id
                ORDER BY c.age, repetitions DESC;
                """;
        //sql that gathers most frequent product id, not used. Reason is we can have false positive
        // when 2 or more products have the same number of repetitions
//        var sql = """
//                SELECT c.firstName, c.lastName, SUM(p.price) AS total_order_value
//                FROM clients c
//                JOIN orders o ON c.id = o.client_id
//                JOIN order_item oi ON o.id = oi.order_id
//                JOIN products p ON oi.product_id = p.id
//                GROUP BY c.id
//                ORDER BY total_order_value DESC
//                """;

        var ageHistogram = new AtomicReference<List<AgeHistogramMapper>>(List.of());

        try (Handle handle = jdbi.open()) {
            if (checkIfTableExists(handle,
                    List.of(
                            "clients",
                            "orders",
                            "order_item",
                            "products"))) {
                ageHistogram.set(
                        handle
                                .registerRowMapper(new AgeHistogramMapper())
                                .createQuery(sql)
                                .mapTo(AgeHistogramMapper.class)
                                .list());
            } else ageHistogram.set(List.of());
        }
        // group age and category
        Map<Integer, Map<String, Integer>> groupedByAgeAndCategory = ageHistogram.get().stream()
                .collect(Collectors.groupingBy(AgeHistogramMapper::getAge,
                        Collectors.groupingBy(AgeHistogramMapper::getCategory,
                                Collectors.summingInt(AgeHistogramMapper::getRepetitions))));

        return groupedByAgeAndCategory.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                    Map.Entry<String, Integer> maxEntry = null;
                    for (Map.Entry<String, Integer> tempEntry : entry.getValue().entrySet()) {
                        if (maxEntry == null || tempEntry.getValue() > maxEntry.getValue()) {
                            maxEntry = tempEntry;
                        }
                    }
                    return maxEntry.getKey();
                }));
    }

    @Override
    public Map<String, Map<String, Product>> getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(
            Category category) {
        var highestPriceSql = """
                SELECT p.id, p.name, p.category, p.price
                FROM products p
                GROUP BY p.id
                HAVING p.category='%s'
                ORDER BY p.price DESC
                LIMIT 1;
                """.formatted(category.getName());
        var lowestPriceSql = """
                SELECT p.id, p.name, p.category, p.price
                FROM products p
                GROUP BY p.id
                HAVING p.category='%s'
                ORDER BY p.price ASC
                LIMIT 1;
                """.formatted(category.getName());

        var avrPriceSql = """
                SELECT avg(p.price) as avg_price
                FROM products p
                Group BY p.category
                HAVING p.category='%s'
                """.formatted(category.getName());


        var minMaxAverageOfProductsInCategory = new AtomicReference<Map<String, Map<String, Product>>>(Map.of());

        try (Handle handle = jdbi.open()) {
            if (checkIfTableExists(handle,
                    List.of(
                            "products"))) {
                var lowestPrice =
                        handle
                                .createQuery(lowestPriceSql)
                                .mapToBean(Product.class)
                                .findFirst()
                                .orElse(null);
                var highestPrice =
                        handle
                                .createQuery(highestPriceSql)
                                .mapToBean(Product.class)
                                .findFirst()
                                .orElse(null);

                var avg = handle
                        .createQuery(avrPriceSql)
                        .mapTo(String.class)
                        .findFirst()
                        .orElse("-1");

                var tempValue = new BigDecimal(avg)
                        .setScale(2, RoundingMode.HALF_UP);

                minMaxAverageOfProductsInCategory.set(Map.of(
                        "Lowest value Product",
                        Map.of(
                                lowestPrice != null ? lowestPrice.getPrice().toString()
                                        : "Didn't found instance",
                                lowestPrice != null ? lowestPrice : Product.builder().build()),
                        "Highest value Product",
                        Map.of(
                                highestPrice != null ? highestPrice.getPrice().toString()
                                        : "Didn't found instance",
                                highestPrice != null ? highestPrice : Product.builder().build()),
                        "Average price of products in category %s".formatted(category.getName()),
                        Map.of(
                                tempValue.compareTo(BigDecimal.ZERO) != -1 ? tempValue.toString()
                                        : "Error occurred while mapping average value"
                                , new Product())

                ));
            } else minMaxAverageOfProductsInCategory.set(Map.of());
        }
        return minMaxAverageOfProductsInCategory.get();
    }

    public Map<Category, List<Client>> getClientsThatBoughtTheMostProductsBasedOnCategory() {

        var sql = """
                SELECT pr.category, c.id, c.firstName, c.lastName, COUNT(*) AS count
                				FROM products pr
                				JOIN order_item oi ON pr.id = oi.product_id
                				JOIN orders o ON oi.order_id = o.id
                				JOIN clients c ON o.client_id = c.id
                				GROUP BY pr.category, c.id
                				   HAVING COUNT(*) = (SELECT MAX(sub.count)
                									  FROM (SELECT COUNT(*) AS count
                								FROM products p
                								JOIN order_item oi ON p.id = oi.product_id
                								JOIN orders o ON oi.order_id = o.id
                								JOIN clients c ON o.client_id = c.id
                								WHERE p.category = pr.category
                								GROUP BY c.id) sub);
                """;

        //todo zapytać czy jest to poprawne?
        var resultsMap = new AtomicReference<Map<Category, List<Client>>>(Map.of());

        try (Handle handle = jdbi.open()) {
            if (checkIfTableExists(handle,
                    List.of(
                            "clients",
                            "orders",
                            "order_item",
                            "products"))) {
                handle.createQuery(sql)
                        .map((r, ctx) -> {
                                    var result = new Object() {

                                        public String category = r.getString("category");
                                        public String firstName = r.getString("firstName");
                                        public String lastName = r.getString("lastName");

                                        public Client getClient() {
                                            return Client
                                                    .builder()
                                                    .id(0L)
                                                    .firstName(firstName)
                                                    .lastName(lastName)
                                                    .age(0)
                                                    .cash(0f)
                                                    .build();
//                                                new Client(0L, firstName, lastName, 0, 0f);
                                        }

                                    };
                                    //complicated logic but there is possibility that there are more than one client,
                                    //that bought highest number of products from category
                                    //so this instance needs to be resolved,
                                    // with multiple clients in list assigned to one category key in the map
                                    if (resultsMap.get()
                                            .keySet()
                                            .stream()
                                            .anyMatch((element) -> element.getName().equals(result.category))) {
                                        var tempMap = resultsMap.get();
                                        tempMap.get(Category.getCategory(result.category)).add(result.getClient());
                                        resultsMap.set(tempMap);
                                    } else {
                                        var currentCategory = Category.getCategory(result.category);
                                        var tempMap = new HashMap<>(resultsMap.get());
                                        tempMap.put(currentCategory, new LinkedList<>());
                                        tempMap.get(currentCategory).add(result.getClient());
                                        resultsMap.set(tempMap);
                                    }
                                    return result;
                                }
                        ).collect(Collectors.toList());
            } else resultsMap.set(Map.of());
        }
        return resultsMap.get();
    }

    private boolean checkIfTableExists(Handle handle, List<String> tableNames) {

        //todo name of db is hardcoded, needs to be read from properties
        return handle.createQuery("""
                        SELECT COUNT(table_name)
                        FROM information_schema.tables
                        WHERE table_schema = '%s' AND table_name IN ( %s );
                        """.formatted("shop",
                        tableNames.stream()
                                .map(s -> "'" + s + "'")
                                .collect(Collectors.joining(", "))))
                .mapTo(Integer.class)
                .findFirst()
                .orElse(0) == tableNames.size();
    }
}


@Data
@AllArgsConstructor
@NoArgsConstructor
class AgeHistogramMapper implements RowMapper<AgeHistogramMapper> {
    private int age;
    private Long productId;
    private int repetitions;
    private String category;

    @Override
    public AgeHistogramMapper map(ResultSet rs, StatementContext ctx) throws SQLException {
        var entry =
                new AgeHistogramMapper(rs.getInt("age"),
                        rs.getLong("most_frequent_product_id"),
                        rs.getInt("repetitions"),
                        rs.getString("category"));

        return entry;
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ClientDebtMapper implements RowMapper<ClientDebtMapper> {
    private Client cLient;
    private BigDecimal debt;

    @Override
    public ClientDebtMapper map(ResultSet rs, StatementContext ctx) throws SQLException {
        var entry =
                new ClientDebtMapper(new Client((long) rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getBigDecimal("cash")),
                        BigDecimal.valueOf(rs.getInt("debt")).abs());
        return entry;
    }
}
