package org.example;

import org.example.databaseMapper.TempField;
import org.example.jsonMapper.AbstractJsonMapper;
import org.example.jsonModelClass.JsonModelObject;
import org.example.model.Client;
import org.example.model.Order;
import org.example.model.Product;
import org.example.repository.ClientRepositoryImpl;
import org.example.repository.OrderRepositoryImpl;
import org.example.repository.ProductRepositoryImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {

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

        //properties download
        Properties appProperties = new Properties();
        try {
            appProperties
                    .load(new FileInputStream("PATH"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //end of properties

        //file mapper
//        var mapper = new AbstractFromFileMapper<>(
//                Client.class
//                , Product.class);
//
//        var result = mapper
//                .getAllFromFile(
//                        appProperties.getProperty("filePath"),
//                        func);

//        var client = org.example.model.Client.builder()
//                .age(20)
//                .cash(15000f)
//                .firstName("Maciej")
//                .lastName("Jaremowicz")
//                .build();
//        var client2 = org.example.model.Client.builder()
//                .age(20)
//                .cash(2000f)
//                .firstName("Adam")
//                .lastName("Robertowicz")
//                .build();
//        var client3 = org.example.model.Client.builder()
//                .age(34)
//                .cash(3400f)
//                .firstName("Paweł")
//                .lastName("Zychowicz")
//                .build();
//        var client4 = org.example.model.Client.builder()
//                .age(55)
//                .cash(34000f)
//                .firstName("Maciej")
//                .lastName("Jaremowicz")
//                .build();


        //jsonMapper test

//        var mapper = new AbstractJsonMapper<>(Client.class, Product.class);
//        var json = mapper.mapToJson(client);
//        var json2 = mapper.mapToJson(client2);
//        var json3 = mapper.mapToJson(client3);
//        var json4 = mapper.mapToJson(client4);
//        var testResult = mapper.mapFromJson(json);


//        var mapper = new AbstractJsonMapper<>(JsonModelObject.class);
//        var result = mapper.getFromJsonFile(appProperties.getProperty("jsonPath"));

        var jdbi = DatabaseConnection.create(appProperties);

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
                product_id int,
                FOREIGN KEY (client_id) REFERENCES clients(id),
                FOREIGN KEY (product_id) REFERENCES products(id)
                );""";

//        jdbi.useHandle(handle-> handle.execute(createClientTableSql));
//        jdbi.useHandle(handle-> handle.execute(createProductTableSql));
//        jdbi.useHandle(handle -> handle.execute(createOrderTableSql));


        var clientRepository = new ClientRepositoryImpl(jdbi);
        var productRepository = new ProductRepositoryImpl(jdbi);
        var orderReposiory = new OrderRepositoryImpl(jdbi);


//        System.out.println(clientRepository.save(Client
//                .builder()
//                .firstName("Maciej")
//                .lastName("Jaremowicz")
//                .age(25)
//                .cash(2000F)
//                .build()));

//        System.out.println(clientRepository.saveAll(List.of(
//                Client
//                        .builder()
//                        .firstName("Maciej")
//                        .lastName("Jaremowicz")
//                        .age(25)
//                        .cash(2000F)
//                        .build(),
//                Client
//                        .builder()
//                        .firstName("Andrzej")
//                        .lastName("Kowalski")
//                        .age(50)
//                        .cash(1000000F)
//                        .build(),
//                Client
//                        .builder()
//                        .firstName("Robert")
//                        .lastName("Makłowicz")
//                        .age(12)
//                        .cash(200F)
//                        .build(),
//                Client
//                        .builder()
//                        .firstName("Krzysztof")
//                        .lastName("Kolumb")
//                        .age(33)
//                        .cash(15000F)
//                        .build(),
//                Client
//                        .builder()
//                        .firstName("Roberto")
//                        .lastName("Kowalski")
//                        .age(45)
//                        .cash(30000F)
//                        .build()
//        )));
//
//        System.out.println(productRepository.saveAll(List.of(
//                Product
//                        .builder()
//                        .name("Lenovo 22xPC")
//                        .category("Computer")
//                        .price(6999.99F)
//                        .build(),
//                Product
//                        .builder()
//                        .name("Lenovo Mouse 22x")
//                        .category("Mouse")
//                        .price(25.99F)
//                        .build(),
//                Product
//                        .builder()
//                        .name("Lenovo MousePadx13")
//                        .category("MousePad")
//                        .price(10.99F)
//                        .build(),
//                Product
//                        .builder()
//                        .name("Asus RogStrix")
//                        .category("Computer")
//                        .price(1299.99F)
//                        .build(),
//                Product
//                        .builder()
//                        .name("Asus Mouse Rocket")
//                        .category("Mouse")
//                        .price(100.99F)
//                        .build(),
//                Product
//                        .builder()
//                        .name("Slate of cement")
//                        .category("MousePad")
//                        .price(69.99F)
//                        .build(),
//                Product
//                        .builder()
//                        .name("Microsoft CXom")
//                        .category("Computer")
//                        .price(16999.99F)
//                        .build(),
//                Product
//                        .builder()
//                        .name("Apple MagicmouseC")
//                        .category("Mouse")
//                        .price(699.99F)
//                        .build(),
//                Product
//                        .builder()
//                        .name("Apple smartwatch")
//                        .category("Smartwatch")
//                        .price(700.99F)
//                        .build()
//        )));

//        System.out.println(orderReposiory.saveAll(List.of(
//                Order
//                        .builder()
//                        .client(clientRepository.getByFields(
//                                List.of(new TempField("Maciej", "Jaremowicz"))
//                        ).get(0))
//                        .products(List.of(
//                                Product
//                                        .builder()
//                                        .name("Test")
//                                        .price(0F)
//                                        .build()
//                        ))
//                        .build()
//        )));


        var test = clientRepository.getByFields(List.of(
                new TempField("firstName", "Maciej"),
                new TempField("lastName", "Jaremowicz"),
                new TempField("age", "25")
                ));

        System.out.println(test.get(0).equals(test.get(1)));



//        System.out.println(result);


    }
}