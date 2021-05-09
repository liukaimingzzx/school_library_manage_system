package com.database.test.repository;

import com.database.test.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface RecordRepository extends JpaRepository<Record,Integer> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "insert into Record (record_id, email, book_id, borrow_time, return_time, record_state, fine_state) values (?1,?2,?3,?4,?5,?6,?7)")
    void insertRecord(int recordId, String email, String bookId, String borrowTime, String returnTime, int recordState, int fineState);


    @Query(nativeQuery = true,value = "select * from Record order by record_id desc limit 1")
    List<Record> selectMaxRecordId();

    @Query(nativeQuery = true,value = "select * from Record where email=?1 and record_state=0")
    List<Record> selectRecordByEmail(String email);

    @Query(nativeQuery = true,value = "select * from Record where email=?1 and record_state<>0")
    List<Record> selectFinishedRecord(String email);


    @Query(nativeQuery = true,value = "select email from Record where record_id=?1")
    String selectUser(Integer recordId);

    @Query(nativeQuery = true,value = "select book_id from Record where record_id=?1")
    String selectBook(Integer recordId);

    @Query(nativeQuery = true,value = "select borrow_time from Record where record_id=?1")
    String selectOldTime(Integer recordId);


    @Query(nativeQuery = true,value = "select return_time from Record where record_id=?1")
    String selectNewTime(Integer recordId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update Record set return_time=?2,record_state=1 where record_id=?1")
    void returnBook(Integer recordId, String returnTime);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update Record set fine_state=1 where record_id=?1")
    void setFine(Integer recordId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update Record set record_state=2 ,return_time=?2 where record_id=?1")
    void setLost(Integer recordId,String returnTime);


    @Query(nativeQuery = true,value = "select record_id, User.email,user_name,Record.book_id,book_name,book_author, borrow_time, return_time,record_state,fine_state from Record,Book,User where book_name like concat('%',?1,'%') and Book.book_id=Record.book_id and User.email=Record.email")
    List<Map<String,Object>> selectBorrowRecordByBookName(String searchMessage);


    @Query(nativeQuery = true,value = "select record_id, User.email,user_name,Record.book_id,book_name,book_author, borrow_time, return_time,record_state,fine_state from Record,Book,User where Record.email=?1 and record_state=2 and Book.book_id=Record.book_id and User.email=Record.email")
    List<Map<String,Object>> selectLostRecord(String email);


    @Query(nativeQuery = true,value = "select record_id, User.email,user_name,Record.book_id,book_name,book_author, borrow_time, return_time,record_state,fine_state from Record,Book,User where Record.email=?1 and record_state=1 and Book.book_id=Record.book_id and User.email=Record.email")
    List<Map<String,Object>> selectOverRecord(String email);


    @Query(nativeQuery = true,value = "select record_id, borrow_time,return_time,User.email,User.user_name,Book.book_id,Book.book_name,Book.book_author from User,Book,Record where User.email=Record.email and Book.book_id=Record.book_id and Book.book_name=?1")
    List<Map<String,Object>> selectRecordAndBookByBookName(String bookName);

    @Query(nativeQuery = true,value = "select record_id, borrow_time,return_time,User.email,User.user_name,Book.book_id,Book.book_name,Book.book_author,record_state from User,Book,Record where User.email=Record.email and Book.book_id=Record.book_id and User.email=?1 and record_state<>0")
    List<Map<String,Object>> selectAllRecord(String email);

}
