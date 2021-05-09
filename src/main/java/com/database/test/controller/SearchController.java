package com.database.test.controller;

import com.database.test.entity.Book;
import com.database.test.entity.Record;
import com.database.test.entity.User;
import com.database.test.repository.BookRepository;
import com.database.test.repository.FineRepository;
import com.database.test.repository.RecordRepository;
import com.database.test.repository.UserRepository;
import com.database.test.result.ReturnInfo;
import com.database.test.util.StateChangeToStringUtil;
import com.database.test.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class SearchController {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    UserRepository userRepository;


    @Autowired
    FineRepository fineRepository;


    //对书库的搜索（即借阅书籍界面的搜索功能）

    @ResponseBody
    @RequestMapping(value = "/searchBook",method = RequestMethod.POST)
    public boolean searchBook(@RequestParam("method") int method,
                                         @RequestParam("searchMessage")String searchMessage,
                                         HttpSession session){
        session.setAttribute("method",method);
        session.setAttribute("searchMessage",searchMessage);
        return true;
    }

    @RequestMapping(value = "/searchBookSuccess",method = RequestMethod.GET)
    public String searchBookSuccess(HttpSession session,
                                    Model model){
        int method= (int) session.getAttribute("method");
        String searchMessage= (String) session.getAttribute("searchMessage");
        List<Book> bookList = new LinkedList<>();
        if(method==0) {
            bookList=bookRepository.selectBookLikeBookId(searchMessage);
        }
        if(method==1){
            bookList=bookRepository.selectBookLikeBookIndex(searchMessage);
        }
        if(method==2){
            bookList=bookRepository.selectBookLikeBookClassify(searchMessage);
        }
        if(method==3){
            bookList=bookRepository.selectBookLikeBookName(searchMessage);
        }
        if(method==4){
            bookList=bookRepository.selectBookLikeBookAuthor(searchMessage);
        }
        if(method==5){
            bookList=bookRepository.selectBookLikeBookPress(searchMessage);
        }
        if(method==6){
            bookList=bookRepository.selectBookLikeBookIntroduction(searchMessage);
        }
        if(method==7){
            List<Book> bookList1=bookRepository.selectBookLikeBookId(searchMessage);
            if (bookList1.size()>0){
                System.out.println(bookList1.size());
                bookList.addAll(bookList1);
            }
            bookList1=bookRepository.selectBookLikeBookIndex(searchMessage);
            if (bookList1.size()>0){
                bookList.addAll(bookList1);
            }
            bookList1=bookRepository.selectBookLikeBookClassify(searchMessage);
            if (bookList1.size()>0){
                bookList.addAll(bookList1);
            }
            bookList1=bookRepository.selectBookLikeBookName(searchMessage);
            if (bookList1.size()>0){
                bookList.addAll(bookList1);
            }
            bookList1=bookRepository.selectBookLikeBookAuthor(searchMessage);
            if (bookList1.size()>0){
                bookList.addAll(bookList1);
            }
            bookList1=bookRepository.selectBookLikeBookPress(searchMessage);
            if (bookList1.size()>0){
                bookList.addAll(bookList1);
            }
            bookList1=bookRepository.selectBookLikeBookIntroduction(searchMessage);
            if (bookList1.size()>0){
                bookList.addAll(bookList1);
            }
        }
        model.addAttribute("booklist", bookList);
        return "user/bookBorrowPage.html";
    }





    //对借阅记录的搜索

    //按书名查记录
    @RequestMapping(value = "/searchBorrowRecord",method = RequestMethod.POST)
    public String searchBorrowRecord(@RequestParam("searchMessage")String searchMessage,
                                                 Model model){
        List<Map<String,Object>> tmpList = recordRepository.selectBorrowRecordByBookName(searchMessage);
        List<Map<String,Object>> returnList = new ArrayList<>();
        new StateChangeToStringUtil().changeRecordStateToString(tmpList,returnList);
        model.addAttribute("borrowedBookList", returnList);
        return "user/borrowRecordPage.html";
    }



    //查丢失书籍
    @RequestMapping(value = "/searchLostRecord",method = RequestMethod.GET)
    public String searchBorrowRecord(HttpSession session,
                                     Model model){
        String email = (String) session.getAttribute("currentEmail");
        List<Map<String,Object>> returnInfos = recordRepository.selectLostRecord(email);
        List<Map<String,Object>> returnList=new LinkedList<>();
        new StateChangeToStringUtil().changeRecordStateToString(returnInfos,returnList);
        model.addAttribute("recordList",returnList);
        return "user/listRecordPage.html";
    }

    //查已还书籍
    @RequestMapping(value = "/searchReturnRecord",method = RequestMethod.GET)
    public String searchReturnedRecord(HttpSession session,
                                       Model model){
        String email = (String) session.getAttribute("currentEmail");
        List<Map<String,Object>> returnInfos = recordRepository.selectOverRecord(email);
        List<Map<String,Object>> returnList=new LinkedList<>();
        new StateChangeToStringUtil().changeRecordStateToString(returnInfos,returnList);
        model.addAttribute("borrowedBookList",returnList);
        return "user/borrowRecordPage.html";
    }




    //管理员部分


    //书籍管理部分的查找
    @RequestMapping(value = "/searchAdminBook",method = RequestMethod.POST)
    public String getAdminBook(@RequestParam("searchMessage")String searchMessage,
                               Model model){

        List<Book> bookList=new ArrayList<>();
        List<Book> bookList1=bookRepository.selectBookLikeBookId(searchMessage);
        if (bookList1.size()>0){
            bookList.addAll(bookList1);
        }
        bookList1=bookRepository.selectBookLikeBookIndex(searchMessage);
        if (bookList1.size()>0){
            bookList.addAll(bookList1);
        }
        bookList1=bookRepository.selectBookLikeBookClassify(searchMessage);
        if (bookList1.size()>0){
            bookList.addAll(bookList1);
        }
        bookList1=bookRepository.selectBookLikeBookName(searchMessage);
        if (bookList1.size()>0){
            bookList.addAll(bookList1);
        }
        bookList1=bookRepository.selectBookLikeBookAuthor(searchMessage);
        if (bookList1.size()>0){
            bookList.addAll(bookList1);
        }
        bookList1=bookRepository.selectBookLikeBookPress(searchMessage);
        if (bookList1.size()>0){
            bookList.addAll(bookList1);
        }
        bookList1=bookRepository.selectBookLikeBookIntroduction(searchMessage);
        if (bookList1.size()>0){
            bookList.addAll(bookList1);
        }
        model.addAttribute("bookList",bookList);
        return "admin/bookManagePage.html";
    }



    //用户管理部分的查找
    //按email和username模糊查找用户
    @RequestMapping(value = "/searchUsers",method = RequestMethod.POST)
    public String searchUsers(@RequestParam("searchMessage") String searchMessage,
                                           Model model){
        List<User> users = userRepository.selectUserLikeEmail(searchMessage);
        if(users.size()==0)
        {
            users = userRepository.selectUserLikeUsername(searchMessage);
        }
        model.addAttribute("userList",users);
        return "admin/userManagePage.html";
    }








    //罚款部分的查找


    //按email和bookname模糊查找逾期但是已还的罚款记录
    @RequestMapping(value = "/searchReturned",method = RequestMethod.POST)
    public String searchReturned(@RequestParam("searchMessage") String searchMessage,
                                              Model model){
        List<Map<String,Object>> tmpList = fineRepository.searchReturnedLikeEmail(searchMessage);
        List<Map<String,Object>> returnList = new LinkedList<>();
        if(tmpList.size()==0)
        {
            tmpList = fineRepository.searchReturnedLikeBook(searchMessage);
        }
        //penalty_state的切换，从0、1变为未缴纳和已缴纳
        new StateChangeToStringUtil().changePenaltyStateAndCalDays(tmpList,returnList);
        model.addAttribute("returnList",returnList);
        return "admin/penaltyReturnedPage.html";
    }


    //按email和bookname模糊查找逾期但是未还的记录
    @RequestMapping(value = "/searchNotReturn",method = RequestMethod.POST)
    public String searchNotReturn(@RequestParam("searchMessage") String searchMessage,
                                               Model model){
        List<Map<String,Object>> tmpList = fineRepository.searchNotReturnedLikeEmail(searchMessage);
        if(tmpList.size()==0)
        {
            tmpList = fineRepository.searchNotReturnedLikeBook(searchMessage);
        }
        List<Map<String,Object>> returnList = new LinkedList<>();
        for(int i=0;i<tmpList.size();i++)
        {
            Map<String,Object> one  = tmpList.get(i);
            Date oldTime = (Date) one.get("borrow_time");
            String newTime = new TimeUtil().getCurrentTime();
            int flag = (int) one.get("record_state");
            int delayDays = new TimeUtil().calLateDays(oldTime.toString(),newTime);
            String recordState="未归还";
            Map<String,Object> newOne = new HashMap<>();
            newOne.putAll(one);
            newOne.remove("record_state");
            newOne.remove("borrow_time");
            newOne.put("delay_days",delayDays);
            newOne.put("borrow_time",oldTime.toString());
            newOne.put("record_state",recordState);
            if(delayDays>0)
                returnList.add(newOne);
        }
        model.addAttribute("returnList",returnList);
        return "admin/penaltyNotReturnedPage.html";
    }

}
