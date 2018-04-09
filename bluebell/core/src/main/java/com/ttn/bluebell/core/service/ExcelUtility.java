package com.ttn.bluebell.core.service;

import com.ttn.bluebell.core.api.EmployeeOperations;
import com.ttn.bluebell.core.api.EmployeeReviewerFeedbackOperations;
import com.ttn.bluebell.domain.employee.EEmployeeReviewerFeedback;
import com.ttn.bluebell.domain.employee.ERating;
import com.ttn.bluebell.durable.model.employee.Employee;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by ttnd on 30/12/16.
 */
@Service
public class ExcelUtility {

    @Autowired
    private EmployeeOperations employeeOperations;

    @Autowired
    private EmployeeReviewerFeedbackOperations employeeReviewerFeedbackOperations;

    private static XSSFSheet sheet;

    public void uploadEmployeeReviewData(InputStream iStream){
        loadExcel(iStream);

        Set<Employee> allEmployees = employeeOperations.findAllEmployeeByCompetency(null,false,true);
        Map<String, String> empMap = new HashMap<>();
        allEmployees.forEach(employee -> {
            empMap.put(employee.getCode(), employee.getEmailAddress());
        });

        List<EEmployeeReviewerFeedback> feedBackList = new ArrayList<>();

        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next();
        String currEmail = "";
        while(rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if(row.getCell(0)!=null  && !row.getCell(0).toString().equals("")){
                if(row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    currEmail = empMap.get(row.getCell(0).toString().substring(
                            0, row.getCell(0).toString().indexOf('.')
                    ));
                }else{
                    currEmail = empMap.get(row.getCell(0).toString());
                }
                feedBackList.add(createFeedBackObj(row, currEmail));
            }
            else if(!currEmail.equals("")){
                feedBackList.add(createFeedBackObj(row, currEmail));
            }
        }

        saveFeedback(feedBackList);
    }

    private void saveFeedback(List<EEmployeeReviewerFeedback> feedbackList){
        feedbackList.forEach(eEmployeeReviewerFeedback -> {
            employeeReviewerFeedbackOperations.save(eEmployeeReviewerFeedback);
        });
    }

    private EEmployeeReviewerFeedback createFeedBackObj(Row row, String email){
        EEmployeeReviewerFeedback employeeReviewerFeedback = new EEmployeeReviewerFeedback();
        employeeReviewerFeedback.setEmail(email);
        ERating rating = new ERating();

        if(row.getCell(1)!=null  && !row.getCell(1).toString().equals("")) {
            rating.setCycle(row.getCell(1).toString());
        }

        if(row.getCell(2)!=null  && !row.getCell(2).toString().equals("")) {
            rating.setValue(row.getCell(2).toString());
        }

        if(row.getCell(3)!=null  && !row.getCell(3).toString().equals("")) {
            rating.setFeedback(row.getCell(3).toString());
        }

        employeeReviewerFeedback.setRating(rating);

        return employeeReviewerFeedback;
    }

    private void loadExcel(InputStream iStream){
        try{
            Workbook workbook = new XSSFWorkbook(iStream);
            sheet = (XSSFSheet)workbook.getSheetAt(0);
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
}
