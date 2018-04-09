package com.ttn.bluebell.repository;

import com.ttn.bluebell.domain.project.ETimeAndMoneyProject;
import com.ttn.bluebell.durable.model.project.BillableProject;
import com.ttn.bluebell.durable.model.project.billing.InvoiceInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by praveshsaini on 22/9/16.
 */
@Repository
public interface TimeAndMoneyProjectRepository extends BillableProjectRepository<ETimeAndMoneyProject> {
   /* List<ETimeAndMoneyProject> findByBillingTypeAndActiveIsTrue(BillableProject.BillingType billingType);*/
    /*@Query(value = "select etmp from ETimeAndMoneyProject etmp where etmp.billingInfo.invoiceInfo.invoiceCycle = ?1 and etmp.active = ?2")
    List<ETimeAndMoneyProject> findByInvoiceCycle(InvoiceInfo.InvoiceCycle invoiceCycle,boolean active);
    @Query(value = "select etmp from ETimeAndMoneyProject etmp where etmp.billingInfo.invoiceInfo.invoiceCycle = ?1 and etmp.active = ?2 and etmp.region in (?3)")
    List<ETimeAndMoneyProject> findByInvoiceCycleAndRegionIn(InvoiceInfo.InvoiceCycle invoiceCycle,boolean active,ArrayList<String> region);*/
   @Query(value = "select etmp from ETimeAndMoneyProject etmp where etmp.region in (?1) and etmp.active = true")
    List<ETimeAndMoneyProject> findProjectByRegionAndActive(ArrayList<String> region);
}
