package com.ttn.bluebell.core.service;

import com.ttn.bluebell.core.api.ClientOperations;
import com.ttn.bluebell.core.api.ProjectBasicOperations;
import com.ttn.bluebell.core.exception.EntityNotFoundException;
import com.ttn.bluebell.domain.client.EClient;
import com.ttn.bluebell.durable.model.client.Client;
import com.ttn.bluebell.repository.ClientRepository;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by ttnd on 29/9/16.
 */
@Service
public class ClientTemplate implements ClientOperations {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private DozerBeanMapper beanMapper;

    @Autowired
    ProjectBasicOperations projectBasicOperations;

    @Override
    public Client create(Client client) {

        EClient dbClient = beanMapper.map(client,EClient.class);
        dbClient = clientRepository.saveAndFlush(dbClient);
        client = beanMapper.map(dbClient,Client.class);
        return client;
    }

    @Override
    public Set<Client> findAllClients() {
        List<EClient> eClientList = clientRepository.findAllByActiveIsTrueOrderByClientNameAsc();
        Set<Client> clientList = eClientList.stream().map(eClient ->beanMapper.map(eClient,Client.class)).collect(Collectors.toSet());
        clientList = new TreeSet<Client>(clientList);
        return clientList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long clientId) {

        EClient eClient = findClientById(clientId);
        eClient.setActive(false);
        eClient.getProjectList().stream().forEach(
                eProject -> {
                    if(eProject.getActive())
                        projectBasicOperations.delete(eProject.getProjectId());
                }
        );
        clientRepository.saveAndFlush(eClient);
    }

    @Override
    public Client update(Long clientId, Client client) {

        clientExists(clientId);

        client.setClientId(clientId);
        EClient eClient = findClientById(clientId);
        beanMapper.map(client, eClient);
        eClient = clientRepository.saveAndFlush(eClient);

        return beanMapper.map(eClient, Client.class);

    }

    @Override
    public Client findById(Long clientId) {
        EClient eClient = findClientById(clientId);
        return beanMapper.map(eClient, Client.class);
    }

    public EClient findClientById(Long clientId) {
        EClient eClient = clientRepository.findByClientIdAndActiveIsTrue(clientId);
        if(eClient == null)
            throw new EntityNotFoundException("exception.entity.notfound.client");

        return eClient;
    }

    public boolean clientExists(Long clientId){

        return findClientById(clientId) != null;
    }
}
