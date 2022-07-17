package crud.controller;

import crud.model.Role;
import crud.model.User;
import crud.service.RoleService;
import crud.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserRestController {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    public UserRestController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder, Validator validator) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    @GetMapping("/list")
    public List<User> listUsers() {
        return userService.listUsers();
    }

    @GetMapping("/get")
    public User getUser(@RequestParam("id") Long id) {
        if (userService.getUser(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id '" + id + "' not found");
        }
        return userService.getUser(id);
    }

    @PostMapping("/add")
    public void addUser(@RequestBody @Valid User user, Errors errors) {
        System.out.println("Add user: " + user);
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User validation error: "
                    + errors.getFieldErrors().stream().map(x -> x.getField() + " " + x.getDefaultMessage())
                    .collect(Collectors.joining("; ")));
        }
        if (userService.getUser(user.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with email '" + user.getEmail()
                    + "' already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
    }

    @PutMapping("/update")
    public void updateUser(@RequestBody User user) {
        User userWithId = userService.getUser(user.getId());
        User userWithEmail = userService.getUser(user.getEmail());
        Errors errors = new BeanPropertyBindingResult(user, "user");

        if (userWithId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id '" + user.getId() + "' not found");
        }

        if (user.getPassword().isEmpty()) {
            user.setPassword(userWithId.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        validator.validate(user, errors);
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User validation error: "
                    + errors.getFieldErrors().stream().map(x -> x.getField() + " " + x.getDefaultMessage())
                    .collect(Collectors.joining("; ")));
        }

        if (userWithEmail != null && !userWithEmail.getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with email '" + user.getEmail()
                    + "' already exists");
        }
        userService.updateUser(user);
    }

    @DeleteMapping("/remove")
    public void removeUser(@RequestParam("id") Long id) {
        if (userService.getUser(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id '" + id + "' not found");
        }
        userService.removeUser(id);
    }

    @GetMapping("/roles")
    public List<Role> listRoles() {
        return roleService.listRoles();
    }
}
