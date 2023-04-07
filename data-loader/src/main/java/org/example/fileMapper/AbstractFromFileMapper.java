package org.example.fileMapper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

public class AbstractFromFileMapper<T, U> implements FromFileMapper<T, U> {

    private final Class<T> entityType;
    private final Class<U> typeContent;

    //TODO not needed, need to remove
    private List<U> values = new ArrayList<>();

    public AbstractFromFileMapper(Class<T> entityType, Class<U> typeContent) {

        //TODO validate types on fields
        this.entityType = entityType;
        this.typeContent = typeContent;
    }
    @Override
    public Map<T, Map<U, Integer>> getAllFromFile(String path, Function keyMapper) {

        Map<T, Map<U, Integer>> output = new HashMap<>();
        try (var lines = Files.lines(Paths.get(path))) {
            lines.map(keyMapper).forEach(el -> {
                var key = createKey(((List<?>) el).get(0));
                var fields = new ArrayList<>((List<?>) el);
                fields.remove(0);
                var value = createValues((List<Object>) fields);
                output.put(key.get(), value);
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return output;
    }


    private Optional<T> createKey(Object el) {
        var first = (List<String>) el;

        var test = new String[first.size()];
        for (int i = 0; i < first.size(); i++) {
            test[i] = first.get(i);
        }

        var fields = entityType.getDeclaredFields();
        Class<?>[] parameters = new Class<?>[fields.length];
        for (int i = 0; i < fields.length; i++) {
            parameters[i] = fields[i].getType();
        }

        var values = getValues(parameters, test);

        try {
            var mappedTempKey = entityType
                    .getConstructor(parameters)
                    .newInstance(values.toArray());
            return Optional.of(mappedTempKey);
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 NoSuchMethodException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }
    private Map<U, Integer> createValues(List<Object> source) {
        for (Object el : source) {
            var first = (List<String>) el;
            var test = new String[first.size()];
            for (int i = 0; i < first.size(); i++) {
                test[i] = first.get(i);
            }
            var fields = typeContent.getDeclaredFields();
            Class<?>[] parameters = new Class<?>[fields.length];
            for (int i = 0; i < fields.length; i++) {
                parameters[i] = fields[i].getType();
            }
            var values = getValues(parameters, test);

            try {
                var mappedTempKey = typeContent
                        .getConstructor(parameters)
                        .newInstance(values.toArray());
                this.values.add(mappedTempKey);
            }
            catch (InstantiationException |
                     IllegalAccessException |
                     InvocationTargetException |
                     NoSuchMethodException e) {
                System.out.println(e.getMessage());
            }
        }
        //remove copies, and count reoccurrences
        Map<U, Integer> resultMap = new HashMap<>();
        Set<U> removedCopiesSet = new HashSet<U>(values);
        for(U element: removedCopiesSet){
            var occurences = Collections.frequency(values, element);
            resultMap.put(element, occurences);
        }
        values = new ArrayList<>();
        return resultMap;
    }
    private List<Object> getValues(Class<?>[] parameters, String[] stringValues) {
        //todo add support for other types
        List<Object> values = new ArrayList();
        for (int i = 0; i < parameters.length; i++) {
            switch (parameters[i].getSimpleName()) {
                case "Integer":
                    values.add(
                            Integer.valueOf(
                                    String.valueOf(stringValues[i])));
                    break;
                case "Float":
                    values.add(
                            Float.valueOf(
                                    String.valueOf(stringValues[i])));
                    break;
                default:
                    values.add(stringValues[i]);
                    break;
            }
        }
        return values;
    }
}
