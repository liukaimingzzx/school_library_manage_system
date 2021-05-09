package com.database.test.controller;

import com.database.test.entity.User;
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
public class UserController {

    @Autowired
    UserRepository userRepository;


    @Autowired
    BookRepository bookRepository;


    @Autowired
    RecordRepository recordRepository;


    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(){
        return "user/login.html";
    }


    @RequestMapping(value = "/loginSuccess",method = RequestMethod.POST)
    public String loginSuccess(@RequestParam("email")String email,
                               @RequestParam("password")String password,
                               HttpSession session,
                               Map<String,Object> map){

        List<User> users=userRepository.selectByEmail(email);
        if (users.size()==0) {
        }else {
            if (users.get(0).getPassword().equals(password)){
                session.setAttribute("currentEmail",email);
                return "user/userMainMenu.html";
            }else {
                map.put("msg","用户名或密码错误！");
            }
        }
        return "user/login.html";
    }




    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String register(){
        return "user/register.html";
    }


    @RequestMapping(value = "/registerSuccess",method = RequestMethod.POST)
    public String registerSuccess(@RequestParam("email")String email,
                                  @RequestParam("password")String passwrod,
                                  @RequestParam("userName")String userName,
                                  @RequestParam("gender")String gender,
                                  @RequestParam("tel")String tel,
                                  @RequestParam("qq")String qq,
                                  @RequestParam("introduction")String introduction,
                                  Map<String,String> map){

        List<User> users=userRepository.selectByEmail(email);
        if (users.size()==0){
            map.put("msg","注册成功！");
            userRepository.insertRecord(email,passwrod,0,5,userName,gender,tel,qq,introduction);
            return "user/login.html";
        }else {
            map.put("msg","注册失败！邮箱已注册！");
            return "user/register.html";
        }
    }



    @RequestMapping(value = "/userInfo",method = RequestMethod.GET)
    public String getUserInfoManage(HttpSession session,
                                    Model model){
        String email= (String) session.getAttribute("currentEmail");
        List<User> users=userRepository.selectByEmail(email);
        if (users.size()>0){
            User user=users.get(0);
            model.addAttribute("email",email);
            model.addAttribute("userName",user.getUserName());
            model.addAttribute("gender",user.getGender());
            model.addAttribute("tel",user.getTel());
            model.addAttribute("QQ",user.getQq());
            model.addAttribute("maxNum",user.getMaxNum());
            model.addAttribute("currentNum",user.getCurrentNum());
            model.addAttribute("introduction",user.getIntroduction());
            model.addAttribute("password",user.getPassword());
        }
        return "user/userInfoManagePage.html";
    }

    @ResponseBody
    @RequestMapping(value = "/userInfoChange",method = RequestMethod.POST)
    public String userInfoChange(@RequestParam("userName")String userName,
                                 @RequestParam("userGender")String userGender,
                                 @RequestParam("userQQ")String userQQ,
                                 @RequestParam("userTel")String userTel,
                                 @RequestParam("userIntroduction")String userIntroduction,
                                 HttpSession session){
        String userEmail= (String) session.getAttribute("currentEmail");
        userRepository.updateUserInfo(userEmail,userName,userGender,userQQ,userTel,userIntroduction);
        return "个人信息修改成功!";
    }



    @RequestMapping(value = "/mainMenu",method = RequestMethod.GET)
    public String mainMenu(){
        return "user/userMainMenu.html";
    }


    @RequestMapping(value = "/changePassword",method = RequestMethod.GET)
    public String changePassword(){ return "user/userPasswordChangePage.html"; }


    @ResponseBody
    @RequestMapping(value ="/changePasswordSuccess", method = RequestMethod.POST)
    public Map<String,String> changePassword(@RequestParam("oldPassword") String password1,
                                             @RequestParam("newPassword") String password2,
                                             HttpSession session){
        Map<String,String> modelMap = new HashMap<>();
        String email= (String) session.getAttribute("currentEmail");
        List<User> list = userRepository.selectByEmail(email);
        if(!password1.equals(list.get(0).getPassword()))
        {
            modelMap.put("msg","输入的原密码有误！");
        }
        else if(password2.equals(list.get(0).getPassword())){
            modelMap.put("msg","新旧密码不能相同！");
        }
        else {
            int flag = userRepository.updatePassword(email, password2);
            if (flag > 0) {
                modelMap.put("msg", "修改密码成功！");
            } else
                modelMap.put("msg", "修改密码失败！");
        }
        return modelMap;
    }
}
