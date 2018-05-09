package com.rws.data;

import java.io.Serializable;
import java.util.Date;

public class WagonInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean isStore = false;
    
    public Boolean getIsStore() {
        return isStore;
    }
    
    public void setIsStore(Boolean isStore) {
        this.isStore = isStore;
    }
    private Integer wagonType;
    
    public Integer getWagonType() {
        return wagonType;
    }
    
    public void setWagonType(Integer wagonType) {
        this.wagonType = wagonType;
    }
    private Integer wagonLength;
    
    public Integer getWagonLength() {
        return wagonLength;
    }
    
    public void setWagonLength(Integer wagonLength) {
        this.wagonLength = wagonLength;
    }
}
