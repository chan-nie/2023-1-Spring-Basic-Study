package hello.hellospring.controller;

import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController {

    // new MemberService()로 새로 생성해서 사용할 수도 있지만,
    // 스프링이 관리하게 되면 모두 스프링 컨테이너에 등록하고, 여기서 받아 쓰도록 바꾸어야 함
    // 다른 여러 컨트롤러들이 member controller를 가져다 쓸 수 있게 됨 -> 여러 개 생성할 필요 없이 하나만 생성해서 공용으로 사용하는 것이 남.
    // 스프링 컨테이너에 등록하고 하나만 사용하며 됨 이제부턴 -> 코드에서 생성자를 통해 연결
    private final MemberService memberService;

    // 해당 컨트롤러의 생성자를 통해 연결, @AutoWired는 멤버서비스를 스프링이 스프링 컨테이너에 있는 멤버 서비스로 연결해줌.
    // 생성자에 @AutoWired가 있으면 스프링이 연관된 객체를 스프링 컨테이너에서 찾아서 넣어줌.
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}
