package com.zakharovakr.blogCMSMastery.controllers;

import com.zakharovakr.blogCMSMastery.daos.*;
import com.zakharovakr.blogCMSMastery.dtos.Blogpost;
import com.zakharovakr.blogCMSMastery.dtos.Comment;
import com.zakharovakr.blogCMSMastery.dtos.Hashtag;
import com.zakharovakr.blogCMSMastery.dtos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class BlogpostController {
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

    @Autowired
    ImageDao imageDao;

    private final String BLOGPOST_UPLOAD_DIRECTORY = "Blogposts";

    @GetMapping({"/post"})
    public String displayPostPage(HttpServletRequest request, Model model ) {
        int postId = Integer.parseInt(request.getParameter("id"));
        Blogpost blogpost = blogpostDao.readBlogpostById(postId);
        List<Comment> commentList = commentDao.getCommentByBlogpostId(postId);
        List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");

        List<Hashtag> hashtagsForPostList = blogpostDao.getTagsForBlogpost(postId);

        //set categories side menu
        List<Hashtag> tagList = hashtagDao.readAllHashtags();
        model.addAttribute("tagList", tagList);

        model.addAttribute("blogpost", blogpost);
        model.addAttribute("commentList", commentList);
        model.addAttribute("staticList", staticList);
        model.addAttribute("hashtagsForPostList", hashtagsForPostList);
        return "post";
    }

    @GetMapping({"/static"})
    public String displayStaticPage(HttpServletRequest request, Model model) {
        int postId = Integer.parseInt(request.getParameter("id"));
        Blogpost blogpost = blogpostDao.readBlogpostById(postId);
        List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");
        //set categories side menu
        List<Hashtag> tagList = hashtagDao.readAllHashtags();

        model.addAttribute("tagList", tagList);

        model.addAttribute("blogpost", blogpost);
        model.addAttribute("staticList", staticList);
        return "static";
    }

    @GetMapping("/contentManager")
    public String displayContentManager(Model model) {
        //set up nav bar (with static pages)
        List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");
        model.addAttribute("staticList", staticList);

        //set up content manager page
        List<Blogpost> blogpostList = blogpostDao.readAllBlogposts();
        model.addAttribute("blogpostList", blogpostList);

        return "contentManager";
    }

    @GetMapping("/editBlogpost")
    public String displayEditBlogpost(HttpServletRequest request, Model model) {
        //set up nav bar (with static pages)
        List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");
        model.addAttribute("staticList", staticList);

        //set pre filled form
        int blogpostId = Integer.parseInt(request.getParameter("id"));
        Blogpost blogpost = blogpostDao.readBlogpostById(blogpostId);
        model.addAttribute("blogpost", blogpost);

        //set drop down list in edit's form
        List<User> userList = userDao.readAllUsers();
        model.addAttribute("userList", userList);

        List<String> typeList = new ArrayList<>();
        typeList.add("blog");
        typeList.add("static");
        model.addAttribute("typeList", typeList);

        List<String> statusList = new ArrayList<>();
        statusList.add("private");
        statusList.add("public");
        model.addAttribute("statusList", statusList);

        List<Hashtag> tagList = hashtagDao.readAllHashtags();
        model.addAttribute("tagList", tagList);

        return "editBlogpost";
    }

    @PostMapping("/editBlogpost")
    public String performEditBlogpost(HttpServletRequest request, Model model, Authentication authResult) {

        //save edited content
        int blogpostId = Integer.parseInt(request.getParameter("id"));
        Blogpost blogpost = blogpostDao.readBlogpostById(blogpostId);

        blogpost.setTimePosted(LocalDateTime.now());
        blogpost.setTitle(request.getParameter("title"));
        String statusStr = request.getParameter("status");
        if (statusStr != null) {
            blogpost.setStatus(request.getParameter("status"));
        }

        //if a content manager is editing the post - it becomes private
        String role =  authResult.getAuthorities().toString();

        if (!role.contains("ROLE_ADMIN")){
            blogpost.setStatus("private");
        }

        String typeStr = request.getParameter("type");
        if (typeStr != null) {
            blogpost.setType(request.getParameter("type"));
        }
        String userIdStr = request.getParameter("userId");
        if (userIdStr != null) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            blogpost.setUser(userDao.readUserById(userId));
        }
        blogpost.setContent(request.getParameter("content"));

        List<Hashtag> tagList = new ArrayList<>();
        try {
            String[] tagIdList = request.getParameterValues("hashtag");
            for (String tagId : tagIdList) {
                int id = Integer.parseInt(tagId);
                tagList.add(hashtagDao.readHashtagById(id));
            }
            blogpost.setHashtags(tagList);
        } catch (NullPointerException ex) {

        }

        //validation
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Blogpost>> errors = validate.validate(blogpost);
        model.addAttribute("errors", errors);
        if (!errors.isEmpty()) {

            //set drop down list in edit's form
            List<User> userList = userDao.readAllUsers();
            model.addAttribute("userList", userList);

            List<String> typeList = new ArrayList<>();
            typeList.add("blog");
            typeList.add("static");
            model.addAttribute("typeList", typeList);

            List<String> statusList = new ArrayList<>();
            statusList.add("private");
            statusList.add("public");
            model.addAttribute("statusList", statusList);

            List<Hashtag> tagListAll = hashtagDao.readAllHashtags();
            model.addAttribute("tagList", tagListAll);

            model.addAttribute("errors", errors);
            model.addAttribute("blogpost", blogpost);

            return "editBlogpost";
        }

        blogpostDao.updateBlogpost(blogpost);

        //set up nav bar
        List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");
        model.addAttribute("staticList", staticList);

        //set up content manager page
        List<Blogpost> blogpostList = blogpostDao.readAllBlogposts();
        model.addAttribute("blogpostList", blogpostList);

        return "redirect:/contentManager";
    }

    @GetMapping("/createBlogpost")
    public String displayCreateBlogpost(Model model) {
        //set up nav bar
        List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");
        model.addAttribute("staticList", staticList);

        //set drop down list in form
        List<User> userList = userDao.readAllUsers();
        model.addAttribute("userList", userList);

        List<String> typeList = new ArrayList<>();
        typeList.add("blog");
        typeList.add("static");
        model.addAttribute("typeList", typeList);

        List<String> statusList = new ArrayList<>();
        statusList.add("private");
        statusList.add("public");
        model.addAttribute("statusList", statusList);

        List<Hashtag> tagList = hashtagDao.readAllHashtags();
        model.addAttribute("tagList", tagList);

        return "createBlogpost";
    }

    @PostMapping("/createBlogpost")
    public String performCreateBlogpost(HttpServletRequest request, Model model) {
        //perform create content
//        String fileLocation = imageDao.saveImage(file, Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)), BLOGPOST_UPLOAD_DIRECTORY);
        Blogpost blogpost = new Blogpost();
        blogpost.setTimePosted(LocalDateTime.now());
        blogpost.setTitle(request.getParameter("title"));
        blogpost.setStatus(request.getParameter("status"));
        blogpost.setContent(request.getParameter("content"));
//        blogpost.setPhotoFileName(fileLocation);

        //only admin
        if (request.getParameter("type") == null) {
            blogpost.setType("blog"); //default
        } else {
            blogpost.setType(request.getParameter("type"));
        }
        if (request.getParameter("status") == null) {
            blogpost.setStatus("private"); //default
        } else {
            blogpost.setStatus(request.getParameter("status"));
        }

        if (request.getParameter("userId") == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();   //default
            String username = auth.getName();
            blogpost.setUser(userDao.getUserByUsername(username));
        } else {
            int userId = Integer.parseInt(request.getParameter("userId"));
            blogpost.setUser(userDao.readUserById(userId));
        }


        List<Hashtag> tagList = new ArrayList<>();
        try {
            String[] tagIdList = request.getParameterValues("hashtag");
            for (String tagId : tagIdList) {
                int id = Integer.parseInt(tagId);
                tagList.add(hashtagDao.readHashtagById(id));
            }
            blogpost.setHashtags(tagList);
        } catch (NullPointerException ex) {

        }

        //validation
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Blogpost>> errors = validate.validate(blogpost);
        model.addAttribute("errors", errors);
        if (!errors.isEmpty()) {
            //set drop down list in edit's form
            List<User> userList = userDao.readAllUsers();
            model.addAttribute("userList", userList);

            List<String> typeList = new ArrayList<>();
            typeList.add("blog");
            typeList.add("static");
            model.addAttribute("typeList", typeList);

            List<String> statusList = new ArrayList<>();
            statusList.add("private");
            statusList.add("public");
            model.addAttribute("statusList", statusList);

            List<Hashtag> tagListAll = hashtagDao.readAllHashtags();
            model.addAttribute("tagList", tagListAll);

            model.addAttribute("errors", errors);
            model.addAttribute("blogpost", blogpost);

            return "createBlogpost";
        }

        blogpostDao.createBlogpost(blogpost);

        //set up nav bar
        List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");
        model.addAttribute("staticList", staticList);

        //set up content manager page
        List<Blogpost> blogpostList = blogpostDao.readAllBlogposts();
        model.addAttribute("blogpostList", blogpostList);


        return "redirect:/contentManager";
    }

    @GetMapping("/deleteBlogpost")
    public String deleteBlogpost (HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        blogpostDao.deleteBlogpost(id);

        //set up content manager page
        List<Blogpost> blogpostList = blogpostDao.readAllBlogposts();
        model.addAttribute("blogpostList", blogpostList);

        return "redirect:/contentManager";
    }

    @GetMapping("/searchPost")
    public String displaySearchResult (HttpServletRequest request, Model model) {
        String idString = request.getParameter("id");

        if (idString != null) {
            int id = Integer.parseInt(request.getParameter("id"));
            List<Blogpost> blogList = blogpostDao.getBlogpostByType("blog");
            model.addAttribute("blogList", blogList);
            //set title
            Hashtag tag = hashtagDao.readHashtagById(id);
            model.addAttribute("tag", tag);
        }
        //search using the search box
        String search = request.getParameter("search");
        if (search != null) {
            String searchText = request.getParameter("search");
            List<Blogpost> blogList = blogpostDao.getBlogpostBySearchTitle(searchText);
            model.addAttribute("blogList", blogList);
        }

        //set up nav bar
        List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");
        model.addAttribute("staticList", staticList);

        //set categories side menu
        List<Hashtag> tagList = hashtagDao.readAllHashtags();

        model.addAttribute("tagList", tagList);

        return "searchPost";
    }

    @GetMapping("/searchPostByCategory")
    public String displaySearchResultByCategory (HttpServletRequest request, Model model) {

        List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");

        String tagIdString = request.getParameter("tagId");
        List<Blogpost> blogpostsForTagList = blogpostDao.getBlogpostByTag(Integer.parseInt(tagIdString));

        //set categories side menu
        List<Hashtag> tagList = hashtagDao.readAllHashtags();

        model.addAttribute("tagList", tagList);

        model.addAttribute("staticList", staticList);

        model.addAttribute("blogpostsForTagList", blogpostsForTagList);

        return "searchPostByCategory";
    }
}

