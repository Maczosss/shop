package org.example;

import org.example.dataProvider.DataProviderService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        //properties download
        Properties appProperties = new Properties();
        try {
            appProperties
                    .load(new FileInputStream("PATH_TO_PROPERTIES"));
        } catch (IOException e) {
//            throw new RuntimeException(e);
            System.out.println("well");
        }
        //end of properties

        DataProviderService.createDatabaseEntries(appProperties);
    }
}