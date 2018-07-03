package com.katarzynasurtel.kursownia.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import com.katarzynasurtel.kursownia.repositories.UserRepository;
import com.katarzynasurtel.kursownia.entities.Course;
import com.katarzynasurtel.kursownia.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexPage(Model m, Principal principal) {
        //m.addAttribute("user", userRepository.findByLogin(principal.getName()));
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        //zwrócenie nazwy widoku logowania, w tym przypadku login.html
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerPage(Model m) {
        m.addAttribute("user", new User());
        m.addAttribute("edit", false);
        m.addAttribute("action", "/register");
        return "register";
    }

    @RequestMapping(value = "/edit_profile", method = RequestMethod.GET)
    public String editProfile(Model m, Principal principal) {
        User u = userRepository.findByLogin(principal.getName());
        m.addAttribute("user", u);
        m.addAttribute("edit", true);
        m.addAttribute("action", "/edit_profile");
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPagePOST(@ModelAttribute(value = "user") @Valid User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "redirect:/register?error=Nie wszystkie pola zostaly wypelnione!";
        }

        if(userRepository.findByLogin(user.getLogin()) != null) return "redirect:/register?error=Uzytkownik o podanym loginie juz istnieje!";

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "redirect:/login";
    }

    @RequestMapping(value = "/edit_profile", method = RequestMethod.POST)
    public String editUserPagePOST(@ModelAttribute(value = "user") @Valid User user, BindingResult bindingResult, Principal p) {
        if(bindingResult.hasErrors()) {
            return "redirect:/edit_profile?error=Nie wszystkie pola zostaly wypelnione!";
        }


        User oldU = userRepository.findByLogin(p.getName());

        List<Course> subbed;
        List<Course> created;

        subbed = oldU.getSubbedCourses();
        created = oldU.getMyCourses();


        if(!user.getLogin().equals(oldU.getLogin()) && userRepository.findByLogin(user.getLogin()) != null)
            return "redirect:/edit_profile?error=Uzytkownik o podanym loginie juz istnieje!";


        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIdUser(oldU.getIdUser());

        user.setMyCourses(created);
        user.setSubbedCourses(subbed);

        userRepository.save(user);

        Authentication authenticaiton = new UsernamePasswordAuthenticationToken(user, user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authenticaiton);



        return "profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profilePage(Model m, Principal principal) {
        m.addAttribute("user", userRepository.findByLogin(principal.getName()));
        return "profile";
    }

    @RequestMapping(value = "/delete_user", method = RequestMethod.POST)
    public String deleteUSer(Model m, Principal principal) {
        User u;
        if(userRepository.findByLogin("uzytkownik_usuniety") != null)
            u = userRepository.findByLogin("uzytkownik_usuniety");
        else {
            u = new User("uzytkownik_usuniety", "u", "uzytkownik_usuniety", "u");
            userRepository.save(u);
        }

        User deletedUser = userRepository.findByLogin(principal.getName());

        for(Course c : deletedUser.getMyCourses())
            c.setAuthor(u);

        userRepository.delete(deletedUser);
        return "redirect:/logout";
    }

}
