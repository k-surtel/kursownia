package com.katarzynasurtel.kursownia.controllers;

import com.katarzynasurtel.kursownia.repositories.CourseRepository;
import com.katarzynasurtel.kursownia.repositories.UserRepository;
import com.katarzynasurtel.kursownia.entities.Course;
import com.katarzynasurtel.kursownia.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@SessionAttributes({"course"})
@Controller
public class CourseController {

    @Autowired
    CourseRepository courseRepository;
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/courses", method = RequestMethod.GET)
    public String loginPage(Model m) {
        m.addAttribute("courseslist", courseRepository.findAll());
        return "courses";
    }

    @RequestMapping(value = "/new_course", method = RequestMethod.GET)
    public String newCoursePage(Model m) {
        m.addAttribute("course", new Course());
        m.addAttribute("edit", false);
        m.addAttribute("action", "/new_course");
        return "new_course";
    }

    @RequestMapping(value = "/courses/{idCourse}", method = RequestMethod.GET)
    public String coursePage(@PathVariable Integer idCourse, Model m) {
        Course c = courseRepository.findById(idCourse).get();
        m.addAttribute("course", c);
        m.addAttribute("author", c.getAuthor());
        List<String> subbedUsersNames = new ArrayList<>();
        for(User u : c.getSubbedUsers()) subbedUsersNames.add(u.getName());
        m.addAttribute("subbednames", subbedUsersNames);
        return "course_page";
    }

    @RequestMapping(value = "/new_course", method = RequestMethod.POST)
    public String newCoursePagePOST(@ModelAttribute(value = "course") @Valid Course course, BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) {
            return "redirect:/new_course?error=Podaj nazwe kursu!";
        }
        course.setAuthor(userRepository.findByLogin(principal.getName()));
        courseRepository.save(course);
        int id = course.getIdCourse();
        return "redirect:/courses/"+String.valueOf(id);
    }

    @RequestMapping(value = "/courses/{idCourse}/apply", method = RequestMethod.POST)
    public String applyToACourse(@PathVariable Integer idCourse, Principal principal) {
        User u = userRepository.findByLogin(principal.getName());
        Course c = courseRepository.findById(idCourse).get();

        if(!c.getSubbedUsers().contains(u)) {
            c.getSubbedUsers().add(u);
            courseRepository.save(c);
        }

        return "redirect:/courses/"+c.getIdCourse();
    }

    @RequestMapping(value = "/courses/{idCourse}/unsub", method = RequestMethod.POST)
    public String unsubFromACourse(@PathVariable Integer idCourse, Principal principal) {
        User u = userRepository.findByLogin(principal.getName());
        Course c = courseRepository.findById(idCourse).get();

        if(c.getSubbedUsers().contains(u)) {
            c.getSubbedUsers().remove(u);
            courseRepository.save(c);
        }
        return "redirect:/courses/"+c.getIdCourse();
    }

    @RequestMapping(value = "/courses/{idCourse}/delete", method = RequestMethod.POST)
    public String deleteCourse(@ModelAttribute(value = "course") Course course) {
        courseRepository.delete(course);
        return "redirect:/profile";
    }

    @RequestMapping(value = "/courses/{idCourse}/update", method = RequestMethod.GET)
    public String updateCourse(@ModelAttribute(value = "course") Course course, Model m) {
        m.addAttribute("course", courseRepository.findById(course.getIdCourse()).get());
        m.addAttribute("edit", true);
        m.addAttribute("action", "/courses/update");
        return "new_course";
    }

    @RequestMapping(value = "/courses/update", method = RequestMethod.POST)
    public String updateCoursePagePOST(@ModelAttribute(value = "course") @Valid Course course, BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) {
            return "redirect:/courses"+String.valueOf(course.getIdCourse()+"update?error=Podaj nazwe kursu!");
        }
        courseRepository.save(course);
        int id = course.getIdCourse();
        return "redirect:/courses/"+String.valueOf(id);
    }

}
