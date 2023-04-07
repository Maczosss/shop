package org.example;

import org.example.fileMapper.AbstractFromFileMapper;
import org.example.model.Client;
import org.example.model.Order;
import org.example.model.Product;

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
                    .load(new FileInputStream("D:\\Sandbox\\KMShop\\shop\\application\\src\\main\\resources\\app.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //end of properties

        //file mapper
        var mapper = new AbstractFromFileMapper<>(
                Client.class
                , Product.class);

        var result = mapper
                .getAllFromFile(
                        appProperties.getProperty("filePath"),
                        func);

        //JsonMapper
        System.out.println(result);
    }
}