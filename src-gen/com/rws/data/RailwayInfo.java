package com.rws.data;

import java.io.Serializable;
import java.util.Date;

public class RailwayInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean isStore = false;
    
    public Boolean getIsStore() {
        return isStore;
    }
    
    public void setIsStore(Boolean isStore) {
        this.isStore = isStore;
    }
    private Integer railwayId;
    
    public Integer getRailwayId() {
        return railwayId;
    }
    
    public void setRailwayId(Integer railwayId) {
        this.railwayId = railwayId;
    }
    private Integer railwayType;
    
    public Integer getRailwayType() {
        return railwayType;
    }
    
    public void setRailwayType(Integer railwayType) {
        this.railwayType = railwayType;
    }
    private Integer totalLength;
    
    public Integer getTotalLength() {
        return totalLength;
    }
    
    public void setTotalLength(Integer totalLength) {
        this.totalLength = totalLength;
    }
    private Integer usefulLength;
    
    public Integer getUsefulLength() {
        return usefulLength;
    }
    
    public void setUsefulLength(Integer usefulLength) {
        this.usefulLength = usefulLength;
    }
}
