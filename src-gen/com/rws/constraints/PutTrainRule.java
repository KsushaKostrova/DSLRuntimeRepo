package com.rws.constraints;

public class PutTrainRule {
    private String trainLengthConstraint;
    
    public String getTrainLengthConstraint() {
        return trainLengthConstraint;
    }
    
    public void setTrainLengthConstraint(String trainLengthConstraint) {
        this.trainLengthConstraint = trainLengthConstraint;
    }
    private String wagonsConstraint;
    
    public String getWagonsConstraint() {
        return wagonsConstraint;
    }
    
    public void setWagonsConstraint(String wagonsConstraint) {
        this.wagonsConstraint = wagonsConstraint;
    }
}
