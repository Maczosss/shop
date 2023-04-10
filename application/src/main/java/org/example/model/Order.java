package org.example.model;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Order {

    private Client client;
    private List<Product> products;
}
