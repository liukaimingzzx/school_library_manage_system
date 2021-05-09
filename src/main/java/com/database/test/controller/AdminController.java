package com.database.test.controller;


import com.database.test.entity.Admin;
import com.database.test.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    AdminRepository adminRepository;


    @RequestMapping(value = "/adminLogin",method = RequestMethod.GET)
    public String adminLogin(){
        return "admin/adminLogin.html";
    }


    @RequestMapping(value = "adminLoginSuccess",method = RequestMethod.POST)
    public String adminLoginSuccess(@RequestParam("adminEmail")String adminEmail,
                                    @RequestParam("adminPassword")String adminPassword,
                                    Map<String,String> map,
                                    HttpSession session){
        List<Admin> admins=adminRepository.selectAdminByEmail(adminEmail);
        if (admins.size()==0){
            map.put("msg","用户名或密码错误！");
        }else {
            if (admins.get(0).getAdminPassword().equals(adminPassword)){
                session.setAttribute("currentEmail",adminEmail);
                return "admin/adminMainMenu.html";
            }else {
                map.put("msg","用户名或密码错误！");
            }
        }
        return "admin/adminLogin.html";
    }


    @RequestMapping(value = "/adminMainMenu",method = RequestMethod.GET)
    public String adminMainMenu(){
        return "admin/adminMainMenu.html";
    }


}
