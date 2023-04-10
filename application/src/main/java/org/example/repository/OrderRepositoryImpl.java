package org.example.repository;

import com.mysql.cj.xdevapi.Client;
import org.example.databaseMapper.AbstractCrudRepository;
import org.example.model.Order;
import org.example.model.Product;
import org.jdbi.v3.core.Jdbi;

import java.util.Map;

public class OrderRepositoryImpl
        extends AbstractCrudRepository<Order, Long>
        implements OrderRepository{

    public OrderRepositoryImpl(Jdbi jdbi){
        super(jdbi);
    }


    @Override
    public Map<Client, Map<Product, Integer>> getData() {
        return null;
    }
}
