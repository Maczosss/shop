package org.application.service;

import org.application.model.Category;
import org.application.model.Client;
import org.application.repository.ClientRepository;
import org.application.repository.ClientRepositoryImpl;
import org.application.repository.DataSource;
import org.dataLoader.DatabaseConnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

public class ClientService {

    private ClientRepository clientRepository;
    public ClientService(DataSource source){
        var appProperties = new Properties();
        try {
            appProperties
                    .load(new FileInputStream("D:\\Sandbox\\KMShop\\shop\\application\\src\\main\\resources\\app.properties"));
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

    private void createDatabaseRepository(Properties appProperties) {
        clientRepository = new ClientRepositoryImpl(DatabaseConnection.create(appProperties));
    }

    public Client getHighestPayingCustomer(){
        return clientRepository.getHighestPayingClient();
    }

    public Map<Client, BigDecimal> checkClientsDebt(){
        return clientRepository.checkClientsDebt();
    }
    public Client getHighestPayingCustomerInCategory(Category category){
        return clientRepository.getHighestPayingClientInCategory(category);
    }

    private void createTextFileRepository() {
    }

    private void createJsonFileRepository() {

    }
}
