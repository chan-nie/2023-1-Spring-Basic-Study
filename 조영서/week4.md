# Section 5


### **회원 웹 기능 - 홈 화면 추가**

1. .../src/main/java/hello/hellospring/controller/HomeController.java 을 생성해서 홈 컨트롤러 추가

```
@Controller
public class HomeController {
    @GetMapping("/")//도메인의 첫번째public String home() {
        return "home";//home.html이 호출
    }
}
```

2. .../src/main/resources/templates/home.html 을 생성해서 회원 관리용 홈 생성

```
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
</div><!-- /container --></body>
</html>
```

3. HelloSpringApplication.java 실행 시

![https://blog.kakaocdn.net/dn/caAcDT/btsd1h21KGI/z487Qe3vcGydGAYo8YDKm0/img.png](https://blog.kakaocdn.net/dn/caAcDT/btsd1h21KGI/z487Qe3vcGydGAYo8YDKm0/img.png)

- localhost:8080 접속 시 홈 화면에 매핑된 html이 있어 호출해와 끝남, 기존에 만든 정적 파일은 무시
- 우선 순위 : 컨트롤러 > 정적 파일

### **회원 웹 기능 - 등록**

1. MemberController.java에 코드 추가

```
@Controller
public class MemberController {
    private final MemberService memberService;//Memberservice 가져와 쓰기@Autowired//Spring Container의 MemberService와 연결public MemberController(MemberService memberService) {//생성자 호출this.memberService = memberService;
    }

    @GetMapping(value = "/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }
}
```

2. .../src/main/resources/templates/members/createMemberForm.html 생성

```
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
</div><!-- /container --></body>
</html>
```

3. Run 후 localhost:8080 접속 => 회원 등록 껍데기 완성

![https://blog.kakaocdn.net/dn/b4kiRh/btsd3d7fSEi/1kIeneR8pieff8RZooqoiK/img.png](https://blog.kakaocdn.net/dn/b4kiRh/btsd3d7fSEi/1kIeneR8pieff8RZooqoiK/img.png)

4. .../src/main/java/hello/hellospring/controller/MemberForm.java 생성

```
public class MemberForm {
    private String name;//createMemberForm.html의 name과 매칭되며 데이터가 들어옴public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

```

5. MemberController.java에 코드 추가

```
@Controller
public class MemberController {
    private final MemberService memberService;//Memberservice 가져와 쓰기@Autowired//Spring Container의 MemberService와 연결public MemberController(MemberService memberService) {//생성자 호출this.memberService = memberService;
    }

    @GetMapping(value = "/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping(value = "/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());
        memberService.join(member);//member 넘기기return "redirect:/";//홈 화면으로 다시 보내기
    }
}
```

- url에 입력하는 get 방식으로 /members/new에 접근 => members/createMemberForm 접근 => templates의 해당 html로 이동 => name 등록 => method = post =>MemberController의 create 함수로 이동 => MemberForm의 name에 데이터 도착

### **회원 웹 기능 - 조회**

1. MemberController.java 에 코드 추가

```
@GetMapping(value = "/members")
public String list(Model model) {
    List<Member> members = memberService.findMembers();
    model.addAttribute("members", members);//리스트를 모델에 담기return "members/memberList";
}
```

2. .../src/main/resources/templates/members/memberList.html 생성

```
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
</div><!-- /container --></body>
</html>
```

- 타임리프 동작
- 서버를 내렸다가 다시 실행하면 데이터가 모두 삭제됨 => 데이터 베이스에 저장 필요
