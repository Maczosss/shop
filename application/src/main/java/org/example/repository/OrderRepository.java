package org.example.repository;

import com.mysql.cj.xdevapi.Client;
import org.example.model.Product;

import java.util.Map;

public interface OrderRepository<Order, Long> {


    public Map<Client, Map<Product, Integer>> getData();
}
