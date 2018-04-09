package com.ttn.bluebell.core.api;

import com.ttn.bluebell.durable.model.project.InvoicingDTO;
import com.ttn.bluebell.durable.model.project.Project;
import com.ttn.bluebell.durable.model.project.TimeAndMoneyProject;
import org.apache.velocity.runtime.directive.Parse;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ttn on 1/11/16.
 */
public interface InvoiceOperations {
    Set<InvoicingDTO> getUpcomingBilling(ArrayList<String> regions) throws ParseException;
}
