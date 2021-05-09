package com.database.test.controller;

import com.database.test.entity.Fine;
import com.database.test.entity.Record;
import com.database.test.repository.BookRepository;
import com.database.test.repository.FineRepository;
import com.database.test.repository.RecordRepository;
import com.database.test.repository.UserRepository;
import com.database.test.result.ReturnInfo;
import com.database.test.util.EmailSenderUtil;
import com.database.test.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ReturnBookController {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    RecordRepository recordsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailSenderUtil emailSenderUtil;


    @Autowired
    FineRepository fineRepository;


    //所有未还书籍
    @RequestMapping(value = "/returnBook", method = RequestMethod.GET)
    public String returnBook(Model model,
                             HttpSession session) {
        String email = (String) session.getAttribute("currentEmail");
        List<Record> recordList = recordsRepository.selectRecordByEmail(email);
        List<ReturnInfo> returnList = new LinkedList<>();
        for (int i = 0; i < recordList.size(); i++) {
            Record tmpRecord = recordList.get(i);
            String tmpBook = bookRepository.selectBookNameById(recordList.get(i).getBookId());
            String tmpBookAuthor=bookRepository.selectBookAuthorById(recordList.get(i).getBookId());
            String tmpUser = userRepository.selectNameByEmail(email);
            ReturnInfo returnInfo = new ReturnInfo();
            returnInfo.setRecordId(tmpRecord.getRecordId());
            returnInfo.setEmail(tmpRecord.getEmail());
            returnInfo.setUserName(tmpUser);
            returnInfo.setBookId(tmpRecord.getBookId());
            returnInfo.setBookName(tmpBook);
            returnInfo.setBookAuthor(tmpBookAuthor);
            returnInfo.setBorrowTime(tmpRecord.getBorrowTime());
            returnInfo.setReturnTime(tmpRecord.getReturnTime());
            returnInfo.setRecordState(tmpRecord.getRecordState());
            returnInfo.setFineState(tmpRecord.getFineState());
            returnList.add(returnInfo);
        }
        model.addAttribute("borrowedBookList", returnList);

        return "user/bookReturnPage.html";
    }



    //这个改了一下，我需要拿到的是该条借书记录的recordId,不是书本的ID
    //record_state: 0是借出未还，1是借出已还，2是已挂失
    //fine_state: 0是没超时，1是超时
    @ResponseBody
    @RequestMapping(value = "/returnBookSuccess",method = RequestMethod.POST)
    public Map<String,String> returnBookSuccess(@RequestParam("recordId") Integer recordId,
                                                HttpSession session){
        System.out.println("recordId:"+recordId);
        recordsRepository.returnBook(recordId,new TimeUtil().getCurrentTime());
        userRepository.decNum(recordsRepository.selectUser(recordId));
        bookRepository.incRestNum(recordsRepository.selectBook(recordId));
        String oldTime = recordsRepository.selectOldTime(recordId);
        String newTime = recordsRepository.selectNewTime(recordId);
        String email = recordsRepository.selectUser(recordId);
        String username = userRepository.selectNameByEmail(email);
        String bookName = bookRepository.selectBookNameById(recordsRepository.selectBook(recordId));
        String bookId = recordsRepository.selectBook(recordId);
        int days = new TimeUtil().calLateDays(oldTime,newTime);
        if(days>0){
            recordsRepository.setFine(recordId);
            List<Fine> fines=fineRepository.selectMaxId();
            //生成借阅record   ？？？？？？？不是生成罚款记录吗？
            int maxId=0;
            if (fines.size()==0){
                maxId=0;
            }else {
                maxId=fines.get(0).getPenaltyId();
            }
            fineRepository.insertFine(maxId+1,recordId,email,days*2,0);
            String theme = "XX图书馆：图书逾期归还罚金缴纳提醒";
            String text = "尊敬的读者"+username+"(注册邮箱："+email+"):\n"+"您于"+newTime+
                    "归还的图书，由于超出了借阅允许的最长时间30天，产生了一笔罚款待缴纳。\n"+"书名："+bookName+"\n"+"图书ISBN编号："+bookId+"\n"+
                    "借书时间："+oldTime+"\n"+"还书时间："+newTime+"\n"+"罚款金额（按超出归还日期的天数，每天2元计算）："+days*2+" RMB\n"+"请您于7个工作日内到图书馆四楼缴费办公室进" +
                    "行罚金缴纳，届时请带好您的借阅证以便办理手续，谢谢您的配合！\n\n\n\n\n"+"                                                                        XX图书馆图书管理办公室";

            emailSenderUtil.sendTextMail(email,theme,text);
        }

        Map<String,String> modelMap = new HashMap<>();
        modelMap.put("msg","归还书籍成功!");
        return modelMap;
    }



    @ResponseBody
    @RequestMapping(value="/lostBook",method = RequestMethod.POST)
    public Map<String,String> lostBook(@RequestParam("recordId") Integer recordId,
                                       HttpSession session){
        recordsRepository.setLost(recordId,new TimeUtil().getCurrentTime());
        bookRepository.decTotalNum(recordsRepository.selectBook(recordId));
        String email1 = recordsRepository.selectUser(recordId);
        userRepository.decNum(email1);
        String username = userRepository.selectNameByEmail(email1);
        String bookId = recordsRepository.selectBook(recordId);
        String bookName = bookRepository.selectBookNameById(bookId);
        int bookPrice = bookRepository.selectPrice(bookId);
        String theme = "XX图书馆：图书丢失罚金缴纳提醒";
        String text = "尊敬的读者"+username+"(注册邮箱："+email1+"):\n"+"您于"+new TimeUtil().getCurrentTime()+
                "提交的图书挂失，产生了一笔罚款金待缴纳。\n"+"书名："+bookName+"\n"+"图书ISBN编号："+bookId+"\n"+
                "图书定价："+bookPrice+" RMB\n"+"罚款金额："+bookPrice+" RMB\n"+"请您于7个工作日内到图书馆三楼库存管理办公室进" +
                "行罚金缴纳，届时请带好您的借阅证以便办理手续，谢谢您的配合！\n\n\n\n\n"+"                                                                        XX图书馆图书管理办公室";

        emailSenderUtil.sendTextMail(email1,theme,text);
        Map<String,String> modelMap=new HashMap<>();
        modelMap.put("msg","登记书籍丢失成功，请到邮箱查看罚金缴纳提示!");
        return modelMap;
    }

}




