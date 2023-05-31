package org.application.dataProvider;

import org.apache.logging.log4j.LogManager;
import org.application.model.Category;
import org.application.repository.ClientRepositoryImpl;
import org.application.repository.OrderRepositoryImpl;
import org.application.repository.ProductRepositoryImpl;
import org.application.settings.AppProperties;
import org.dataLoader.DatabaseConnection;
import org.dataLoader.fileMapper.AbstractFromFileMapper;
import org.application.model.Client;
import org.application.model.Order;
import org.application.model.Product;
import org.application.repository.*;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultSetException;

import java.util.*;
import java.util.function.Function;


public class DataProviderService {

    private static ClientRepositoryImpl clientRepository;
    private static ProductRepositoryImpl productRepository;
    private static OrderRepositoryImpl orderReposiory;
    private static List<Client> clients = List.of(Client
                    .builder()
                    .firstName("Maciej")
                    .lastName("Jaremowicz")
                    .age(25)
//                    .cash(BigDecimal.valueOf(2000F))
                    .cash(2000F)
                    .build(),
            Client
                    .builder()
                    .firstName("Andrzej")
                    .lastName("Kowalski")
                    .age(25)
//                    .cash(BigDecimal.valueOf(1000000F))
                    .cash(1000000F)
                    .build(),
            Client
                    .builder()
                    .firstName("Robert")
                    .lastName("Makłowicz")
                    .age(12)
//                    .cash(BigDecimal.valueOf(200F))
                    .cash(200F)
                    .build(),
            Client
                    .builder()
                    .firstName("Krzysztof")
                    .lastName("Kolump")
                    .age(33)
//                    .cash(BigDecimal.valueOf(15000F))
                    .cash(15000F)
                    .build(),
            Client
                    .builder()
                    .firstName("Roberto")
                    .lastName("Kowalski")
                    .age(45)
//                    .cash(BigDecimal.valueOf(30000F))
                    .cash(30000F)
                    .build());

    private static List<Product> products = List.of(
            Product
                    .builder()
                    .name("Lenovo 22xPC")
                    .category(Category.COMPUTER.getName())
                    .price(6999.99F)
                    .build(),
            Product
                    .builder()
                    .name("Lenovo Mouse 22x")
                    .category(Category.MOUSE.getName())
                    .price(25.99F)
                    .build(),
            Product
                    .builder()
                    .name("Lenovo MousePadx13")
                    .category(Category.MOUSE_PAD.getName())
                    .price(10.99F)
                    .build(),
            Product
                    .builder()
                    .name("Asus RogStrix")
                    .category(Category.COMPUTER.getName())
                    .price(1299.99F)
                    .build(),
            Product
                    .builder()
                    .name("Asus Mouse Rocket")
                    .category(Category.MOUSE.getName())
                    .price(100.99F)
                    .build(),
            Product
                    .builder()
                    .name("Slate of cement")
                    .category(Category.MOUSE_PAD.getName())
                    .price(69.99F)
                    .build(),
            Product
                    .builder()
                    .name("Microsoft CXom")
                    .category(Category.COMPUTER.getName())
                    .price(16999.99F)
                    .build(),
            Product
                    .builder()
                    .name("Apple Magic mouseC")
                    .category(Category.MOUSE.getName())
                    .price(699.99F)
                    .build(),
            Product
                    .builder()
                    .name("Apple smartwatch")
                    .category(Category.SMARTWATCH.getName())
                    .price(700.99F)
                    .build()
    );

