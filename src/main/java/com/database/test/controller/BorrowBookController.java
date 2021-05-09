package com.database.test.controller;

import com.database.test.entity.Book;
import com.database.test.entity.Record;
import com.database.test.entity.User;
import com.database.test.repository.BookRepository;
import com.database.test.repository.RecordRepository;
import com.database.test.repository.UserRepository;
import com.database.test.util.SubTextUtil;
import com.database.test.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class BorrowBookController {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/borrowBook",method = RequestMethod.GET)
    public String borrow(Model model){
        List<Book> bookList=bookRepository.selectAllBook();
        new SubTextUtil().subBookIntroduction(bookList);
        model.addAttribute("booklist",bookList);
        return "user/bookBorrowPage.html";
    }


    @ResponseBody
    @RequestMapping(value = "/borrowSuccess",method = RequestMethod.POST)
    public Map<String,String> borrowSuccess(@RequestParam("bookId")String bookId,
                                 HttpSession session){
        String email= (String) session.getAttribute("currentEmail");
        List<User> list = userRepository.selectByEmail(email);
        Map<String,String> result=new HashMap<>();

        int borrowfg = list.get(0).getMaxNum()-list.get(0).getCurrentNum();
        if(borrowfg<=0){
            result.put("result","您的已借书量已经达到允许的最大值"+list.get(0).getMaxNum()+"本!");
        }
        else{
            int restNum = bookRepository.selectRestNum(bookId);
            if(restNum==0){
                result.put("result","该书暂无库存，无法借阅!");
            }
            else{
                List<Record> records=recordRepository.selectMaxRecordId();
                //生成借阅record
                int maxId=0;
                if (records.size()==0){
                    maxId=0;
                }else {
                    maxId=records.get(0).getRecordId();
                }
                recordRepository.insertRecord(maxId+1,email,bookId,new TimeUtil().getCurrentTime(),null,0,0);
                bookRepository.updateRestNum(bookId);
                userRepository.addNum(email);
                List<User> list2 = userRepository.selectByEmail(email);
                int num1 = list2.get(0).getMaxNum()-list.get(0).getCurrentNum()-1;
                result.put("result","借阅成功,您目前还可以借"+num1+"本书!");
            }
        }
        return result;
    }

}

