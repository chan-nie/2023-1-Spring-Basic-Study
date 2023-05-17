package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")   //처음에 호출됨. 처음에 호출되는 html 을 만들기 위해서 templete 에 home.html 만들기
    //local host 8080 으로 들어가면 아까전에 만들었던 html 화면이 보임.
    //왤컴 페이지로 갈 줄 알았는데, 다른 페이지로 감. ( 우선순위가 존재 )

    //요청이 오면 관련 컨트롤러 찾고, 없으면 static 파일 찾음.
    //mapping 된 url 있어서 , static 에 있는 index.html 은 실행되지 않은 것임.
    public String home() {
        return "home";
    }
}
