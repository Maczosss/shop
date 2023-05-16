package org.application.service;

import org.application.model.Category;
import org.application.model.Client;
import org.application.model.Product;
import org.application.repository.ClientRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ClientServiceTest {

    @Mock
    private ClientRepositoryImpl clientRepository;

    private ClientService clientService;

    @BeforeEach
    void setUp() {
        clientService = new ClientService(clientRepository);
    }

    @Test
    void getHighestPayingCustomerExistTest() {
        // given
        var client = Client.builder()
                .firstName("John")
                .lastName("Doe")
                .cash(300f)
                .build();
        Mockito
                .when(clientRepository.getHighestPayingClient())
                .thenReturn(Optional.of(client));

        // when
        String result = clientService.getHighestPayingCustomer();

        // then
        Assertions.assertEquals("Highest paying customer is: John Doe", result);
    }

    @Test
    void getHighestPayingCustomerNotExistTest() {
        //given
        Mockito
                .when(clientRepository.getHighestPayingClient())
                .thenReturn(Optional.empty());

        // when
        String result = clientService.getHighestPayingCustomer();

        // then
        Assertions.assertEquals("No data was returned from data source.", result);
    }

    @Test
    void checkClientsDebtExist() {
        //given
        var map = new HashMap<Client, BigDecimal>();
        map.put(Client.builder().firstName("Adam").lastName("kowalski").build(), new BigDecimal(22));
        Mockito
                .when(clientRepository.checkClientsDebt())
                .thenReturn(map);

        // when
        String result = clientService.checkClientsDebt();

        // then
        Assertions.assertEquals("Clients with debt to pay: \n" +
                "Adam kowalski, with id: null, has debt: 22.\n", result);
    }

    @Test
    void checkClientsDebtNotExist() {
        //given
        Mockito
                .when(clientRepository.checkClientsDebt())
                .thenReturn(null);

        // when
        String result = clientService.checkClientsDebt();

        // then
        Assertions.assertEquals("No data was returned from data source.", result);
    }

    @Test
    void checkClientsDebtReturnsNegativeAndNullMapEntryAndKey() {
        //given
        var map = new HashMap<Client, BigDecimal>();
        map.put(Client.builder().firstName("Adam").lastName("kowalski").build(), new BigDecimal(22));
        map.put(Client.builder().firstName("Maciej").lastName("Jaworski").build(), new BigDecimal(-1));
        map.put(Client.builder().firstName("Kamil").lastName("Otter").build(), null);
        map.put(null, new BigDecimal(1));
        Mockito
                .when(clientRepository.checkClientsDebt())
                .thenReturn(map);

        // when
        String result = clientService.checkClientsDebt();

        // then
        Assertions.assertEquals("Clients with debt to pay: \n" +
                "Adam kowalski, with id: null, has debt: 22.\n", result);
    }

    @Test
    void getHighestPayingClientInCategory() {
        var client = Client.builder()
                .firstName("John")
                .lastName("Doe")
                .cash(300f)
                .build();
        Mockito
                .when(clientRepository.getHighestPayingClientInCategory(Category.COMPUTER))
                .thenReturn(Optional.of(client));

        // when
        String result = clientService.getHighestPayingClientInCategory(Category.COMPUTER.getName());

        // then
        Assertions.assertEquals("Highest paying customer in category computer is John Doe", result);
    }

    @Test
    void getHighestPayingClientInCategoryNotExist() {
        var client = Client.builder()
                .firstName("John")
                .build();
        Mockito
                .when(clientRepository.getHighestPayingClientInCategory(Category.COMPUTER))
                .thenReturn(Optional.of(client));

        // when
        String result = clientService.getHighestPayingClientInCategory(Category.COMPUTER.getName());

        // then
        Assertions.assertEquals("No data was returned from data source.", result);

        Mockito
                .when(clientRepository.getHighestPayingClientInCategory(Category.COMPUTER))
                .thenReturn(Optional.empty());

        // when
        String result2 = clientService.getHighestPayingClientInCategory(Category.COMPUTER.getName());
        // then
        Assertions.assertEquals("No data was returned from data source.", result);
    }

    @Test
    void getMostBoughtProductCategoryBasedOnAge() {
        //given
        var categoryMap = new HashMap<Integer, String>();
               categoryMap.put(1, "computer");
                categoryMap.put(2, null);
               categoryMap.put(null, "smartwatch");

        Mockito
                .when(clientRepository.getMostBoughtProductCategoryBasedOnAge())
                .thenReturn(categoryMap);

        // when
        String result = clientService.getMostBoughtProductCategoryBasedOnAge();

        // then
        Assertions.assertEquals("Most bought product category based on age: \n" +
                "in age group: 1, most bought category was: computer.\n", result);

        //given
        Mockito
                .when(clientRepository.getMostBoughtProductCategoryBasedOnAge())
                .thenReturn(null);

        // when
        String emptyResult = clientService.getMostBoughtProductCategoryBasedOnAge();

        // then
        Assertions.assertEquals("No data was returned from data source.", emptyResult);
    }

    @Test
    void getClientsThatBoughtTheMostProductsBasedOnCategory() {
        //given
        var categoryMap = new HashMap<Category, List<Client>>();
        var clientList = new LinkedList<Client>();
        clientList.add(Client
                .builder()
                .firstName("Robert")
                .lastName("Makłowicz")
                .age(12)
                .cash(200F)
                .build());
        clientList.add(Client
                .builder()
                .firstName("Krzysztof")
                .lastName("Kolump")
                .age(33)
                .cash(15000F)
                .build());
        clientList.add(null);

        categoryMap.put(Category.COMPUTER, clientList);
        categoryMap.put(null, clientList);
        categoryMap.put(Category.KEYBOARD, null);
        Mockito
                .when(clientRepository.getClientsThatBoughtTheMostProductsBasedOnCategory())
                .thenReturn(categoryMap);

        // when
        String result = clientService.getClientsThatBoughtTheMostProductsBasedOnCategory();

        // then
        Assertions.assertEquals("Clients that bought most products in category: \n" +
                "in category: computer, Robert Makłowicz and Krzysztof Kolump bought highest number of products.\n", result);

        //given
        Mockito
                .when(clientRepository.getClientsThatBoughtTheMostProductsBasedOnCategory())
                .thenReturn(null);

        // when
        String emptyResult = clientService.getClientsThatBoughtTheMostProductsBasedOnCategory();

        // then
        Assertions.assertEquals("No data was returned from data source.", emptyResult);
    }
    @Test
    void getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory() {
        //given
        //Map<String, Map<String, Product>>
        var minMaxAvgMap = new HashMap<String, Map<String, Product>>();
        var product1 = new HashMap<String, Product>();
        var product2 = new HashMap<String, Product>();
        var product3 = new HashMap<String, Product>();
        product1.put("10", Product.builder().id(1L).name("test product1").category(Category.COMPUTER.getName()).price(10F).build());
        product2.put("100", Product.builder().id(2L).name("test product2").category(Category.COMPUTER.getName()).price(100F).build());
        product3.put("55", new Product());

        minMaxAvgMap.put("Lowest value Product",product1);
        minMaxAvgMap.put("Highest value Product", product2);
        minMaxAvgMap.put("Average price of products in category %s".formatted(Category.COMPUTER.getName()), product3);

        Mockito
                .when(clientRepository.getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(Category.COMPUTER))
                .thenReturn(minMaxAvgMap);

        // when
        String result = clientService.getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(Category.COMPUTER.getName());

        // then
        Assertions.assertEquals("For category computer:\n" +
                "Highest value Product, test product2 with value 100 \n" +
                "Average price of products in category computer, 55\n" +
                "Lowest value Product, test product1 with value 10 \n", result);

        //given
        Mockito
                .when(clientRepository.getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(Category.COMPUTER))
                .thenReturn(null);

        // when
        String emptyResult = clientService.getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(Category.COMPUTER.getName());

        // then
        Assertions.assertEquals("No data was returned from data source.", emptyResult);
    }
}