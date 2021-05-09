package com.database.test.controller;

import com.database.test.entity.Book;
import com.database.test.repository.BookRepository;
import com.database.test.repository.RecordRepository;
import com.database.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BookController {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    UserRepository userRepository;


    @ResponseBody
    @RequestMapping(value = "/setBookId",method = RequestMethod.POST)
    public boolean setBookId(@RequestParam("bookId") String bookId,
                             HttpSession session){
        session.setAttribute("currentBook",bookId);
        return true;
    }


    //得到书籍的详细信息
    @RequestMapping(value = "/bookDetail",method= RequestMethod.GET)
    public String seeBookInfo(HttpSession session,
                              Model model){
        String bookId= (String) session.getAttribute("currentBook");
        Book tmpBook = bookRepository.getBookInfo(bookId);
        model.addAttribute("bookId",tmpBook.getBookId());
        model.addAttribute("bookIndex",tmpBook.getBookIndex());
        model.addAttribute("bookClassify",tmpBook.getBookClassify());
        model.addAttribute("bookName",tmpBook.getBookName());
        model.addAttribute("bookAuthor",tmpBook.getBookAuthor());
        model.addAttribute("bookPress",tmpBook.getBookPress());
        model.addAttribute("bookIntroduction",tmpBook.getBookIntroduction());
        model.addAttribute("bookRestNum",tmpBook.getBookRestNum());
        model.addAttribute("bookTotalNum",tmpBook.getBookTotalNum());
        model.addAttribute("bookPrice",tmpBook.getBookPrice());
        return "user/bookDetailPage.html";
    }



    //新增书籍
    @RequestMapping(value = "/addBook",method = RequestMethod.GET)
    public String addBook(){
        return "admin/bookUploadPage.html";
    }

    @ResponseBody
    @RequestMapping(value = "/addBookSuccess",method = RequestMethod.POST)
    public Map<String,String> addBook(@RequestParam("bookId") String bookId,
                                      @RequestParam("bookIndex") String bookIndex,
                                      @RequestParam("bookClassify") String bookClassify,
                                      @RequestParam("bookName") String bookName,
                                      @RequestParam("bookAuthor") String bookAuthor,
                                      @RequestParam("bookPress") String bookPress,
                                      @RequestParam("bookIntroduction") String bookIntroduction,
                                      @RequestParam("bookTotalNum") int bookTotalNum,
                                      @RequestParam("bookPrice") int bookPrice){
        Map<String,String> modelMap = new HashMap<>();
        List<Book> tmpbook = bookRepository.selectBookById(bookId);
        if (tmpbook.size()==0){
            int flag = bookRepository.addBook(bookId,bookIndex,bookClassify, bookName, bookAuthor, bookPress, bookIntroduction, bookTotalNum, bookTotalNum, bookPrice);
            if(flag>0){
                modelMap.put("msg","新增书籍成功！");
            }else
                modelMap.put("msg","新增书籍失败！");
        }
        else{
            modelMap.put("msg","该书籍已录入，请勿重复录入书籍信息！");
        }
        return modelMap;
    }



    //书籍管理页面，包括书籍的修改和删除

    @RequestMapping(value = "/bookManage",method = RequestMethod.GET)
    public String bookManage(Model model){
        List<Book> bookList=bookRepository.selectAllBook();
        model.addAttribute("bookList",bookList);
        return "admin/bookManagePage.html";
    }

    //修改书籍信息
    @RequestMapping(value = "/modifyBook",method = RequestMethod.GET)
    public String modifyBook(HttpSession session,
                             Model model){
        String bookId= (String) session.getAttribute("currentBook");
        Book tmpBook = bookRepository.getBookInfo(bookId);
        model.addAttribute("bookId",tmpBook.getBookId());
        model.addAttribute("bookIndex",tmpBook.getBookIndex());
        model.addAttribute("bookClassify",tmpBook.getBookClassify());
        model.addAttribute("bookName",tmpBook.getBookName());
        model.addAttribute("bookAuthor",tmpBook.getBookAuthor());
        model.addAttribute("bookPress",tmpBook.getBookPress());
        model.addAttribute("bookIntroduction",tmpBook.getBookIntroduction());
        model.addAttribute("bookRestNum",tmpBook.getBookRestNum());
        model.addAttribute("bookTotalNum",tmpBook.getBookTotalNum());
        model.addAttribute("bookPrice",tmpBook.getBookPrice());
        return "admin/bookModifyPage.html";
    }

    @ResponseBody
    @RequestMapping(value = "/modifyBookSuccess",method = RequestMethod.POST)
    public Map<String,String> modifyBook(@RequestParam("bookId") String bookId,
                                         @RequestParam("bookIndex") String bookIndex,
                                         @RequestParam("bookClassify") String bookClassify,
                                         @RequestParam("bookName") String bookName,
                                         @RequestParam("bookAuthor") String bookAuthor,
                                         @RequestParam("bookPress") String bookPress,
                                         @RequestParam("bookIntroduction") String bookIntroduction,
                                         @RequestParam("bookRestNum") int bookRestNum,
                                         @RequestParam("bookTotalNum") int bookTotalNum,
                                         @RequestParam("bookPrice") int bookPrice){
        Map<String,String> modelMap = new HashMap<>();
        int flag = bookRepository.modifyBook(bookId,bookIndex,bookClassify, bookName, bookAuthor, bookPress, bookIntroduction, bookRestNum, bookTotalNum, bookPrice);
        if(flag>0){
            modelMap.put("msg","修改书籍信息成功！");
        }else
            modelMap.put("msg","修改书籍信息失败！");
        return modelMap;
    }


    //删除书籍
    @ResponseBody
    @RequestMapping(value = "/deleteBook",method = RequestMethod.POST)
    public Map<String,String> deleteBook(@RequestParam("bookId") String bookId){
        Map<String,String> modelMap = new HashMap<>();
        int flag = bookRepository.deleteBook(bookId);
        if(flag>0){
            modelMap.put("msg","书籍信息删除成功！");
        }else
            modelMap.put("msg","书籍信息删除失败！");
        return modelMap;
    }

    //书籍信息检索和用户检索的一样




}
