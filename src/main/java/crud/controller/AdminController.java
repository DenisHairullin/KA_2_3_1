package crud.controller;

import crud.model.Role;
import crud.model.User;
import crud.service.RoleService;
import crud.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder, Validator validator) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    @GetMapping
    public String showAdmin(Model model) {
        model.addAttribute("users", userService.listUsers());
        return "admin";
    }

    @PostMapping(path = "/add")
    public String processAddUser(@Valid User user, Errors errors, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            attributes.addFlashAttribute("fieldErrors", errors.getFieldErrors());
            return "redirect:/admin/validationError";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        return "redirect:/admin";
    }

    @PutMapping(path = "/edit")
    public String processEditUser(User user, RedirectAttributes attributes) {
        Errors errors = new BeanPropertyBindingResult(user, "user");

        if (user.getPassword().isEmpty()) {
            user.setPassword(userService.getUser(user.getId()).getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        validator.validate(user, errors);
        if (errors.hasErrors()) {
            attributes.addFlashAttribute("fieldErrors", errors.getFieldErrors());
            return "redirect:/admin/validationError";
        }
        if (userService.getUser(user.getId()) == null) {
            attributes.addAttribute("id", user.getId());
            return "redirect:/admin/notFoundError";
        }

        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/remove")
    public String processRemoveUser(@RequestParam(name = "id") Long id, RedirectAttributes attributes) {
        if (userService.getUser(id) == null) {
            attributes.addAttribute("id", id);
            return "redirect:/admin/notFoundError";
        }
        userService.removeUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/validationError")
    public String showValidationError() {
        return "errorValidation";
    }

    @GetMapping("/notFoundError")
    public String showNotFoundError() {
        return "errorNotFound";
    }

    @GetMapping(path = "/check")
    @ResponseBody
    public Boolean checkUser(@RequestParam(name = "email") String email) {
        return userService.listUsers().stream().noneMatch(x -> x.getEmail().equalsIgnoreCase(email));
    }

    @ModelAttribute("allRoles")
    public List<Role> listAllRoles()
    {
        return roleService.listRoles();
    }
}