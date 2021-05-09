package com.database.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @Column(name = "book_id")
    private String bookId;

    @Column(name = "book_index")
    private String bookIndex;

    @Column(name = "book_classify")
    private String bookClassify;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "book_author")
    private String bookAuthor;

    @Column(name = "book_press")
    private String bookPress;

    @Column(name = "book_introduction")
    private String bookIntroduction;

    @Column(name = "book_restnum")
    private Integer bookRestNum;

    @Column(name = "book_totalnum")
    private Integer bookTotalNum;

    @Column(name = "book_price")
    private Integer bookPrice;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookIndex() {
        return bookIndex;
    }

    public void setBookIndex(String bookIndex) {
        this.bookIndex = bookIndex;
    }

    public String getBookClassify() {
        return bookClassify;
    }

    public void setBookClassify(String bookClassify) {
        this.bookClassify = bookClassify;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookPress() {
        return bookPress;
    }

    public void setBookPress(String bookPress) {
        this.bookPress = bookPress;
    }

    public String getBookIntroduction() {
        return bookIntroduction;
    }

    public void setBookIntroduction(String bookIntroduction) {
        this.bookIntroduction = bookIntroduction;
    }

    public Integer getBookRestNum() {
        return bookRestNum;
    }

    public void setBookRestNum(Integer bookRestNum) {
        this.bookRestNum = bookRestNum;
    }

    public Integer getBookTotalNum() {
        return bookTotalNum;
    }

    public void setBookTotalNum(Integer bookTotalNum) {
        this.bookTotalNum = bookTotalNum;
    }

    public Integer getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(Integer bookPrice) {
        this.bookPrice = bookPrice;
    }
}