package com.zakharovakr.blogCMSMastery.controllers;

import com.zakharovakr.blogCMSMastery.daos.*;
import com.zakharovakr.blogCMSMastery.dtos.Blogpost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LoginController {
    @Autowired
    RoleDao roleDao;

    @Autowired
    UserDao userDao;

    @Autowired
    BlogpostDao blogpostDao;

    @Autowired
    HashtagDao hashtagDao;

    @Autowired
    CommentDao commentDao;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        //set up nav bar
        List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");
        model.addAttribute("staticList", staticList);
        return "login";
    }
}
