package hello.hellospring.controller;

import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController {
    private final MemberService memberService; //Memberservice 가져와 쓰기
    @Autowired //Spring Container의 MemberService와 연결
    public MemberController(MemberService memberService) { //생성자 호출
        this.memberService = memberService;
    }
}