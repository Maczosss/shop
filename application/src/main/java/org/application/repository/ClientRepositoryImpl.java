package org.application.repository;

import lombok.Data;
import org.application.model.Category;
import org.dataLoader.databaseMapper.AbstractCrudRepository;
import org.application.model.Client;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ClientRepositoryImpl
        extends AbstractCrudRepository<Client, Long>
        implements ClientRepository {

    public ClientRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    @Override
    public Client getHighestPayingClient() {
        var sql = """
                SELECT c.firstName, c.lastName, SUM(p.price) AS total_order_value
                FROM clients c
                JOIN orders o ON c.id = o.client_id
                JOIN order_item oi ON o.id = oi.order_id
                JOIN products p ON oi.product_id = p.id
                GROUP BY c.id
                ORDER BY total_order_value DESC
                """;

        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .mapToBean(Client.class)
                .first()
        );
    }
    @Override
    public Client getHighestPayingClientInCategory(Category category) {
        return null;
    }
    @Override
    public Map<Client, BigDecimal> checkClientsDebt() {
        var sql = """
                SELECT c.id, c.firstName, c.lastName, c.cash, total_order_value, (c.cash - total_order_value) AS debt
                FROM clients c
                JOIN (
                    SELECT o.client_id, SUM(p.price) AS total_order_value
                    FROM orders o
                    JOIN order_item oi ON o.id = oi.order_id
                    JOIN products p ON oi.product_id = p.id
                    GROUP BY o.client_id
                ) AS orders_by_client ON c.id = orders_by_client.client_id
                HAVING debt<0
                ORDER BY debt DESC;
                """;
        //todo ogarnąć opcję z where, prawdopodobnie nie da się bez subquery
        var clientsWithDebt = new AtomicReference<List<ClientDebtMapper>>(List.of());

        try(Handle handle = jdbi.open()){
           clientsWithDebt.set(
                    handle
                            .registerRowMapper(new ClientDebtMapper())
                            .createQuery(sql)
                            .mapTo(ClientDebtMapper.class)
                            .list());
            for(ClientDebtMapper client: clientsWithDebt.get()){
                System.out.printf(
                        "%s %s, has debt %s%n", client.getCLient().getFirstName(),
                        client.getCLient().getLastName(),
                        client.getDebt()
                );
            }
        }
        return clientsWithDebt
                .get()
                .stream()
                .collect(Collectors
                        .toMap(
                                ClientDebtMapper::getCLient,
                                ClientDebtMapper::getDebt));
    }
}

@Data
class ClientDebtMapper implements RowMapper<ClientDebtMapper> {
    private Client cLient;
    private BigDecimal debt;

    public ClientDebtMapper() {
    }

    public ClientDebtMapper(Client cLient, BigDecimal debt) {
        this.cLient = cLient;
        this.debt = debt;
    }

    @Override
    public ClientDebtMapper map(ResultSet rs, StatementContext ctx) throws SQLException {
        var entry =
                new ClientDebtMapper(new Client((long) rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName")),
                        BigDecimal.valueOf(rs.getInt("debt")).abs());
        return entry;
    }
}
