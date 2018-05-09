package com.rws.data;

import java.io.Serializable;
import java.util.Date;

public class TrainInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean isStore = false;
    
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
    private Integer trainType;
    
    public Integer getTrainType() {
        return trainType;
    }
    
    public void setTrainType(Integer trainType) {
        this.trainType = trainType;
    }
    private Integer trainLength;
    
    public Integer getTrainLength() {
        return trainLength;
    }
    
    public void setTrainLength(Integer trainLength) {
        this.trainLength = trainLength;
    }
    private Integer trainWagonToService;
    
    public Integer getTrainWagonToService() {
        return trainWagonToService;
    }
    
    public void setTrainWagonToService(Integer trainWagonToService) {
        this.trainWagonToService = trainWagonToService;
    }
}
