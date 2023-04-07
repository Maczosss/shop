package org.example.repo;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID>{

    Optional<T> save(T item);

    List<T> getAll();

}
