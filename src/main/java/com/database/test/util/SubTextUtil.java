package com.database.test.util;

import com.database.test.entity.Book;

import java.util.List;

public class SubTextUtil {

    public void subBookIntroduction(List<Book> bookList){
        for (int i=0;i<bookList.size();i++){
            Book book=bookList.get(i);
            if (book.getBookIntroduction().length()>20){
                book.setBookIntroduction(book.getBookIntroduction().substring(0,20));
            }
        }
    }

}
