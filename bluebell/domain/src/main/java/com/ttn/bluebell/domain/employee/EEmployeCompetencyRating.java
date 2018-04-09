package com.ttn.bluebell.domain.employee;

import com.ttn.bluebell.domain.BaseModel;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * Created by ttnd on 5/10/16.
 */
@Entity
@Table(name = "EMPLOYEE_COMPETENCY_RATING")
public class EEmployeCompetencyRating extends BaseModel{

    @Id
    @Column(name = "EMP_CODE", nullable = false)
    private String code;

    @Embedded
    private ECompetency competency;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ECompetency getCompetency() {
        return competency;
    }

    public void setCompetency(ECompetency competency) {
        this.competency = competency;
    }
    
    @Override
    public boolean equals(Object obj) {
    	EEmployeCompetencyRating rating = (EEmployeCompetencyRating)obj;
    	return (this.code.equals(rating.getCode()) && this.getCompetency().getName().equals(rating.getCompetency().getName()));
    }
    
    @Override
    public int hashCode() {
    	return code.hashCode();
    }
}
