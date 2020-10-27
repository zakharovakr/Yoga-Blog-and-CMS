package com.zakharovakr.blogCMSMastery.controllers;

import com.zakharovakr.blogCMSMastery.daos.*;
import com.zakharovakr.blogCMSMastery.dtos.Blogpost;
import com.zakharovakr.blogCMSMastery.dtos.Role;
import com.zakharovakr.blogCMSMastery.dtos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {
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

    @GetMapping("/userManager")
    public String displayUserManager(Model model) {
        //set up nav bar
        List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");
        model.addAttribute("staticList", staticList);

        //set up user list
        List<User> userList = userDao.readAllUsers();
        model.addAttribute("userList", userList);

        return "userManager";
    }

    @GetMapping("/createUser")
    public String displayCreateUser(HttpServletRequest request, Model model) {
        //set up nav bar
        List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");
        model.addAttribute("staticList", staticList);

        //set up form
        List<Role> roleList = roleDao.readAllRoles();
        model.addAttribute("roleList", roleList);

        String url = request.getHeader("referer");
        model.addAttribute("returnPage", url);

        return "createUser";
    }

    @PostMapping("/createUser")
    public String performCreateUser(HttpServletRequest request, Model model) {
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setEmail(request.getParameter("email"));

        //set password
        String rawPassword = request.getParameter("password");
        String encodedPassword = encodingPassword(rawPassword);
        if ( encodedPassword != user.getPassword()) {
            user.setPassword(encodedPassword);
        }
        //set role
        List<Role> allRole = roleDao.readAllRoles();
        List<Role> roleList = new ArrayList<>();
        for (Role role : allRole) {
            if(role.getRole().equals("ROLE_USER")) {
                roleList.add(role);
            }
        }

        user.setRoles(roleList);
        user.setEnable(true);

        //validation
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> errors = validate.validate(user);
        model.addAttribute("errors", errors);

        if (!errors.isEmpty()) {
            //set up nav bar
            List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");
            model.addAttribute("staticList", staticList);

            //set up form
            roleList = roleDao.readAllRoles();
            model.addAttribute("roleList", roleList);

            model.addAttribute("user", user);
            model.addAttribute("errors", errors);
            return "createUser";
        }

        userDao.createUser(user);

        String returnPageStr = request.getParameter("returnPage");
        String returnPage = returnPageStr.substring(21, returnPageStr.length());
        String toReturn = "redirect:" + returnPage;

        return toReturn;
    }

    @GetMapping("/editUser")
    public String editUser(HttpServletRequest request, Model model){
        //set up nav bar
        List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");
        model.addAttribute("staticList", staticList);

        //set up pre filled form
        List<Role> roleList = roleDao.readAllRoles();
        model.addAttribute("roleList", roleList);

        if(request.getParameter("id") != null) {
            //This is use when direct from userManager
            int id = Integer.parseInt(request.getParameter("id"));
            User user = userDao.readUserById(id);
            model.addAttribute("user", user);
        } else {
            //This is use when direct from editProfile
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userName = auth.getName();
            User user = userDao.getUserByUsername(userName);
            model.addAttribute("user", user);
        }
        String url = request.getHeader("referer");
        model.addAttribute("returnPage", url);

        return "editUser";
    }

    @PostMapping("/editUser")
    public String performEditUser(HttpServletRequest request, Model model) {
        int userId = Integer.parseInt(request.getParameter("id"));
        User user = userDao.readUserById(userId);
        user.setUsername(request.getParameter("username"));
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setEmail(request.getParameter("email"));

        //set password
        String rawPassword = request.getParameter("password");
        String currentPassword = user.getPassword();
        if ( !rawPassword.equals(currentPassword)) {
            String encodedPassword = encodingPassword(rawPassword);
            user.setPassword(encodedPassword);
        }
        //set role
        List<Role> roleList = new ArrayList<>();
        try{
            String[] roleIdList = request.getParameterValues("role"); // if not admin this will crash
            for (String roleId : roleIdList) {
                int id = Integer.parseInt(roleId);
                Role role = roleDao.readRoleById(id);
                roleList.add(role);
                user.setRoles(roleList);
            }
        } catch (NullPointerException ex) {

        }
        if (request.getParameter("enabled") != null) {
            boolean enable = Boolean.parseBoolean(request.getParameter("enabled"));
            user.setEnable(enable);
        }

        //validation
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> errors = validate.validate(user);
        model.addAttribute("errors", errors);

        if (!errors.isEmpty()) {
            //set up nav bar
            List<Blogpost> staticList = blogpostDao.getBlogpostByType("static");
            model.addAttribute("staticList", staticList);

            //set up form
            roleList = roleDao.readAllRoles();
            model.addAttribute("roleList", roleList);

            model.addAttribute("user", user);
            model.addAttribute("errors", errors);
            return "editUser";
        }
        userDao.updateUser(user);

        //setup where to return to
        String returnPageStr = request.getParameter("returnPage");  //page before editing
        String returnPage = returnPageStr.substring(21, returnPageStr.length());
        String toReturn = "redirect:" + returnPage;

        return toReturn;
    }

    @GetMapping("/deleteUser")
    public String deleteUser(HttpServletRequest request, Model model) {
        int userId = Integer.parseInt(request.getParameter("id"));
        userDao.deleteUser(userId);

        return "redirect:/userManager";
    }

    private String encodingPassword(String password) {
        String clearTxtPw = password;
        // BCrypt
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPw = encoder.encode(clearTxtPw);
        return hashedPw;
    }
}
