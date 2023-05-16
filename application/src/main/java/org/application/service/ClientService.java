package org.application.service;

import org.application.model.Category;
import org.application.model.Client;
import org.application.model.Order;
import org.application.model.Product;
import org.application.repository.*;
import org.dataLoader.DatabaseConnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public class ClientService {

    private ClientRepository clientRepository;

    private Properties appProperties;

    public ClientService(ClientRepository repository) {
        this.clientRepository = repository;
    }

    public ClientService(DataSource source) {
        this.appProperties = new Properties();
        try {
            appProperties
                    .load(new FileInputStream("D:\\Sandbox\\KMShop\\shop\\application\\src\\main\\resources\\app.properties"));
        } catch (IOException e) {
//            throw new RuntimeException(e);
            System.out.println("well");
        }
        switch (source) {
            case DATABASE -> createDatabaseRepository();
            case TXT_FILE -> createTextFileRepository();//todo add repository
            case JSON_FILE -> createJsonFileRepository();//todo add repository
        }
    }

    //done
    public String getHighestPayingCustomer() {
        var client = clientRepository.getHighestPayingClient().orElse(null);
        if (client == null) {
            return noData();
        }
        return "Highest paying customer is: %s %s".formatted(client.getFirstName(), client.getLastName());
    }

    //done
    public String checkClientsDebt() {
        var debtMap = clientRepository.checkClientsDebt();
        if (debtMap == null) {
            return noData();
        }
        if (debtMap.size() > 0) {
            return "Clients with debt to pay: \n%s".formatted(debtMap.entrySet().stream()
                    .filter(e-> e.getValue()!=null && e.getKey()!=null)
                    .filter(debt -> debt.getValue().compareTo(BigDecimal.ZERO) != (-1))
                    .map(entry -> "%s %s, with id: %s, has debt: %s.\n".formatted(entry.getKey().getFirstName(), entry.getKey().getLastName(), entry.getKey().getId(), entry.getValue()))
                    .collect(Collectors.joining("")));
        } else {
            return "There are no clients with any debt at the moment";
        }
    }

    //done
    public String getHighestPayingClientInCategory(String category) {
        try {
            var tempCategory = Category.getCategory(category);
            var client = clientRepository.getHighestPayingClientInCategory(tempCategory).orElse(null);
            if (client == null || (client.getFirstName()==null || client.getLastName()==null)) {
                return noData();
            }
            return "Highest paying customer in category %s is %s %s".formatted(category, client.getFirstName(), client.getLastName());
        } catch (IllegalArgumentException e) {
            //todo add logging
            return e.getMessage();
        }
    }

    //done
    public String getMostBoughtProductCategoryBasedOnAge() {
        var ageProductMap = clientRepository.getMostBoughtProductCategoryBasedOnAge();
        if (ageProductMap == null) {
            return noData();
        }
        if (ageProductMap.size() > 0) {
            return "Most bought product category based on age: \n%s"
                    .formatted(
                            ageProductMap.entrySet().stream()
                                    .filter(entryTest ->
                                            entryTest.getKey()!=null &&
                                                    entryTest.getKey()>0 &&
                                                    entryTest.getValue()!=null &&
                                                    !entryTest.getValue().equals(""))
                                    .map(entry -> "in age group: %s, most bought category was: %s.\n".formatted(
                                            entry.getKey(), entry.getValue()))
                                    .collect(Collectors.joining("")));
        } else {
            return "No info on age groups was provided.";
        }
    }

    public String getClientsThatBoughtTheMostProductsBasedOnCategory() {
        var mostBoughtCategoryBasedOnProducts =
                clientRepository.getClientsThatBoughtTheMostProductsBasedOnCategory();
        if (mostBoughtCategoryBasedOnProducts == null) {
            return noData();
        }
        if (mostBoughtCategoryBasedOnProducts.size() > 0) {
            return "Clients that bought most products in category: \n%s"
                    .formatted(
                            mostBoughtCategoryBasedOnProducts.entrySet().stream()
                                    .filter(entryTest ->
                                            entryTest.getKey()!=null &&
                                            entryTest.getValue() !=null
                                            )
                                    .map(entry -> "in category: %s, %s bought highest number of products.\n"
                                            .formatted(
                                                    entry.getKey().getName(), entry.getValue()
                                                            .stream()
                                                            .filter(clientTest ->
                                                                    clientTest!=null &&
                                                                    clientTest.getFirstName()!=null &&
                                                                    clientTest.getLastName()!=null &&
                                                                    !clientTest.getFirstName().equals("") &&
                                                                    !clientTest.getLastName().equals(""))
                                                            .map((client) -> "%s %s"
                                                                    .formatted(
                                                                            client.getFirstName(),
                                                                            client.getLastName()
                                                                    ))
                                                            .collect(Collectors.joining(" and "))
                                            ))
                                    .collect(Collectors.joining("")));
        } else {
            return "No info on age groups was provided.";
        }
    }

    public String getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(String category) {
        try {
            var tempCategory = Category.getCategory(category);
            var productStatisticMap =
                    clientRepository
                            .getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(tempCategory);
            if (productStatisticMap == null) {
                return noData();
            }
            if (productStatisticMap.size() > 0) {

                return "For category %s:\n%s"
                        .formatted(category, productStatisticMap.entrySet().stream()
                                .map(
                                        entry -> {
                                            return "%s, %s\n".formatted(entry.getKey(),
                                                    entry.getValue().entrySet().stream()
                                                            .filter(entryTest -> entryTest.getValue()!=null)
                                                            .map(valueEntry -> {
                                                                if (valueEntry.getValue().getName() != null) {
                                                                    return "%s with value %s ".formatted(
                                                                            valueEntry.getValue().getName(),
                                                                            valueEntry.getKey());
                                                                } else {
                                                                    return valueEntry.getKey();
                                                                }
                                                            }).collect(Collectors.joining())
                                            );
                                        }
                                ).collect(Collectors.joining()));
            } else {
                return "No data on product statistics fetched.";
            }
        } catch (IllegalArgumentException e) {
            //todo add logging
            return e.getMessage();
        }
    }

    private void createDatabaseRepository() {
        if (appProperties.size() > 0)
            clientRepository = new ClientRepositoryImpl(DatabaseConnection.create(appProperties));
    }

    private void createTextFileRepository() {
    }

    private void createJsonFileRepository() {
        clientRepository = new JsonOrderRepositoryImpl(Order.class, appProperties.getProperty("jsonFileLocation"));
    }

    private String noData() {
        return "No data was returned from data source.";
    }
}
