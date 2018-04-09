package com.ttn.bluebell.domain.client;

import com.ttn.bluebell.domain.BaseModel;
import com.ttn.bluebell.domain.common.EAddress;
import com.ttn.bluebell.domain.project.EProject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by ttnd on 29/9/16.
 */
@Entity
@Table(name = "CLIENT")
public class EClient extends BaseModel{

    @Id
    @Column(name = "CLIENT_ID")
    @GenericGenerator(name="idGenerator", strategy="increment")
    @GeneratedValue(generator="idGenerator")
    private Long clientId;

    @NotNull
    @Column(name = "CLIENT_NAME")
    private String clientName;


    @NotNull
    @Embedded
    private EAddress address;

    @NotNull
    @Column(name = "ACTIVE")
    private Boolean active;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EProject> projectList;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public EAddress getAddress() {
        return address;
    }

    public void setAddress(EAddress address) {
        this.address = address;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public List<EProject> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<EProject> projectList) {
        this.projectList = projectList;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
