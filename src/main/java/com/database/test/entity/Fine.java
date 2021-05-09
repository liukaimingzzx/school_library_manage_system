package com.database.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Penalty")
public class Fine {
    @Id
    @Column(name = "penalty_id")
    private int penaltyId;

    @Column(name = "record_id")
    private int recordId;

    @Column(name = "email")
    private String email;

    @Column(name = "penalty_bill")
    private int penaltyBill;

    @Column(name = "penalty_state")
    private int penaltyState;

    public int getPenaltyId() {
        return penaltyId;
    }

    public void setPenaltyId(int penaltyId) {
        this.penaltyId = penaltyId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPenaltyBill() {
        return penaltyBill;
    }

    public void setPenaltyBill(int penaltyBill) {
        this.penaltyBill = penaltyBill;
    }

    public int getPenaltyState() {
        return penaltyState;
    }

    public void setPenaltyState(int penaltyState) {
        this.penaltyState = penaltyState;
    }
}
