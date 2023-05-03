package org.application.api;

import org.application.repository.DataSource;
import org.application.service.ClientService;

public class ResponseApi {

    private ClientService service;

    private ResponseApi(ClientService service){
        this.service=service;
    }
    private ClientService getService(){
        return service;
    }
    public static ClientService get(){

        //todo add DataSource to properties
        return new ResponseApi(new ClientService(DataSource.DATABASE)).getService();
    }

    public static String getHighestPayingCustomerInCategory(String category){
        return "";
    }
}
