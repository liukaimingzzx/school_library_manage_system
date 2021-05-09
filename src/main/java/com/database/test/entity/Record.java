package com.database.test.entity;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name = "Record")
public class Record {

    @Id
    @Column(name = "record_id")
    private Integer recordId;

    @Column(name = "email")
    private String email;

    @Column(name = "book_id")
    private String bookId;

    @Column(name = "borrow_time")
    private String borrowTime;

    @Column(name = "return_time")
    private String returnTime;


    @Column(name = "record_state")
    private Integer recordState;

    @Column(name = "fine_state")
    private Integer fineState;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(String borrowTime) {
        this.borrowTime = borrowTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }


    public Integer getRecordState() {
        return recordState;
    }

    public void setRecordState(Integer recordState) {
        this.recordState = recordState;
    }

    public Integer getFineState() {
        return fineState;
    }

    public void setFineState(Integer fineState) {
        this.fineState = fineState;
    }
}
