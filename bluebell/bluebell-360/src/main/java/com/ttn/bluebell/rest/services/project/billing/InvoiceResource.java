package com.ttn.bluebell.rest.services.project.billing;

import com.ttn.bluebell.core.service.InvoiceTemplate;
import com.ttn.bluebell.durable.model.project.InvoicingDTO;
import com.ttn.bluebell.durable.model.project.Project;
import com.ttn.bluebell.durable.model.project.TimeAndMoneyProject;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.*;

/**
 * Created by ttn on 1/11/16.
 */

@RestController
@RequestMapping("/invoice")
public class InvoiceResource {

    @Autowired
    private InvoiceTemplate invoiceTemplate;

    @RequestMapping(value = "/upcoming/billing-info",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = InvoicingDTO.class,responseContainer ="Set"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Set<InvoicingDTO> getUpcomingBilling(@ApiParam(name ="upcomingBilling",value ="upcomingBilling List of regions eg:{'regions':[regionlist]}") @RequestBody LinkedHashMap<String,Object> upcomingBilling){
        try {
            return invoiceTemplate.getUpcomingBilling((ArrayList<String>) upcomingBilling.get("regions"));
        }catch(ParseException pe){
            return null;
        }
    }

}
