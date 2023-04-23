package org.example.dataProvider;

import org.example.DatabaseConnection;
import org.example.databaseMapper.TempField;
import org.example.fileMapper.AbstractFromFileMapper;
import org.example.model.Category;
import org.example.model.Client;
import org.example.model.Order;
import org.example.model.Product;
import org.example.repository.*;

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
                    .cash(2000F)
                    .build(),
            Client
                    .builder()
                    .firstName("Andrzej")
                    .lastName("Kowalski")
                    .age(50)
                    .cash(1000000F)
                    .build(),
            Client
                    .builder()
                    .firstName("Robert")
                    .lastName("Makłowicz")
                    .age(12)
                    .cash(200F)
                    .build(),
            Client
                    .builder()
                    .firstName("Krzysztof")
                    .lastName("Kolump")
                    .age(33)
                    .cash(15000F)
                    .build(),
            Client
                    .builder()
                    .firstName("Roberto")
                    .lastName("Kowalski")
                    .age(45)
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

    public static void createDatabaseEntries(Properties appProperties){
        var jdbi = DatabaseConnection.create(appProperties);

        //TODO add creating shop database

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

        jdbi.useHandle(handle-> handle.execute(createClientTableSql));
        jdbi.useHandle(handle-> handle.execute(createProductTableSql));
        jdbi.useHandle(handle -> handle.execute(createOrderTableSql));
        jdbi.useHandle(handle -> handle.execute(createOrderItemTableSql));

       clientRepository = new ClientRepositoryImpl(jdbi);
       productRepository = new ProductRepositoryImpl(jdbi);
       orderReposiory = new OrderRepositoryImpl(jdbi);


        System.out.println(clientRepository.saveAll(clients));

        System.out.println(productRepository.saveAll(products));

        System.out.println(orderReposiory.saveAllOrders(createRandomOrders(
                10,
                3)));


        var test = clientRepository.getByFields(List.of(
                new TempField("firstName", "Maciej"),
                new TempField("lastName", "Jaremowicz"),
                new TempField("age", "25")
        ));
    }



    public static void createFileEntries(Properties appProperties){

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
                        appProperties.getProperty("filePath"),
                        func);
    }

    public static void createJsonEntries(Properties appProperties){


        var client = org.example.model.Client.builder()
                .age(20)
                .cash(15000f)
                .firstName("Maciej")
                .lastName("Jaremowicz")
                .build();
        var client2 = org.example.model.Client.builder()
                .age(20)
                .cash(2000f)
                .firstName("Adam")
                .lastName("Robertowicz")
                .build();
        var client3 = org.example.model.Client.builder()
                .age(34)
                .cash(3400f)
                .firstName("Paweł")
                .lastName("Zychowicz")
                .build();
        var client4 = org.example.model.Client.builder()
                .age(55)
                .cash(34000f)
                .firstName("Maciej")
                .lastName("Jaremowicz")
                .build();



//        var mapper = new AbstractJsonMapper<>(Client.class, Product.class);
//        var json = mapper.mapToJson(client);
//        var json2 = mapper.mapToJson(client2);
//        var json3 = mapper.mapToJson(client3);
//        var json4 = mapper.mapToJson(client4);
//        var testResult = mapper.mapFromJson(json);
//
//
//        var mapper = new AbstractJsonMapper<>(JsonModelObject.class);
//        var result = mapper.getFromJsonFile(appProperties.getProperty("jsonPath"));
    }


    private static List<Order> createRandomOrders(
            int numberOfOrders, //make random
            int maxNumberOfProducts //make random
            ) {
        //todo random number of products, now its hardcoded to 3
        var clients = clientRepository.getAll();
        var products = productRepository.getAll();
        var ordersToSave = new ArrayList<Order>();
        var rand = new Random();
        for(int i =0; i<=numberOfOrders; i++){
            ordersToSave.add(
                    new Order(clients.get(
                            rand.nextInt(clients.size()-1)
                    ),
                            rand
                                    .ints(maxNumberOfProducts, 0, products.size())
                                    .mapToObj(obj->products.get(obj))
                                    .toList()));
        }
        return ordersToSave;
    }

    private static List<Product> getRandomProducts(int size, List<Product> availableProducts){
        Random rand = new Random();

        var result = rand
                .ints(size, 0, availableProducts.size())
                .mapToObj(obj->availableProducts.get(obj))
                .toList();
        return List.of();
    }

}
