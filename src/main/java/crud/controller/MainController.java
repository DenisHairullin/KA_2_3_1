package crud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    @GetMapping
    String showMain(Model model) {
        return "blank";
    }

    @GetMapping("/denied")
    String showDenied() {
        return "denied";
    }
}
