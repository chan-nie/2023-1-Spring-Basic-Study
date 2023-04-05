package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    /* 여기서부터 2주차 과제 */
    static class WeekTwo {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
    @GetMapping("hello-weekTwo")
    @ResponseBody
    public WeekTwo helloWeek2(@RequestParam("text") String text, Model model) {
        WeekTwo weekTwo = new WeekTwo();
        weekTwo.text = text;
        return weekTwo;
    }

    @GetMapping("hello-int")
    public String helloInt(@RequestParam("number") int number, Model model) {
        model.addAttribute("number", number);
        return "hello-int";
    }

    /* 여기까지 2주차 과제 */

    @GetMapping("hello")
    public String hello(Model model){
        model.addAttribute("data", "hello!!");
        return "hello";
    }

    @GetMapping("hello-mvc")  // 외부에서 파라미터 받기
    public String helloMvc(@RequestParam("name") String name, Model model){
        model.addAttribute("name", name);
        return "hello-template";
    }

    @GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam("name") String name){
        return "hello" + name;
    }

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    static class Hello {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
