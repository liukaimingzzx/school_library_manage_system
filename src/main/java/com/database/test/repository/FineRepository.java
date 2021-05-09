package com.database.test.repository;

import com.database.test.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface FineRepository extends JpaRepository<Fine,String> {
    @Query(nativeQuery = true,value = "select penalty_id,Penalty.record_id,Penalty.email,Penalty.penalty_bill,Penalty.penalty_state,user_name,Record.book_id,book_name, borrow_time, return_time from Penalty,Record,Book,User where  fine_state=1 and penalty_state=0 and Penalty.record_id=Record.record_id and Penalty.email=User.email and Record.book_id=Book.book_id ")
    List<Map<String,Object>> seeReturnedRecord();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "insert into library1.Penalty(penalty_id, record_id, email, penalty_bill, penalty_state) values(?1,?2,?3,?4,?5) ")
    void insertFine(int maxId, int recordId, String email, int bill, int state);

    @Query(nativeQuery = true,value = "select * from library1.Penalty order by penalty_id desc limit 1")
    List<Fine> selectMaxId();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update Penalty set penalty_state=1 where penalty_id=?1")
    void setFineFlag(int id);

    @Query(nativeQuery = true,value = "select penalty_id,Penalty.record_id,Penalty.email,Penalty.penalty_bill,Penalty.penalty_state,user_name,Record.book_id,book_name, borrow_time, return_time from Penalty,Record,Book,User where  fine_state=1 and penalty_state=0 and Penalty.record_id=Record.record_id and Penalty.email=User.email and Record.book_id=Book.book_id and Penalty.email  like concat('%',?1,'%')")
    List<Map<String,Object>> searchReturnedLikeEmail(String searchMessage);

    @Query(nativeQuery = true,value = "select penalty_id,Penalty.record_id,Penalty.email,Penalty.penalty_bill,Penalty.penalty_state,user_name,Record.book_id,book_name, borrow_time, return_time from Penalty,Record,Book,User where  fine_state=1 and penalty_state=0 and Penalty.record_id=Record.record_id and Penalty.email=User.email and Record.book_id=Book.book_id and Book.book_name  like concat('%',?1,'%')")
    List<Map<String,Object>> searchReturnedLikeBook(String searchMessage);

    @Query(nativeQuery = true,value = "select Record.record_id,Record.email,Record.book_id,Record.borrow_time,Record.record_state,user_name,book_name from Record,Book,User where  record_state=0 and Record.email=User.email and Record.book_id=Book.book_id ")
    List<Map<String,Object>> seeNotReturnedRecord();

    @Query(nativeQuery = true,value = "select Record.record_id,Record.email,Record.book_id,Record.borrow_time,Record.record_state,user_name,book_name from Record,Book,User where  record_state=0 and Record.email=User.email and Record.book_id=Book.book_id and Record.email like concat('%',?1,'%')")
    List<Map<String,Object>> searchNotReturnedLikeEmail(String msg);

    @Query(nativeQuery = true,value = "select Record.record_id,Record.email,Record.book_id,Record.borrow_time,Record.record_state,user_name,book_name from Record,Book,User where  record_state=0 and Record.email=User.email and Record.book_id=Book.book_id and Book.book_name like concat('%',?1,'%')")
    List<Map<String,Object>> searchNotReturnedLikeBook(String msg);


    @Query(nativeQuery = true,value = "select penalty_id,Penalty.record_id,Penalty.email,Penalty.penalty_bill,Penalty.penalty_state,user_name,Record.book_id,book_name, borrow_time, return_time from Penalty,Record,Book,User where  fine_state=1 and Penalty.record_id=Record.record_id and Penalty.email=User.email and Record.book_id=Book.book_id ")
    List<Map<String,Object>> getPenaltyRecord();



    @Query(nativeQuery = true,value = "select penalty_id,Penalty.record_id,Penalty.email,Penalty.penalty_bill,Penalty.penalty_state,user_name,Record.book_id,book_name, borrow_time, return_time from Penalty,Record,Book,User where penalty_state=1 and Penalty.record_id=Record.record_id and Penalty.email=User.email and Record.book_id=Book.book_id")
    List<Map<String,Object>> getAllHandlePenaltyRecorrd();

}
