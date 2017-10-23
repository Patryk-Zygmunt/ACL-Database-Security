package com.designPatterns.ACLDatabaseSecurity.controllers;

import com.designPatterns.ACLDatabaseSecurity.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Linus on 21.10.2017.
 */
@Controller
public class UserController {

    @Autowired
    SalaryService salaryService;


    @RequestMapping(value = "/salary", method = RequestMethod.GET)
    public String showSalaries(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("salary", salaryService.getSalaries());
        model.addAttribute("logged_user", authentication.getName());
        return "salary";
    }


}
