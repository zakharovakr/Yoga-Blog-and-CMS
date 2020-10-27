package com.zakharovakr.blogCMSMastery.controllers;

import com.zakharovakr.blogCMSMastery.daos.*;
import com.zakharovakr.blogCMSMastery.dtos.Blogpost;
import com.zakharovakr.blogCMSMastery.dtos.Hashtag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@Controller
public class HashtagController {
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

    @GetMapping("/categoryManager")
    public String displayCategoryManager (Model model) {
        //set up nav bar
        List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");
        model.addAttribute("staticList", staticList);

        List<Hashtag> tagList = hashtagDao.readAllHashtags();
        model.addAttribute("tagList", tagList);

        return "categoryManager";
    }


    @PostMapping("/createCategory")
    public String createTag (HttpServletRequest request, Model model) {
        //set up tag table
        List<Hashtag> tagList = hashtagDao.readAllHashtags();
        model.addAttribute("tagList", tagList);

        Hashtag tag = new Hashtag();
        tag.setName(request.getParameter("tag"));

        //validation
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Hashtag>> errors = validate.validate(tag);
        model.addAttribute("errors", errors);

        if (!errors.isEmpty()) {
            //set up nav bar
            List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");
            model.addAttribute("staticList", staticList);

            return "categoryManager";
        }

        hashtagDao.createHashtag(tag);

        return "redirect:/categoryManager";
    }

    @GetMapping("/editCategory")
    public String editCategory(HttpServletRequest request, Model model) {
        //set up nav bar
        List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");
        model.addAttribute("staticList", staticList);

        int id = Integer.parseInt(request.getParameter("id"));
        Hashtag hashtag = hashtagDao.readHashtagById(id);

        model.addAttribute("hashtag", hashtag);

        return "editCategory";
    }

    @PostMapping("/editCategory")
    public String performEditUser(HttpServletRequest request, Model model) {

        int tagId = Integer.parseInt(request.getParameter("id"));
        Hashtag hashtag = hashtagDao.readHashtagById(tagId);
        hashtag.setName(request.getParameter("name"));

        //validation
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Hashtag>> errors = validate.validate(hashtag);
        model.addAttribute("errors", errors);

        if (!errors.isEmpty()) {

            //set up nav bar
            List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");
            model.addAttribute("staticList", staticList);

            hashtag.setName(request.getParameter("name"));

            model.addAttribute("hashtag", hashtag);

            return "editCategory";
        }

        hashtagDao.updateHashtag(hashtag);

        return "redirect:/categoryManager";
    }

    @GetMapping("deleteCategory")
    public String deleteTag (HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        hashtagDao.deleteHashtag(id);

        //set up tag table
        List<Hashtag> tagList = hashtagDao.readAllHashtags();
        model.addAttribute("tagList", tagList);

        return "redirect:/categoryManager";
    }


}

