package org.application;

import org.application.dataProvider.DataProviderService;
import org.application.repository.DataSource;
import org.application.service.ClientService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        //properties download
        Properties appProperties = new Properties();
        try {
            appProperties
                    .load(new FileInputStream("D:\\Sandbox\\KMShop\\shop\\application\\src\\main\\resources\\app.properties"));
        } catch (IOException e) {
//            throw new RuntimeException(e);
            System.out.println("well");
        }
        //end of properties

//        DataProviderService.createDatabaseEntries(appProperties);
        System.out.println(new ClientService(DataSource.DATABASE).checkClientsDebt());
    }
}