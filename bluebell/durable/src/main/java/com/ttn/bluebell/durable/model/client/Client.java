package com.ttn.bluebell.durable.model.client;

import com.ttn.bluebell.durable.model.common.Address;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by ttnd on 28/9/16.
 */
public class Client implements Comparable{
    @ApiModelProperty(notes = "CLient Id")
    private Long clientId;

    @NotNull(message = "{client.name.required")
    @ApiModelProperty(notes = "Client Name",required = true)
    private String clientName;

    @Valid
    @NotNull(message  ="{client.address.required}")
    @ApiModelProperty(notes = "CLient Address",required = true)
    private Address address;

    private Boolean active=true;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public int compareTo(Object o) {
        Client clo = (Client) o;
        return this.clientName.compareTo(clo.clientName);
    }
}
