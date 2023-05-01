package org.dataLoader.databaseMapper;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {

    Optional<T> save(T t);

    List<T> getAll();
}
