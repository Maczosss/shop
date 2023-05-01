package org.application.repository;

import com.mysql.cj.xdevapi.Client;
import org.dataLoader.databaseMapper.AbstractCrudRepository;
import org.application.model.Order;
import org.application.model.Product;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import java.util.List;
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

    @Override
    public Order saveOrder(Object orderToSave) {
        return null;
    }

    @Override
    public List<Order> saveAllOrders(List ordersToSave) {
        var orders = (List<Order>) ordersToSave;

        try(Handle handle = jdbi.open()){
            handle.begin();
            for(Order order: orders){
                var orderId = handle.createUpdate("insert into %s %s values %s;".formatted(
                    super.getTableName(),
                    "( %s, %s )".formatted(
                            "client_id",
                            "total_quantity"
                    ),
                        "( %s, %s )".formatted(
                                order.getClient().getId(),
                                order.getProducts().size())))
                        .executeAndReturnGeneratedKeys()
                        .mapTo(Long.class)
                        .one();

                for(Product product : order.getProducts()){
                    handle.createUpdate("insert into %s %s values %s;".formatted(
                            "order_item",
                            "( order_id, product_id )",
                            "( %s , %s )".formatted(
                                    orderId,
                                    product.getId()
                            )
                    ))
                            .execute();
                }
            }
            handle.commit();
        }catch(Exception e) {
            System.out.println("Error while creating orders tables" +  e.getMessage());
            throw new RuntimeException("Error while creating orders tables", e);
        }
        return orders;
    }
}
