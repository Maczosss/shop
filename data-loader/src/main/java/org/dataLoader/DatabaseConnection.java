package org.dataLoader;

import org.jdbi.v3.core.Jdbi;

import java.util.Properties;

public interface DatabaseConnection {
    static Jdbi create(Properties properties){
        return Jdbi.create(properties.getProperty("databaseURL"),
                properties.getProperty("databaseUser"),
                properties.getProperty("databasePassword"));
    }
}
