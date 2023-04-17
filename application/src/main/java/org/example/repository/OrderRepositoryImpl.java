package org.example.repository;

import com.mysql.cj.xdevapi.Client;
import org.example.databaseMapper.AbstractCrudRepository;
import org.example.model.Order;
import org.example.model.Product;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
//        var insertedRows = new AtomicReference<List<Order>>(List.of());
//        jdbi.useTransaction(transaction -> {
//            var sql = "insert into order values %s;".formatted(
//                    getTableName(),
//                    getColumnNamesForInsert(),
//                    objectsToSave.stream()
//                            .map(this::getValuesForInsert)
//                            .collect(Collectors.joining(", "))
//            );
//            var insertedRowCount = jdbi.withHandle(
//                    handle ->
//                            handle.execute(sql));
//            if(insertedRowCount>0){
//                insertedRows.set(findLast(insertedRowCount));
//            }
//        });
//        return insertedRows.get();
        return orders;
    }
    public boolean test(){
        jdbi.useTransaction(transaction -> {
            var sql = "insert into orders (client_id , product_id) values (1,2);";
            jdbi.withHandle(
                    handle ->
                            handle.execute(sql));
        });
        return true;
    }
}
