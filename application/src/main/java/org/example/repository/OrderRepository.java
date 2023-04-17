package org.example.repository;

import com.mysql.cj.xdevapi.Client;
import org.example.model.Product;

import java.util.List;
import java.util.Map;

public interface OrderRepository<Order, Long> {


    Map<Client, Map<Product, Integer>> getData();

    Order saveOrder(Order orderToSave);

    List<Order> saveAllOrders(List<Order> ordersToSave);
}
