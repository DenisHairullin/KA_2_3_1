package crud.web;

import crud.data.model.User;
import crud.data.service.UserService;
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
        model.addAttribute("users", userService.findAll());
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
        userService.save(user);
        return "redirect:/users";
    }

    @PostMapping(path = "add", params = "action=Cancel")
    public String cancelAddUser() {
        return "redirect:/users";
    }

    @GetMapping("/edit")
    public String showEditUser(Model model, @RequestParam(name = "id") Long id)
    {
        model.addAttribute("user", userService.findById(id).orElseThrow(
                () -> new IllegalArgumentException("User with id " + id + " not found")
        ));
        return "userForm";
    }

    @PostMapping(path = "/edit", params = "action=Submit")
    public String processEditUser(@Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            return "userForm";
        }
        userService.save(user);
        return "redirect:/users";
    }

    @PostMapping(path = "/edit", params = "action=Cancel")
    public String cancelEditUser() {
        return "redirect:/users";
    }

    @GetMapping("/remove")
    public String processRemoveUser(@RequestParam(name = "id") Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }
}