    public static void createDatabaseEntries() {
        var jdbi = DatabaseConnection.create(
                AppProperties
                        .getInstance()
                        .getAppProperties());
        //drop previous data and provide new records
        if (DatabaseConnection.checkIfDbExists(AppProperties.getInstance().getAppProperties())) {

            dropTablesIfExist(jdbi);

            var createClientTableSql = """
                    create table if not exists clients (
                    id integer primary key auto_increment,
                    firstName varchar(50) not null,
                    lastName varchar(50) not null,
                    age integer default 18,
                    cash integer default 0
                    );
                    """;

            var createProductTableSql = """
                    create table if not exists products (
                    id integer primary key auto_increment,
                    name varchar(50) not null,
                    category varchar(50) not null,
                    price integer default 0
                    );
                    """;

            var createOrderTableSql = """
                    CREATE TABLE IF NOT EXISTS orders (
                    id integer primary key auto_increment,
                    client_id int,
                    total_quantity int,
                    FOREIGN KEY (client_id) REFERENCES clients(id)
                    );""";

            var createOrderItemTableSql = """
                    CREATE TABLE IF NOT EXISTS order_item (
                    id integer primary key auto_increment,
                    /*quantity int, quantity is problematic, would be resolved when grouping by before insert would be applied*/
                    order_id int,
                    product_id int,
                    FOREIGN KEY (order_id) REFERENCES orders(id),
                    FOREIGN KEY (product_id) REFERENCES products(id)
                    );""";


            jdbi.useHandle(handle -> handle.execute(createClientTableSql));
            jdbi.useHandle(handle -> handle.execute(createProductTableSql));
            jdbi.useHandle(handle -> handle.execute(createOrderTableSql));
            jdbi.useHandle(handle -> handle.execute(createOrderItemTableSql));

            clientRepository = new ClientRepositoryImpl(jdbi);
            productRepository = new ProductRepositoryImpl(jdbi);
            orderReposiory = new OrderRepositoryImpl(jdbi);


            clientRepository.saveAll(clients);

            productRepository.saveAll(products);

            orderReposiory.saveAllOrders(createRandomOrders());
        }
        //for further use
//        var test = clientRepository.getByFields(List.of(
//                new TempField("firstName", "Maciej"),
//                new TempField("lastName", "Jaremowicz"),
//                new TempField("age", "25")
//        ));
    }

    public static void dropTablesIfExist(Jdbi jdbi) {
        try {
            jdbi.withHandle(handle -> {

                var tablesToDropCommands = handle.createQuery("""
                                SELECT concat('DROP TABLE IF EXISTS `', table_name, '`;')
                                FROM `information_schema`.`tables`
                                WHERE `table_schema` = 'shop';""")
                        .mapTo(String.class)
                        .list();
                tablesToDropCommands.add("SET FOREIGN_KEY_CHECKS=0;");
                Collections.rotate(tablesToDropCommands, 1);
                for (String command : tablesToDropCommands) {
                    handle.execute(command);
                }
                return true;
            });
        } catch (ResultSetException e) {
            LogManager.getLogger(DataProviderService.class).error("%s, exception message: %s".formatted(
                    "Error occurred while attempting to create database '%s'".formatted(
                            AppProperties
                                    .getInstance()
                                    .getAppProperties()
                                    .getProperty("databaseName")
                    ),
                    e.getMessage()
            ));
        }

    }


    public static void createFileEntries() {

        Function<String, List<List<String>>> func = (line) ->
                Arrays.stream(
                                line.split(" "))
                        .map(el ->
                                el
                                        .replace("[", "")
                                        .replace("]", ""))
                        .map(variable ->
                                Arrays
                                        .stream(variable.split(";"))
                                        .toList())
                        .toList();

        //file mapper
        var mapper = new AbstractFromFileMapper<>(
                Client.class
                , Product.class);

        var result = mapper
                .getAllFromFile(
                        AppProperties
                                .getInstance()
                                .getAppProperties()
                                .getProperty("filePath"),
                        func);
    }

    public static void createJsonEntries() {
        var jsonRepo = new JsonOrderRepositoryImpl(
                Order.class, AppProperties
                .getInstance()
                .getAppProperties()
                .getProperty("jsonFileLocation"));
        jsonRepo.saveAllOrders(createRandomOrders());
    }


    private static List<Order> createRandomOrders() {
        var numberOfOrders = Integer.parseInt(AppProperties.getInstance().getAppProperties().getProperty("numberOfOrders"));
        var maxNumberOfProducts = Integer.parseInt(AppProperties.getInstance().getAppProperties().getProperty("maxNumberOfProducts"));

        var ordersToSave = new ArrayList<Order>();
        var rand = new Random();
        for (int i = 0; i <= numberOfOrders; i++) {
            ordersToSave.add(
                    new Order(clients.get(
                            rand.nextInt(clients.size() - 1)
                    ),
                            rand
                                    .ints(maxNumberOfProducts, 0, products.size())
                                    .mapToObj(obj -> products.get(obj))
                                    .toList()));
        }
        for (Order order : ordersToSave) {
            LogManager.getLogger(DataProviderService.class).info("Client: " + order.getClient() + " Buys: " + order.getProducts());
        }
        return ordersToSave;
    }

    private static List<Product> getRandomProducts(int size, List<Product> availableProducts) {
        Random rand = new Random();
        var result = rand
                .ints(size, 0, availableProducts.size())
                .mapToObj(obj -> availableProducts.get(obj))
                .toList();
        return List.of();
    }

}
