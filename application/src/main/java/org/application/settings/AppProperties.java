package org.application.settings;

import org.apache.logging.log4j.LogManager;
import org.application.repository.DataSource;
import org.dataLoader.DatabaseConnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class AppProperties {
    
    private static AppProperties instance;

    private static boolean dataSourceInitialized = false;
    private Properties appProperties;

    private AppProperties(){
    }

    public static AppProperties getInstance(){
        if(instance==null){
            synchronized (AppProperties.class){
                if(instance==null){
                    instance = new AppProperties();
                }
            }
        }
        return instance;
    }

    public AppProperties initialize(String path){
        if(appProperties==null) {
            this.appProperties = new Properties();
            try {
                this.appProperties
                        .load(new FileInputStream(path));
            } catch (IOException e) {
                LogManager.getLogger(getClass()).error("%s, exception message: %s".formatted(
                        "Error occurred while attempting to load properties file, file was not found",
                        e.getMessage()
                ));
                return null;
            }
        }
        return this;
    }

    public Properties getAppProperties(){
        return appProperties;}
    public boolean getDataSourceInitialized(){return dataSourceInitialized;}
    public void setDataSourceInitialized(boolean flag){dataSourceInitialized = flag;}

    public boolean checkDataSource(String source) {
        var dataSource = DataSource.getSource(source);
        switch(dataSource){
            case DATABASE -> dataSourceInitialized = DatabaseConnection.checkIfDbExists(appProperties);
            case JSON_FILE -> dataSourceInitialized = doesFileExist(appProperties.getProperty("jsonFileLocation")+"\\data.json");
            case TXT_FILE -> dataSourceInitialized = doesFileExist(appProperties.getProperty("filePath"));
        }
        return dataSourceInitialized;
    }

    private boolean doesFileExist(String filePath) {
        Path path = Paths.get(filePath);
       return Files.exists(path);
    }
}
