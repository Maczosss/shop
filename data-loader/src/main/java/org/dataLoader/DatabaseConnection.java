package org.dataLoader;

import org.jdbi.v3.core.Jdbi;

import java.util.Properties;

public interface DatabaseConnection {
    static Jdbi create(Properties properties){
        return Jdbi.create(properties.getProperty("databaseURL"),
                properties.getProperty("databaseUser"),
                properties.getProperty("databasePassword"));
    }

    static boolean checkIfDbExists(Properties properties) {

        Jdbi jdbi = Jdbi.create(properties.getProperty("databaseURL"),
                properties.getProperty("databaseUser"),
                properties.getProperty("databasePassword"));

        String databaseName = properties.getProperty("databaseName");

        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = :dbName")
                        .bind("dbName", databaseName)
                        .mapTo(String.class)
                        .findFirst()
                        .isPresent()
        );
    }
}
