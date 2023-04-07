package org.example.repo;

import org.jdbi.v3.core.Jdbi;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public class AbstractCrudRepository<T, ID>
        implements CrudRepository<T, ID> {

    protected Jdbi jdbi;

    protected AbstractCrudRepository(Jdbi jdbi){
        this.jdbi = jdbi;
    }

    @SuppressWarnings("unchecked")
    private final Class<T> entityType = (Class<T>)
            ((ParameterizedType) super.getClass().getGenericSuperclass()).getActualTypeArguments()[0];


    @Override
    public Optional<T> save(T item) {
        return Optional.empty();
    }

    @Override
    public List<T> getAll() {
        return null;
    }
}
