package com.ttn.bluebell.domain.employee;

import com.ttn.bluebell.domain.BaseModel;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Employee_Resignations")
public class EmployeeResignation extends BaseModel{
    @Id
    @Column(name = "EMP_CODE", nullable = false)
    private String employeeCode;
    @Lob
    @Column(name = "Exit_Note")
    private String exitNote;
    @Column(name ="Notice_Period")
    private Long noticePeriod;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Initiation_Date")
    private Date initiationDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Last_Working_Date")
    private Date lastWorkingDate;

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getExitNote() {
        return exitNote;
    }

    public void setExitNote(String exitNote) {
        this.exitNote = exitNote;
    }

    public Long getNoticePeriod() {
        return noticePeriod;
    }

    public void setNoticePeriod(Long noticePeriod) {
        this.noticePeriod = noticePeriod;
    }

    public Date getInitiationDate() {
        return initiationDate;
    }

    public void setInitiationDate(Date initiationDate) {
        this.initiationDate = initiationDate;
    }

    public Date getLastWorkingDate() {
        return lastWorkingDate;
    }

    public void setLastWorkingDate(Date lastWorkingDate) {
        this.lastWorkingDate = lastWorkingDate;
    }
}
