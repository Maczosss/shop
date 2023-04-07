package org.example.model;

import org.example.fileMapper.FromFileMapper;
import org.example.service.ConnectionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//import org.example.service.*

public class Order {

    private Map<Client, Map<Product, Integer>> clientOrders;

    private final ConnectionService connectionService;

    private List<String> paths = new ArrayList<>();

    private Order(List<String> paths, ConnectionService service){
        this.paths.addAll(paths);
        this.connectionService = service;
    }

    public Order init(List<String> paths, ConnectionService connectionService){
        return new Order(paths, connectionService);
    }

    public Map<Client, Map<Product, Integer>> getClientOrders(){
        return clientOrders;
    }

    public Order fetchData(){
        connectionService.getData(Product.class, Client.class);
        return this;
    }
}
