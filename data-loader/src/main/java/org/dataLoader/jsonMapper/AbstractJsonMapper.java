package org.dataLoader.jsonMapper;

import com.squareup.moshi.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class AbstractJsonMapper<T>{


    private Class<T> entityType;
    private Moshi moshi = new Moshi.Builder().build();
    private JsonAdapter<T> entityAdapter;
    private JsonAdapter<List<T>> jsonListEntityAdapter;

    public AbstractJsonMapper(Class<T> entityType) {
        this.entityType = entityType;
        this.entityAdapter = moshi.adapter(entityType);
        this.jsonListEntityAdapter = moshi.adapter(Types.newParameterizedType(List.class, this.entityType));
    }

    public List<T> getFromJsonFile(String path){
        try {
            var tempList = jsonListEntityAdapter
                    .fromJson(Files
                            .readString(Paths.get(path+"\\data.json")));
            return tempList;
        } catch (IOException e) {
            System.out.println("No json file found in specified path!");
            return List.of();
        }
    }

    public void save(List<T> t, String path){

        //todo resolve issue whith existing data.json file
        var jsonOrdersList = jsonListEntityAdapter.toJson(t);
        File file = new File(path + "\\data.json");

        if (file.exists()) {
            append(t, path);
            return;
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonOrdersList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void append(List<T> t, String path){
        File file = new File(path + "\\data.json");

        if (!file.exists()) {
            save(t, path);
            return;
        }

        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
            List<T> existingElements = jsonListEntityAdapter.fromJson(content.toString());

            if (existingElements != null) {
                existingElements.addAll(t);
            }

            String updatedJson = jsonListEntityAdapter.toJson(existingElements);

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(updatedJson);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
