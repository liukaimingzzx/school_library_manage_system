
package com.database.test.repository;

import com.database.test.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface BookRepository extends JpaRepository<Book,String> {


    @Query(nativeQuery = true,value = "select * from Book ")
    List<Book> selectAllBook();

    @Query(nativeQuery = true,value = "select book_restnum from Book where book_id=?1")
    int selectRestNum(String bookId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update Book set book_restnum=book_restnum-1 where book_id=?1")
    void updateRestNum(String bookId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update Book set book_restnum=book_restnum+1 where book_id=?1")
    void incRestNum(String bookId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update Book set book_totalnum=book_totalnum-1 where book_id=?1")
    void decTotalNum(String bookId);

    @Query(nativeQuery = true,value = "select book_name from Book where book_id=?1")
    String selectBookNameById(String bookId);

    @Query(nativeQuery = true,value = "select book_author from Book where book_id=?1")
    String selectBookAuthorById(String bookId);


    @Query(nativeQuery = true,value = "select book_price from Book where book_id=?1")
    int selectPrice(String bookId);


    @Query(nativeQuery = true,value = "select * from Book where book_id like concat('%',?1,'%') ")
    List<Book> selectBookLikeBookId(String serachMessage);

    @Query(nativeQuery = true,value = "select * from Book where book_index like concat('%',?1,'%') ")
    List<Book> selectBookLikeBookIndex(String serachMessage);

    @Query(nativeQuery = true,value = "select * from Book where book_classify like concat('%',?1,'%') ")
    List<Book> selectBookLikeBookClassify(String serachMessage);

    @Query(nativeQuery = true,value = "select * from Book where book_name like concat('%',?1,'%') ")
    List<Book> selectBookLikeBookName(String serachMessage);

    @Query(nativeQuery = true,value = "select * from Book where book_author like concat('%',?1,'%') ")
    List<Book> selectBookLikeBookAuthor(String serachMessage);

    @Query(nativeQuery = true,value = "select * from Book where book_press like concat('%',?1,'%') ")
    List<Book> selectBookLikeBookPress(String serachMessage);

    @Query(nativeQuery = true,value = "select * from Book where book_introduction like concat('%',?1,'%') ")
    List<Book> selectBookLikeBookIntroduction(String serachMessage);

    @Query(nativeQuery = true,value = "select * from Book where book_id=?1")
    Book getBookInfo(String bookId);


    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into Book (book_id, book_index, book_classify, book_name, book_author, book_press, book_introduction, book_restnum, book_totalnum, book_price) values (?1,?2,?3,?4,?5,?6,?7,?8,?9,?10)")
    int addBook(String bookId,String bookIndex,String bookClassify,String bookName,String bookAuthor,String bookPress,String bookIntroduction,int bookRestNum,int bookTotalNum,int bookPrice);



    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update Book set book_index=?2, book_classify=?3, book_name=?4, book_author=?5, book_press=?6, book_introduction=?7, book_restnum=?8, book_totalnum=?9, book_price=?10 where book_id=?1")
    int modifyBook(String bookId,String bookIndex,String bookClassify,String bookName,String bookAuthor,String bookPress,String bookIntroduction,int bookRestNum,int bookTotalNum,int bookPrice);


    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "delete from Book where book_id=?1 ")
    int deleteBook(String bookID);

    @Query(nativeQuery = true,value = "select * from Book where book_id =?1")
    List<Book> selectBookById(String serachMessage);


}
