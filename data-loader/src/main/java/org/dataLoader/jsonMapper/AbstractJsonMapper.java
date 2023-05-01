package org.dataLoader.jsonMapper;

import com.squareup.moshi.*;
import java.io.IOException;
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
                            .readString(Paths.get(path)));
            return tempList;
        } catch (IOException e) {
            System.out.println("No json file found in specified path!");
            return List.of();
        }
    }
}
