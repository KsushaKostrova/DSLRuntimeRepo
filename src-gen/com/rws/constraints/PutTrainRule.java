package com.rws.constraints;

import java.io.Serializable;

public class PutTrainRule implements Serializable {
    private static final long serialVersionUID = 1L;
    private String trainLengthConstraint = "trainLength < = all trainLength";
    
    public String getTrainLengthConstraint() {
        return trainLengthConstraint;
    }
    
    public void setTrainLengthConstraint(String trainLengthConstraint) {
        this.trainLengthConstraint = trainLengthConstraint;
    }
    private String wagonsConstraint = "wagonLength * smth < = totalLength";
    
    public String getWagonsConstraint() {
        return wagonsConstraint;
    }
    
    public void setWagonsConstraint(String wagonsConstraint) {
        this.wagonsConstraint = wagonsConstraint;
    }
}
