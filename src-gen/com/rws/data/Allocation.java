package com.rws.data;

import java.io.Serializable;
import java.util.Date;

public class Allocation implements Serializable {
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
    private Integer railwayId;
    
    public Integer getRailwayId() {
        return railwayId;
    }
    
    public void setRailwayId(Integer railwayId) {
        this.railwayId = railwayId;
    }
    private Integer startNick;
    
    public Integer getStartNick() {
        return startNick;
    }
    
    public void setStartNick(Integer startNick) {
        this.startNick = startNick;
    }
}
