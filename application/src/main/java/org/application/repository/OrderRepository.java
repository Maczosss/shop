package org.application.repository;

import java.util.List;

public interface OrderRepository<Order, Long> {
    Order saveOrder(Order orderToSave);

    List<Order> saveAllOrders(List<Order> ordersToSave);
}
