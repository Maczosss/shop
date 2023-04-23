package org.example.repository;

import org.example.databaseMapper.AbstractCrudRepository;
import org.example.model.Client;
import org.example.model.Product;
import org.jdbi.v3.core.Jdbi;

public class ProductRepositoryImpl
        extends AbstractCrudRepository<Product, Long>
        implements ProductRepository{

    public ProductRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }
}
