package org.application.model;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Order {

    private Long id;
    private Client client;
    private List<Product> products;

    public Order(Client client, List<Product> products) {
        this.client = client;
        this.products = products;
    }
}
