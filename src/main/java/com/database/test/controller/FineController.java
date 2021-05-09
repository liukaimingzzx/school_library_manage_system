package com.database.test.controller;

import com.database.test.repository.BookRepository;
import com.database.test.repository.FineRepository;
import com.database.test.repository.RecordRepository;
import com.database.test.repository.UserRepository;
import com.database.test.util.EmailSenderUtil;
import com.database.test.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;


@Controller
public class FineController {


    @Autowired
    FineRepository fineRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    RecordRepository recordsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailSenderUtil emailSenderUtil;


    //record_state: 0是借出未还，1是借出已还，2是已挂失
    //fine_state: 0是没超时，1是超时

    //查看已经处理的罚款记录，即已缴纳罚款费用的记录
    @RequestMapping(value = "/getHandlePenaltyRecord",method = RequestMethod.GET)
    public String getHandleRecord(Model model){
        List<Map<String,Object>> tmpList=fineRepository.getAllHandlePenaltyRecorrd();
        List<Map<String,Object>> returnList=new ArrayList<>();
        for(int i=0;i<tmpList.size();i++) {

            Map<String,Object> one = tmpList.get(i);
            Date oldTime = (Date) one.get("borrow_time");
            Date newTime = (Date) one.get("return_time");
            int delayDays = new TimeUtil().calLateDays(oldTime.toString(),newTime.toString());
            Map<String,Object> newOne = new HashMap<>();
            newOne.putAll(one);
            newOne.remove("borrow_time");
            newOne.remove("return_time");
            newOne.remove("fine_state");
            newOne.remove("penalty_state");
            newOne.put("borrow_time",oldTime.toString());
            newOne.put("return_time",newTime.toString());
            newOne.put("delay_days",delayDays);
            newOne.put("penalty_state","已缴纳");
            returnList.add(newOne);
        }
        model.addAttribute("returnList",returnList);

        return "admin/penaltyHandleRecordPage.html";
    }




    //逾期已还操作
    //查看所有已还但是逾期的记录，包括已缴纳罚款和未缴纳罚款的记录
    @RequestMapping(value = "/getReturnedRecord",method = RequestMethod.GET)
    public String seeReturnedRecord(Model model){
        List<Map<String,Object>> tmpList = fineRepository.seeReturnedRecord();
        List<Map<String,Object>> returnList = new LinkedList<>();
        System.out.println(tmpList.size());
        for(int i=0;i<tmpList.size();i++) {

            Map<String,Object> one = tmpList.get(i);
            Date oldTime = (Date) one.get("borrow_time");
            Date newTime = (Date) one.get("return_time");

            int flag= (int) one.get("penalty_state");
            int delayDays = new TimeUtil().calLateDays(oldTime.toString(),newTime.toString());
            String penaltyState;

            if(flag==1){
                penaltyState="已缴纳";
            }
            else{
                penaltyState="未缴纳";
            }

            Map<String,Object> newOne = new HashMap<>();
            newOne.putAll(one);
            newOne.remove("borrow_time");
            newOne.remove("return_time");
            newOne.remove("fine_state");
            newOne.put("borrow_time",oldTime.toString());
            newOne.put("return_time",newTime.toString());
            newOne.put("delay_days",delayDays);
            newOne.put("penalty_state",penaltyState);
            returnList.add(newOne);
        }
        model.addAttribute("returnList",returnList);
        return "admin/penaltyReturnedPage.html";
    }


    //手动发送罚款邮件
    @ResponseBody
    @RequestMapping(value = "/sendFineEmail",method = RequestMethod.POST)
    public Map<String,String> sendFineEmail(@RequestParam("email") String email,
                                            @RequestParam("recordId") int recordId){
        String oldTime = recordsRepository.selectOldTime(recordId);
        String newTime = recordsRepository.selectNewTime(recordId);
        String username = userRepository.selectNameByEmail(email);
        String bookName = bookRepository.selectBookNameById(recordsRepository.selectBook(recordId));
        String bookId = recordsRepository.selectBook(recordId);
        int days = new TimeUtil().calLateDays(oldTime,newTime);
        String theme = "XX图书馆：图书逾期归还罚金缴纳提醒";
        String text = "尊敬的读者"+username+"(注册邮箱："+email+"):\n"+"您于"+newTime+
                "归还的图书，由于超出了借阅允许的最长时间30天，产生了一笔罚款待缴纳。\n"+"书名："+bookName+"\n"+"图书ISBN编号："+bookId+"\n"+
                "借书时间："+oldTime+"\n"+"还书时间："+newTime+"\n"+"罚款金额（按超出归还日期的天数，每天2元计算）："+days*2+" RMB\n"+"请您于7个工作日内到图书馆四楼缴费办公室进" +
                "行罚金缴纳，届时请带好您的借阅证以便办理手续，谢谢您的配合！\n\n\n\n\n"+"                                                                        XX图书馆图书管理办公室";

        emailSenderUtil.sendTextMail(email,theme,text);
        Map<String,String> modelMap = new HashMap<>();
        modelMap.put("msg","操作成功！");
        return  modelMap;
    }


