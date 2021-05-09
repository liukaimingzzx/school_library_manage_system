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
public class UserManageController {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    RecordRepository recordsRepository;

    @Autowired
    UserRepository userRepository;

    //设置当前进行修改的用户email
    @ResponseBody
    @RequestMapping(value = "/setUserEmail",method = RequestMethod.POST)
    public boolean setUserEmail(@RequestParam("email")String userEmail,
                                HttpSession session){
        session.setAttribute("modifyUserEmail",userEmail);
        return true;
    }

    //显示所有已注册的用户
    @RequestMapping(value = "/userManage", method = RequestMethod.GET)
    public String getAllUsers(Model model) {
        List<User> users = userRepository.selectAllUser();
        model.addAttribute("userList", users);
        return "admin/userManagePage.html";
    }



    @RequestMapping(value = "/modifyUser",method = RequestMethod.GET)
    public String modifyUser(HttpSession session,
                             Model model){
        String email= (String) session.getAttribute("modifyUserEmail");
        List<User> users=userRepository.selectByEmail(email);
        if (users.size()>0){
            User user=userRepository.selectByEmail(email).get(0);
            System.out.println(user.getEmail());
            model.addAttribute("email",user.getEmail());
            model.addAttribute("userName",user.getUserName());
            model.addAttribute("gender",user.getGender());
            model.addAttribute("tel",user.getTel());
            model.addAttribute("QQ",user.getQq());
            model.addAttribute("maxNum",user.getMaxNum());
            model.addAttribute("introduction",user.getIntroduction());
        }
        return "admin/userModifyPage.html";
    }



    //修改用户信息
    @ResponseBody
    @RequestMapping(value = "/modifyUserSuccess", method = RequestMethod.POST)
    public Map<String, Object> modifyUserSuccess(@RequestParam("userEmail") String userEmail,
                                                 @RequestParam("userName") String userName,
                                                 @RequestParam("userGender") String userGender,
                                                 @RequestParam("userQQ") String userQQ,
                                                 @RequestParam("userTel") String userTel,
                                                 @RequestParam("userMaxNum") int maxNum,
                                                 @RequestParam("userIntroduction")String userIntroduction) {
        System.out.println(userEmail);
        Map<String, Object> modelMap = new HashMap<>();
        int flag = userRepository.managerUserInfo(userEmail, userName, userGender, userQQ, userTel, maxNum, userIntroduction);
        if (flag > 0) {
            modelMap.put("msg", "修改读者信息成功！");
        } else
            modelMap.put("msg", "修改读者信息失败！");
        return modelMap;
    }


    //为用户重置密码
    @ResponseBody
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public Map<String, Object> managerUserInfo(@RequestParam("email") String userEmail) {
        Map<String, Object> modelMap = new HashMap<>();
        int flag = userRepository.resetPassword(userEmail, "111111");
        if (flag > 0) {
            modelMap.put("msg", "重置密码成功！");
        } else
            modelMap.put("msg", "重置密码失败！");
        return modelMap;
    }


    //删除用户
    @ResponseBody
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public Map<String, Object> DeleteUser(@RequestParam("email") String email) {
        Map<String, Object> modelMap = new HashMap<>();
        int flag = userRepository.deleteUser(email);
        if (flag > 0) {
            modelMap.put("msg", "注销读者信息成功！");
        } else
            modelMap.put("msg", "注销读者信息失败！");
        return modelMap;
    }
    
}
