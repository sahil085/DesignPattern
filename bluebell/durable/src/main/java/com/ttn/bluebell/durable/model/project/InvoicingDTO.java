package com.ttn.bluebell.durable.model.project;

import java.util.Date;

/**
 * Created by ttnd on 22/12/16.
 */
public class InvoicingDTO implements Comparable{

    private String projectName;

    private String region;

    private Date invDate;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Date getInvDate() {
        return invDate;
    }

    public void setInvDate(Date invDate) {
        this.invDate = invDate;
    }

    @Override
    public int compareTo(Object o) {
        InvoicingDTO obj = (InvoicingDTO) o;
        int dateComp = this.getInvDate().compareTo(obj.invDate);

        if(dateComp == 0) {
            return this.projectName.compareTo(obj.getProjectName());
        }else{
            return dateComp;
        }
    }
}