    //更改罚款是否缴纳标记
    @ResponseBody
    @RequestMapping(value = "/setFineFlag",method = RequestMethod.POST)
    public Map<String,String> SetFineFlag(@RequestParam("penaltyId") int penaltyId){
        fineRepository.setFineFlag(penaltyId);
        Map<String,String> modelMap = new HashMap<>();
        modelMap.put("msg","操作成功！");
        return  modelMap;
    }









    //逾期未还操作

    //查看所有逾期未还的借书记录
    @RequestMapping(value = "/getNotReturnedRecord",method = RequestMethod.GET)
    public String getNotReturnedRecord(Model model){
        List<Map<String,Object>> tmpList = fineRepository.seeNotReturnedRecord();
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

    //点按钮发邮件提醒未还书的人还书
    @ResponseBody
    @RequestMapping(value = "/sendReturnEmail",method = RequestMethod.POST)
    public Map<String,String> sendReturnEmail(@RequestParam("email") String email,
                                            @RequestParam("recordId") int recordId){
        String oldTime = recordsRepository.selectOldTime(recordId);
        String username = userRepository.selectNameByEmail(email);
        String bookName = bookRepository.selectBookNameById(recordsRepository.selectBook(recordId));
        String bookId = recordsRepository.selectBook(recordId);
        int days = new TimeUtil().calLateDays(oldTime,new TimeUtil().getCurrentTime());
        String theme = "XX图书馆：图书逾期未归还提醒";
        String text = "尊敬的读者"+username+"(注册邮箱："+email+"):\n"+"您于"+oldTime+
                "借阅的图书，已经超出了借阅允许的最长时间30天，请及时到图书馆归还图书。\n"+"书名："+bookName+"\n"+"图书ISBN编号："+bookId+"\n"+
                "借书时间："+oldTime+"\n"+"已超出借阅限制最大天数："+days+" 天\n"+"请您及时携带相关证件到图书馆办理还书手续" +
                "，谢谢您的配合！\n\n\n\n\n"+"                                                                        XX图书馆图书管理办公室";
        emailSenderUtil.sendTextMail(email,theme,text);
        Map<String,String> modelMap = new HashMap<>();
        modelMap.put("msg","操作成功！");
        return  modelMap;
    }










    //读者查看自己的罚款记录
    @RequestMapping(value = "/getUserReturnedRecord",method = RequestMethod.GET)
    public String seeUserReturnedRecord(HttpSession session,
                                        Model model){
        String email= (String) session.getAttribute("currentEmail");
        List<Map<String,Object>> tmpList = fineRepository.getPenaltyRecord();
        List<Map<String,Object>> returnList = new LinkedList<>();
        for(int i=0;i<tmpList.size();i++)
        {
            Map<String,Object> one = tmpList.get(i);
            Date oldTime = (Date) one.get("borrow_time");
            Date newTime = (Date) one.get("return_time");

            int flag= (int) one.get("penalty_state");
            int delayDays = new TimeUtil().calLateDays(oldTime.toString(),newTime.toString());
            String penaltyState;
            if(flag==1)
                penaltyState="已缴纳";
            else
                penaltyState="未缴纳";
            Map<String,Object> newOne = new HashMap<>();
            newOne.putAll(one);
            newOne.remove("borrow_time");
            newOne.remove("return_time");
            newOne.remove("fine_state");
            newOne.put("borrow_time",oldTime.toString());
            newOne.put("return_time",newTime.toString());
            newOne.put("delay_days",delayDays);
            newOne.put("penalty_state",penaltyState);
            if(newOne.get("email").equals(email))
                returnList.add(newOne);
        }
        model.addAttribute("returnList",returnList);
        return "user/penaltyRecordPage.html";
    }


}
