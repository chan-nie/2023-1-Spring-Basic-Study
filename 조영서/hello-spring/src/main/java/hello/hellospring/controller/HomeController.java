package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/") //도메인의 첫번째
    public String home() {
        return "home"; //home.html이 호출
    }
}