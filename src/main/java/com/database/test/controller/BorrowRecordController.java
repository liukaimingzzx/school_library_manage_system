package com.database.test.controller;

import com.database.test.entity.Record;
import com.database.test.result.ReturnInfo;
import com.database.test.repository.BookRepository;
import com.database.test.repository.RecordRepository;
import com.database.test.repository.UserRepository;
import com.database.test.util.StateChangeToStringUtil;
import com.database.test.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.*;


@Controller
public class BorrowRecordController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    RecordRepository recordRepository;


    //查看还书记录，显示所有已还和已挂失的书籍
    @RequestMapping(value = "/borrowRecord", method = RequestMethod.GET)
    public String seeRecord(Model model,
                            HttpSession session) {
        String email = (String) session.getAttribute("currentEmail");
        List<Map<String,Object>> tmpList=recordRepository.selectAllRecord(email);
        List<Map<String,Object>> returnList = new LinkedList<>();
        new StateChangeToStringUtil().changeRecordStateToString(tmpList,returnList);
        model.addAttribute("borrowedBookList", returnList);
        return "user/borrowRecordPage.html";
    }

}
