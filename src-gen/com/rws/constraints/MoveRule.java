package com.rws.constraints;

import java.io.Serializable;

public class MoveRule implements Serializable {
    private static final long serialVersionUID = 1L;
    private String MoveRuleConstraint0 = "trainLength < = railwayTotalLength";
    
    public String getMoveRuleConstraint0() {
        return MoveRuleConstraint0;
    }
    
    public void setMoveRuleConstraint0(String MoveRuleConstraint0) {
        this.MoveRuleConstraint0 = MoveRuleConstraint0;
    }
    private String MoveRuleConstraint1 = "trainLength < = railwayUsefulLength";
    
    public String getMoveRuleConstraint1() {
        return MoveRuleConstraint1;
    }
    
    public void setMoveRuleConstraint1(String MoveRuleConstraint1) {
        this.MoveRuleConstraint1 = MoveRuleConstraint1;
    }
}
