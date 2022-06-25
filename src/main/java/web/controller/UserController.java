package web.controller;

import data.model.User;
import data.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add")
    public String processAddUser(User user) {
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

    @PostMapping(value = "/edit", params = "action=Submit")
    public String processEditUser(User user) {
        userService.updateUser(user);
        return "redirect:/users";
    }

    @PostMapping(value = "/edit", params = "action=Cancel")
    public String cancelEditUser() {
        return "redirect:/users";
    }
}