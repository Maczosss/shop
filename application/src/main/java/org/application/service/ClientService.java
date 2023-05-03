package org.application.service;

import org.application.model.Category;
import org.application.model.Client;
import org.application.model.Product;
import org.application.repository.ClientRepository;
import org.application.repository.ClientRepositoryImpl;
import org.application.repository.DataSource;
import org.dataLoader.DatabaseConnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ClientService {

    private ClientRepository clientRepository;
    public ClientService(DataSource source){
        var appProperties = new Properties();
        try {
            appProperties
                    .load(new FileInputStream("PATH_TO_PROPERTIES"));
        } catch (IOException e) {
//            throw new RuntimeException(e);
            System.out.println("well");
        }
        switch(source){
            case DATABASE -> createDatabaseRepository(appProperties);
            case TXT_FILE -> createTextFileRepository();//todo add repository
            case JSON_FILE -> createJsonFileRepository();//todo add repository
        }
    }
    //done
    public Client getHighestPayingCustomer(){
        return clientRepository.getHighestPayingClient();
    }

    //done
    public Map<Client, BigDecimal> checkClientsDebt(){
        return clientRepository.checkClientsDebt();
    }
    //done
    public Client getHighestPayingClientInCategory(Category category){
        return clientRepository.getHighestPayingClientInCategory(category);
    }
    //done
    public Map<Integer, String> getMostBoughtProductCategoryBasedOnAge(){
        return clientRepository.getMostBoughtProductCategoryBasedOnAge();
    }
    public Map<Category, List<Client>> getClientsThatBoughtTheMostProductsBasedOnCategory(){
        return clientRepository.getClientsThatBoughtTheMostProductsBasedOnCategory();
    }

    public Map<String,Map<String, Product>> getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(Category category){
        return clientRepository.getMapWithAverageMaxAndMinValuesForProductsInSpecifiedCategory(category);
    }
    private void createDatabaseRepository(Properties appProperties) {
        clientRepository = new ClientRepositoryImpl(DatabaseConnection.create(appProperties));
    }
    private void createTextFileRepository() {
    }

    private void createJsonFileRepository() {

    }
}
