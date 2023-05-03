# 01. 홈화면 추가

* 홈 컨트롤러 추가

~~~java
package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
 @GetMapping("/")
 public String home() {
 return "home";
 }
}
~~~

* 회원 관리용 홈 

~~~html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>

<div class="container">
 <div>
    <h1>Hello Spring</h1>
    <p>회원 기능</p>
    <p>
        <a href="/members/new">회원 가입</a>
        <a href="/members">회원 목록</a>
    </p>
 </div>

</div> <!-- /container -->

</body>
</html>
~~~
    * 회원 가입을 누르면 /members/new로 이동
    * 회원 목록을 누르면 /members로 이동
        - 현재 두 페이지 다 내용 X -> error 표시 
        - 원래 아무것도 없으면 index.html이 열려야 함 -> 컨트롤러가 정적파일보다 우선순위가 높기 때문 -> 홈 컨트롤러에서 먼저 찾아봄 -> 맵핑된게 있기 때문에 static 파일 실행 X


# 02. 회원 등록

1. 회원 등록 폼 개발
* 회원 등록 폼 컨트롤러
~~~java
@Controller
public class MemberController {
 
 private final MemberService memberService;
 
 @Autowired
 public MemberController(MemberService memberService) {
 this.memberService = memberService;
 }
 
 @GetMapping(value = "/members/new")
 public String createForm() {
 return "members/createMemberForm";
 }

}
~~~

* 회원 등록 폼 HTMl(/members/createMemberForm)

~~~html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>

<div class="container">
 
    <form action="/members/new" method="post">
        <div class="form-group">
            <label for="name">이름</label>
            <input type="text" id="name" name="name" placeholder="이름을
입력하세요">
        </div>
        <button type="submit">등록</button>
    </form>

</div> <!-- /container -->

</body>
</html>
~~~
    * html의 form 태그 사용


2. 회원 등록 컨트롤러
* 웹 등록 화면에서 데이터를 전달 받을 폼 객체

~~~java
package hello.hellospring.controller;

public class MemberForm {
 private String name;
 public String getName() {
    
    return name;
 }
 public void setName(String name) {
    this.name = name;
 }
}
~~~
    * 입력된 name을 setName을 통해 넣어줌

* 회원 컨트롤러에서 회원을 실제 등록하는 기능
~~~java
@PostMapping(value = "/members/new")
public String create(MemberForm form) {
 
 Member member = new Member();
 member.setName(form.getName());
 
 memberService.join(member);
 
 return "redirect:/";
}
~~~
    * return "redirect:/" : 홈화면으로 돌아가기
    * 데이터를 전달해야 하기 때문에 PostMapping
    * 입력된 name이 MemberForm 컨트롤러의 name에 저장됨(setName)

# 03. 회원 조회

* 회원 컨트롤러에서 조회 기능
~~~java
@GetMapping(value = "/members")
public String list(Model model) {
 List<Member> members = memberService.findMembers();
 model.addAttribute("members", members);
 return "members/memberList";
}
~~~
    * member list를 모델에 담음

* 회원 리스트 HTML 
~~~html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>

<div class="container">
 <div>
    <table>
        <thead>
        <tr>
            <th>#</th>
            <th>이름</th>
        </tr>
        </thead>
        <tbody>
            <tr th:each="member : ${members}">
             <td th:text="${member.id}"></td>
             <td th:text="${member.name}"></td>
            </tr>
        </tbody>
    </table>
</div>

</div> <!-- /container -->

</body>
</html>
~~~

    * ${members} : 모델 안에 저장된 값을 꺼냄