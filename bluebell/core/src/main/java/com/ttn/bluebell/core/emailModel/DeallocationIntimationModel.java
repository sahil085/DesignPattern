package com.ttn.bluebell.core.emailModel;

import com.ttn.bluebell.domain.staffing.EStaffing;

import java.util.List;

/**
 * Created by arpit on 12/1/18.
 */
public class DeallocationIntimationModel {
    List<EStaffing> eStaffings;
    String regionName;
    String currentDate;

    public DeallocationIntimationModel(List<EStaffing> list, String regionName, String currentDate) {
        this.eStaffings = list;
        this.regionName = regionName;
        this.currentDate = currentDate;
    }

    public List<EStaffing> geteStaffings() {
        return eStaffings;
    }

    public void seteStaffings(List<EStaffing> eStaffings) {
        this.eStaffings = eStaffings;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
