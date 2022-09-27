package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/newUser")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "newUser";
    }

    @PostMapping("admin/newUser")
    public String createUser(User user, Role role) {
        role = new Role(user.getId(), "ROLE_USER");
        userService.saveUser(user, role);
        return "redirect:/admin";
    }

    @GetMapping("updateUser/{id}")
    public String updateUser(Model model, @PathVariable Integer id) {
        model.addAttribute("user", userService.getUserById(id));
        return "updateUser";
    }

    @PostMapping("updateUser/updateUser/{id}")
    public String update(User user) {
        userService.saveUser(user, new Role(user.getId(), "ROLE_USER"));
        return "redirect:/admin";
    }

    @GetMapping("deleteUser/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }

//    @GetMapping("{id}")
//    public String showUser(@PathVariable Integer id, Model model) {
//        model.addAttribute("user", userService.getUserById(id));
//        return "showUser";
//    }
//
//    @GetMapping("/newUser")
//    public String newUser(Model model) {
//        model.addAttribute("user", new User());
//        return "newUser";
//    }
//
//    @PostMapping("/newUser")
//    public String addUser(@ModelAttribute("user") @Valid User user,
//                          BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "newUser";
//        }
//        userService.addUser(user);
//        return "redirect:/";
//    }
//
//    @GetMapping("/{id}/edit")
//    public String editUser(Model model, @PathVariable("id") Integer id) {
//        model.addAttribute("user", userService.getUserById(id));
//        return "editUser";
//    }
//
//    @PatchMapping("{id}")
//    public String updateUser(@ModelAttribute("user") @Valid User user,
//                             BindingResult bindingResult, @PathVariable("id") Integer id) {
//        if (bindingResult.hasErrors()) {
//            return "editUser";
//        }
//        userService.updateUser(user, id);
//        return "redirect:/";
//    }



    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Integer id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }


}
