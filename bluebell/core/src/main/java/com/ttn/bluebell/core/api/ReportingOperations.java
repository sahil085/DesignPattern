package com.ttn.bluebell.core.api;

import com.ttn.bluebell.durable.model.report.AllocationReportDTO;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by ttnd on 2/11/16.
 */
public interface ReportingOperations {

    Map<String, Map<String, Map<String, Integer>>> regionWiseCompetencyDetail();

    Map<String, Map<String, Map<String, List<StaffRequest>>>> regionAndCompetencyWiseOpenNeedsDetail();

    List<Object> allocationSummary(LocalDate startDate, LocalDate endDate);

    List<AllocationReportDTO> allocationReportSummary(LocalDate startDate, LocalDate endDate);
}
