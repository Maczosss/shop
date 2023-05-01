package org.application.repository;

import org.application.model.Product;
import org.dataLoader.databaseMapper.AbstractCrudRepository;
import org.jdbi.v3.core.Jdbi;

public class ProductRepositoryImpl
        extends AbstractCrudRepository<Product, Long>
        implements ProductRepository{

    public ProductRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }
}
