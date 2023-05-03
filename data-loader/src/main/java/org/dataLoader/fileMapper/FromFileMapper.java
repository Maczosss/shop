package org.dataLoader.fileMapper;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Function;

public interface FromFileMapper<T, U> {

    Map<T, Map<U, Integer>> getAllFromFile(String path, Function<String, ?> function)
            throws NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException;
}
