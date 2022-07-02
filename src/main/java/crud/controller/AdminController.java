package crud.controller;

import crud.model.Role;
import crud.model.User;
import crud.service.RoleService;
import crud.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showUsers(Model model) {
        model.addAttribute("users", userService.listUsers());
        return "userTable";
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        return "redirect:/admin";
    }

    @PostMapping(path = "add", params = "action=Cancel")
    public String cancelAddUser() {
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String showEditUser(Model model, @RequestParam(name = "id") Long id) {
        User user = userService.getUser(id);
        if (user == null) {
            return "userNotFound";
        }
        model.addAttribute("user", user);
        return "userForm";
    }

    @PostMapping(path = "/edit", params = "action=Submit")
    public String processEditUser(@Valid User user, Errors errors) {
        System.out.println(user);
        if (errors.hasErrors()) {
            return "userForm";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping(path = "/edit", params = "action=Cancel")
    public String cancelEditUser() {
        return "redirect:/admin";
    }

    @GetMapping("/remove")
    public String processRemoveUser(@RequestParam(name = "id") Long id) {
        if (userService.getUser(id) == null) {
            return "userNotFound";
        }
        userService.removeUser(id);
        return "redirect:/admin";
    }

    @ModelAttribute("allRoles")
    public List<Role> listAllRoles()
    {
        return roleService.listRoles();
    }
}