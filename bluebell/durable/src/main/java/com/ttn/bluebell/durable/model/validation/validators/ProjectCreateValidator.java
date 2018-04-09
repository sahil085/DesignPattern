package com.ttn.bluebell.durable.model.validation.validators;

import com.ttn.bluebell.durable.enums.BillableType;
import com.ttn.bluebell.durable.model.project.FixedPriceProject;
import com.ttn.bluebell.durable.model.project.NonBillableProject;
import com.ttn.bluebell.durable.model.project.Project;
import com.ttn.bluebell.durable.model.project.TimeAndMoneyProject;
import com.ttn.bluebell.durable.model.project.billing.BillingMilestone;
import com.ttn.bluebell.durable.model.project.billing.FixedPriceBillingInfo;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;
import com.ttn.bluebell.durable.model.validation.ProjectCreate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * Created by ttnd on 30/9/16.
 */



public class ProjectCreateValidator implements ConstraintValidator<ProjectCreate,Project> {

    @Override
    public void initialize(ProjectCreate projectCreate) {

    }

    @Override
    public boolean isValid(Project project, ConstraintValidatorContext constraintValidatorContext) {

        boolean flag =true;
        constraintValidatorContext.disableDefaultConstraintViolation();

        if ( project.getEndDate()!=null && project.getEndDate().before(project.getStartDate() ) ){
            constraintValidatorContext.buildConstraintViolationWithTemplate("{end.date.exceed.project.start.date }").addPropertyNode("endDate").addConstraintViolation();
            flag = false;
        }

        if(project.getClient()== null || project.getClient().getClientId() == null){
            constraintValidatorContext.buildConstraintViolationWithTemplate("{client.name.required }").addPropertyNode("client").addConstraintViolation();
            flag = false;
        }

        if(project instanceof FixedPriceProject){
            FixedPriceProject fixedPriceProject = (FixedPriceProject)project;
            FixedPriceBillingInfo billingInfo = fixedPriceProject.getBillingInfo();
                BigDecimal sum= new BigDecimal(0.0);
            if(billingInfo.getBillingMilestones() != null)
                for(BillingMilestone milestone : billingInfo.getBillingMilestones()){
                    sum = sum.add(milestone.getAmount());
                    if(milestone.getTentativeDate().before(project.getStartDate())){
                        constraintValidatorContext.buildConstraintViolationWithTemplate("{tentativeDate.less.than.project.startDate }").addPropertyNode("billingInfo").addConstraintViolation();
                        flag = false;
                    }
                }

            if(sum.compareTo(billingInfo.getProjectValue().getAmount()) != 0 && billingInfo.getBillingMilestones() != null && billingInfo.getBillingMilestones().size() > 0){
                constraintValidatorContext.buildConstraintViolationWithTemplate("{billingMilestones.not.match.projectValue }").addPropertyNode("billingInfo").addConstraintViolation();
                flag = false;
            }

        }

        if(project instanceof TimeAndMoneyProject){
            TimeAndMoneyProject tnmProject = (TimeAndMoneyProject)project;
            if(tnmProject.getBillingInfo().getInvoiceInfo().getInvoiceStartDate().before(project.getStartDate())){
                constraintValidatorContext.buildConstraintViolationWithTemplate("{invoiceDate.less.than.project.startDate }").addPropertyNode("billingInfo").addConstraintViolation();
                flag = false;
            }

        }

        for(StaffRequest staffRequest : project.getStaffRequests()){
            if(staffRequest.getStartDate().before(project.getStartDate())){
                constraintValidatorContext.buildConstraintViolationWithTemplate("{staffRequest.startDate.less.than.project.startDate }").addPropertyNode("staffRequests").addConstraintViolation();
                flag = false;
            }
            if(staffRequest.getEndDate().after(project.getEndDate())){
                constraintValidatorContext.buildConstraintViolationWithTemplate("{staffRequest.endDate.greater.than.project.endDate }").addPropertyNode("staffRequests").addConstraintViolation();
                flag = false;
            }

            if(project instanceof NonBillableProject && !staffRequest.getBillableType().equals(BillableType.BILLABLE)){
                constraintValidatorContext.buildConstraintViolationWithTemplate("{staffRequest.cannot.be.billable }").addPropertyNode("staffRequests").addConstraintViolation();
                flag = false;
            }
        }


        return flag;
    }
}

