package web.controller;

import data.model.User;
import data.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUsers(Model model) {
        model.addAttribute("users", userService.listUsers());
        return "users";
    }

    @GetMapping("/add")
    public String showAddUser(Model model) {
        model.addAttribute("user", new User());
        return "userForm";
    }

    @PostMapping(path = "/add", params = "action=Submit")
    public String processAddUser(@Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            return "userForm";
        }
        userService.addUser(user);
        return "redirect:/users";
    }

    @GetMapping("/remove")
    public String processRemoveUser(@RequestParam(name = "id") Long id) {
        userService.removeUser(id);
        return "redirect:/users";
    }

    @GetMapping("/edit")
    public String showEditUser(Model model, @RequestParam(name = "id") Long id)
    {
        model.addAttribute("user", userService.getUser(id));
        return "userForm";
    }

    @PostMapping(path = "/edit", params = "action=Submit")
    public String processEditUser(@Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            return "userForm";
        }
        userService.updateUser(user);
        return "redirect:/users";
    }

    @PostMapping(path = {"add", "/edit"}, params = "action=Cancel")
    public String cancelEditUser() {
        return "redirect:/users";
    }
}