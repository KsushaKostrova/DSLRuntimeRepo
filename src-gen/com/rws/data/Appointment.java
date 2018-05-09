package com.rws.data;

import java.io.Serializable;
import java.util.Date;

public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean isStore = true;
    
    public Boolean getIsStore() {
        return isStore;
    }
    
    public void setIsStore(Boolean isStore) {
        this.isStore = isStore;
    }
    private Integer trainId;
    
    public Integer getTrainId() {
        return trainId;
    }
    
    public void setTrainId(Integer trainId) {
        this.trainId = trainId;
    }
    private Integer brigadeId;
    
    public Integer getBrigadeId() {
        return brigadeId;
    }
    
    public void setBrigadeId(Integer brigadeId) {
        this.brigadeId = brigadeId;
    }
    private Integer serviceId;
    
    public Integer getServiceId() {
        return serviceId;
    }
    
    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }
    private Date timeStart;
    
    public Date getTimeStart() {
        return timeStart;
    }
    
    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }
    private Date timeEnd;
    
    public Date getTimeEnd() {
        return timeEnd;
    }
    
    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }
}
