package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MemberController {
    private final MemberService memberService;
    @Autowired
    public MemberController(MemberService memberService) { this.memberService = memberService;
    }
    @GetMapping(value="/members/new")
    public String createForm(){
        return "members/createMemberForm"; // createForm 만들어내기 위해, templete 에 members 디렉토리 만들어줌
    }

    @PostMapping(value = "/members/new")
    public String create(@org.jetbrains.annotations.NotNull MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());  // 이렇게 해서 멤버 만들어짐
        memberService.join(member);  //( 조인 로직을 통해 넣음 당함)
        return "redirect:/";  // 홈 화면으로 다시 보내는 코드  ( 나중에 회원 목록 만들어서 구현할 것임)
    }

    @GetMapping(value = "/members/new")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
