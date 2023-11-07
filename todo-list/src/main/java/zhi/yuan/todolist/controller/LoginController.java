package zhi.yuan.todolist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author LR
 */
@Controller
public class LoginController {
    @GetMapping("/index")
    public String login() {
        return "index.html";
    }
}
