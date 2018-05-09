package com.rws.data;

import java.io.Serializable;
import java.util.Date;

public class Service implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean isStore = false;
    
    public Boolean getIsStore() {
        return isStore;
    }
    
    public void setIsStore(Boolean isStore) {
        this.isStore = isStore;
    }
    private Integer serviceId;
    
    public Integer getServiceId() {
        return serviceId;
    }
    
    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }
    private Date standartDuration;
    
    public Date getStandartDuration() {
        return standartDuration;
    }
    
    public void setStandartDuration(Date standartDuration) {
        this.standartDuration = standartDuration;
    }
}
