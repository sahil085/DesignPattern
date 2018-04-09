package com.ttn.bluebell.rest.services.client;

import com.ttn.bluebell.core.api.ClientOperations;
import com.ttn.bluebell.durable.model.client.Client;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by ttnd on 29/9/16.
 */
@RestController
@RequestMapping("/client")
public class ClientResource {

    @Autowired
    private ClientOperations clientOperations;

    @RequestMapping(value = "",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response =  Client.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Client createClient(@ApiParam @Valid @RequestBody Client client){

        return  clientOperations.create(client);
    }

    @RequestMapping(value = "/{clientId}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response =  Client.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Client updateClient(@ApiParam(name ="clientId",value = "Client ID")@PathVariable Long clientId, @Valid @RequestBody Client client){

        return  clientOperations.update(clientId, client);
    }

    @RequestMapping(value = "/{clientId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response =  Client.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Client findClientById(@ApiParam(name = "clientId")@PathVariable Long clientId){

        return  clientOperations.findById(clientId);
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response =  Client.class,responseContainer = "Set"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Set<Client> findAllClients(){

        return  clientOperations.findAllClients();
    }

    @RequestMapping(value = "/{clientId}",
            method = RequestMethod.DELETE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public void deleteClient(@ApiParam(name = "clientId",required = true)@NotNull @PathVariable Long clientId){

          clientOperations.delete(clientId);
    }
}
