package com.ttn.bluebell.core.api;

import com.ttn.bluebell.durable.model.client.Client;

import java.util.Set;

/**
 * Created by ttnd on 29/9/16.
 */
public interface ClientOperations {

   Client create(Client client);
   Set<Client> findAllClients();
   void delete(Long clientId);
   Client update(Long clientId, Client client);
    Client findById(Long clientId);

}
