package com.ttn.bluebell.durable.model.common;

import java.util.List;

public class RegionListDTO {

    private List<RegionDTO> data;
    private String message;
    private String statusCode;
    public List<RegionDTO> getData() {
        return data;
    }

    public void setData(List<RegionDTO> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
