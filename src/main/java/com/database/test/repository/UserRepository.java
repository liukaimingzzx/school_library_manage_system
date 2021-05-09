package com.database.test.repository;

import com.database.test.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User,String> {

    @Query(nativeQuery = true,value = "select * from User where email=?1")
    List<User> selectByEmail(String email);

    @Query(nativeQuery = true,value = "select user_name from User where email=?1")
    String selectNameByEmail(String email);

    @Query(nativeQuery = true,value = "select * from library1.User")
    List<User> selectAllUser();


    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update User set current_num=current_num+1 where email=?1")
    void addNum(String email);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update User set current_num=current_num-1 where email=?1")
    void decNum(String email);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "insert into User (email, password, current_num, max_num, user_name, gender, tel, qq, introduction) values (?1,?2,?3,?4,?5,?6,?7,?8,?9)")
    void insertRecord(String email, String password, int currentNum, int maxNum, String userName, String gender, String tel, String qq, String introduction);



    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update User set user_name=?2,gender=?3,qq=?4,tel=?5,introduction=?6 where email=?1")
    void updateUserInfo(String email, String username, String gender, String qq, String tel, String introduction);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update User set password=?2 where email=?1")
    int updatePassword(String email, String password);


    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "delete from User where email=?1")
    int deleteUser(String email);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update User set user_name=?2,gender=?3,qq=?4,tel=?5,max_num=?6,introduction=?7 where email=?1")
    int managerUserInfo(String email, String username, String gender, String qq, String tel, int maxNum, String userIntroduction);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update User set password=?2 where email=?1")
    int resetPassword(String email, String password);

    @Query(nativeQuery = true,value = "select * from User where email like concat('%',?1,'%') ")
    List<User> selectUserLikeEmail(String searchMessage);

    @Query(nativeQuery = true,value = "select * from User where user_name like concat('%',?1,'%') ")
    List<User> selectUserLikeUsername(String searchMessage);

}
