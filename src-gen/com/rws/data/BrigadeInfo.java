package com.rws.data;

import java.io.Serializable;
import java.util.Date;

public class BrigadeInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean isStore = false;
    
    public Boolean getIsStore() {
        return isStore;
    }
    
    public void setIsStore(Boolean isStore) {
        this.isStore = isStore;
    }
    private Integer brigadeId;
    
    public Integer getBrigadeId() {
        return brigadeId;
    }
    
    public void setBrigadeId(Integer brigadeId) {
        this.brigadeId = brigadeId;
    }
    private Integer capacity;
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
