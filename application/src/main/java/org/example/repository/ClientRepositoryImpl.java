package org.example.repository;

import org.example.databaseMapper.AbstractCrudRepository;
import org.example.model.Client;
import org.jdbi.v3.core.Jdbi;

public class ClientRepositoryImpl
        extends AbstractCrudRepository<Client, Long>
        implements ClientRepository{

    public ClientRepositoryImpl(Jdbi jdbi){
        super(jdbi);
    }
}
