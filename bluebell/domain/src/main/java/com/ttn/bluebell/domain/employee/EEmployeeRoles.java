package com.ttn.bluebell.domain.employee;

import com.ttn.bluebell.domain.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by ttn on 19/10/16.
 */

@Entity
@Table(name = "EMPLOYEE_ROLES")
public class EEmployeeRoles extends BaseModel{

    @Id
    @Column(name = "ID")
    @GenericGenerator(name="idGenerator", strategy="increment")
    @GeneratedValue(generator="idGenerator")
    private Long id;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "ROLES", nullable = false)
    private String role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
