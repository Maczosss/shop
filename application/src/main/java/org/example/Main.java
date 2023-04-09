package org.example;

import org.example.jsonMapper.AbstractJsonMapper;
import org.example.jsonModelClass.JsonModelObject;

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
                    .load(new FileInputStream("PATH"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //end of properties

        //file mapper
//        var mapper = new AbstractFromFileMapper<>(
//                Client.class
//                , Product.class);
//
//        var result = mapper
//                .getAllFromFile(
//                        appProperties.getProperty("filePath"),
//                        func);

        var client = org.example.model.Client.builder()
                .age(20)
                .cash(15000f)
                .firstName("Maciej")
                .lastName("Jaremowicz")
                .build();
        var client2 = org.example.model.Client.builder()
                .age(20)
                .cash(2000f)
                .firstName("Adam")
                .lastName("Robertowicz")
                .build();
        var client3 = org.example.model.Client.builder()
                .age(34)
                .cash(3400f)
                .firstName("Paweł")
                .lastName("Zychowicz")
                .build();
        var client4 = org.example.model.Client.builder()
                .age(55)
                .cash(34000f)
                .firstName("Maciej")
                .lastName("Jaremowicz")
                .build();


        //jsonMapper test

//        var mapper = new AbstractJsonMapper<>(Client.class, Product.class);
//        var json = mapper.mapToJson(client);
//        var json2 = mapper.mapToJson(client2);
//        var json3 = mapper.mapToJson(client3);
//        var json4 = mapper.mapToJson(client4);
//        var testResult = mapper.mapFromJson(json);


        var mapper = new AbstractJsonMapper<>(JsonModelObject.class);
        var result = mapper.getFromJsonFile(appProperties.getProperty("jsonPath"));

        System.out.println(result);


    }
}