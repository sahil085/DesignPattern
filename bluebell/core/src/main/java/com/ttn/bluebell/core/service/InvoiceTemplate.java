package com.ttn.bluebell.core.service;

import com.ttn.bluebell.core.api.InvoiceOperations;
import com.ttn.bluebell.core.mapper.project.TimeAndMoneyProjectMapper;
import com.ttn.bluebell.domain.project.EFixedPriceProject;
import com.ttn.bluebell.domain.project.EProject;
import com.ttn.bluebell.domain.project.ETimeAndMoneyProject;
import com.ttn.bluebell.domain.project.billing.EBillingMilestone;
import com.ttn.bluebell.durable.model.project.BillableProject;
import com.ttn.bluebell.durable.model.project.InvoicingDTO;
import com.ttn.bluebell.durable.model.project.Project;
import com.ttn.bluebell.durable.model.project.TimeAndMoneyProject;
import com.ttn.bluebell.durable.model.project.billing.InvoiceInfo;
import com.ttn.bluebell.repository.FixedPriceProjectRepository;
import com.ttn.bluebell.repository.TimeAndMoneyProjectRepository;
import org.apache.velocity.runtime.directive.Parse;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.Months;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by ttn on 1/11/16.
 */

@Service
public class InvoiceTemplate implements InvoiceOperations {

    @Autowired
    private TimeAndMoneyProjectRepository timeAndMoneyProjectRepository;

    @Autowired
    private FixedPriceProjectRepository fixedPriceProjectRepository;

    @Override
    public Set<InvoicingDTO> getUpcomingBilling(ArrayList<String> regions) throws ParseException {
        List<ETimeAndMoneyProject> timeAndMoneyProjects = new ArrayList<>();
        List<EFixedPriceProject> fixedPriceProjects = new ArrayList<>();
        Set<InvoicingDTO> list = new TreeSet<>();

        Map<String, List<TimeAndMoneyProject>> regionWiseBillingCycle = new HashMap<>();
        if (regions != null && !regions.isEmpty()) {
            timeAndMoneyProjects.addAll(timeAndMoneyProjectRepository.findProjectByRegionAndActive(regions));
            fixedPriceProjects.addAll(fixedPriceProjectRepository.findProjectByRegionAndActive(regions));
        }

        for (ETimeAndMoneyProject eTimeAndMoneyProject : timeAndMoneyProjects) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
            DateTime startDate = new DateTime(eTimeAndMoneyProject.getBillingInfo().getInvoiceInfo().getInvoiceStartDate());
            DateTime currDate = new DateTime(fmt.parse(LocalDate.now().toString() + " 05:30:00"));
            InvoiceInfo.InvoiceCycle frequency = eTimeAndMoneyProject.getBillingInfo().getInvoiceInfo().getInvoiceCycle();
            int diff = 0;
            DateTime invDate = null;
            InvoicingDTO dto = null;
            switch (frequency) {
                case Monthly:
                    int dayofMonth = startDate.getDayOfMonth();
                    if (dayofMonth < currDate.getDayOfMonth()) {
                        invDate = startDate.plusMonths(Months.monthsBetween(startDate, currDate).getMonths() + 1);
                        diff = Days.daysBetween(invDate, currDate).getDays();
                        if (diff <= 7) {
                            dto = new InvoicingDTO();
                            dto.setProjectName(eTimeAndMoneyProject.getProjectName());
                            dto.setRegion(eTimeAndMoneyProject.getRegionvalue().getRegionName());
                            dto.setInvDate(invDate.toDate());
                            list.add(dto);
                        }
                    } else {
                        diff = dayofMonth - currDate.getDayOfMonth();
                        if (diff <= 7) {
                            dto = new InvoicingDTO();
                            dto.setProjectName(eTimeAndMoneyProject.getProjectName());
                            dto.setRegion(eTimeAndMoneyProject.getRegionvalue().getRegionName());
                            dto.setInvDate(currDate.plusDays(diff).toDate());
                            list.add(dto);
                        }
                    }

                    break;
                case FortNight:
                    diff = Days.daysBetween(startDate, currDate).getDays();
                    diff = diff % 14;
                    if (diff >= 7) {
                        dto = new InvoicingDTO();
                        invDate = currDate.plusDays(14 - diff);
                        dto.setProjectName(eTimeAndMoneyProject.getProjectName());
                        dto.setRegion(eTimeAndMoneyProject.getRegionvalue().getRegionName());
                        dto.setInvDate(invDate.toDate());
                        list.add(dto);
                    }
                    break;
                case Weekly:
                    dto = new InvoicingDTO();
                    diff = Days.daysBetween(startDate, currDate).getDays();
                    diff = diff % 7;
                    invDate = currDate.plusDays(7 - diff);
                    dto.setProjectName(eTimeAndMoneyProject.getProjectName());
                    dto.setRegion(eTimeAndMoneyProject.getRegionvalue().getRegionName());
                    dto.setInvDate(invDate.toDate());
                    list.add(dto);
            }
        }

        for (EFixedPriceProject eFixedPriceProject : fixedPriceProjects) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
            DateTime currDate = new DateTime(fmt.parse(LocalDate.now().toString() + " 05:30:00"));
            DateTime offsetDate = currDate.plusDays(7);

            List<EBillingMilestone> milestones = eFixedPriceProject.getBillingInfo().getBillingMilestones();

            for (EBillingMilestone milestone : milestones) {
                DateTime invDate = new DateTime(milestone.getTentativeDate());
                if (invDate.isBefore(offsetDate) && invDate.isAfter(currDate) || invDate.isEqual(currDate) || invDate.isEqual(offsetDate)) {
                    InvoicingDTO dto = new InvoicingDTO();
                    dto.setProjectName(eFixedPriceProject.getProjectName());
                    dto.setRegion(eFixedPriceProject.getRegionvalue().getRegionName());
                    dto.setInvDate(invDate.toDate());
                    list.add(dto);
                    break;
                }
            }

        }
        return list;
    }

}
