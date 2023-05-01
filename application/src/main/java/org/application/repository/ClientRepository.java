package org.application.repository;

import org.application.model.Category;
import org.application.model.Client;

import java.math.BigDecimal;
import java.util.Map;

public interface ClientRepository {
    Client getHighestPayingClient();

    Client getHighestPayingClientInCategory(Category category);

    Map<Client, BigDecimal> checkClientsDebt();
}
