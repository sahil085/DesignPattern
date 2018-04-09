package com.ttn.bluebell.durable.model.event.notification.payload;

import com.ttn.bluebell.durable.model.event.notification.dto.DeAllocationIntimationRequestDTO;

import java.util.List;

/**
 * Created by deepak on 3/1/18.
 */
public class DeAllocationIntimationRequestPayload {
    List<DeAllocationIntimationRequestDTO> deAllocationIntimationRequestDTOList;

    public List<DeAllocationIntimationRequestDTO> getDeAllocationIntimationRequestDTOList() {
        return deAllocationIntimationRequestDTOList;
    }

    public void setDeAllocationIntimationRequestDTOList(List<DeAllocationIntimationRequestDTO> deAllocationIntimationRequestDTOList) {
        this.deAllocationIntimationRequestDTOList = deAllocationIntimationRequestDTOList;
    }
}
