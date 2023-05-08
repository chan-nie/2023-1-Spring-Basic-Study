# 4주차

### 회원 관리 예제 - 웹 mvc 개발

- 회원 웹 기능 - 홈 화면 추가
    
    회원 등록 폼 컨트롤러
    
    ```java
    // HomeController.java
    
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
    ```
    
    ```html
    // home.html
    
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
    ```
    
    정척 컨텐츠 파트에서 웹브라우저에서 요청이 오면 관련 controller가 있는지 먼저 찾고 없으면 static 파일 찾도록 되어 있어서 기존의 html 파일보다 home.html이 먼저 불러온다.
    
- 회원 웹 기능 - 등록
    
    ```html
    // createMemberForm.html
    
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
    ```
    
    웹 등록 화면에서 데이터를 전달 받을 폼 객체
    
    ```java
    // MemberForm.java
    
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
    ```
    
    회원 컨트롤러에서 회원을 실제 등록하는 기능
    
    ```java
    // MemberController.java
    
    @GetMapping("/members/new")
        public String createForm() {
            return "members/createMemberForm";
        }
    
        @PostMapping("/members/new")
        public String create(MemberForm form) {
            Member member = new Member();
            member.setName(form.getName());
    
            memberService.join(member);
    
            return "redirect/";
        }
    ```
    
- 회원 웹 기능 - 조회
    
    회원 컨트롤러에서 조회 기능
    
    ```java
    // MemberController.java
    
    @GetMapping("/members")
        public String list(Model model) {
            List<Member> members = memberService.findMembers();
            model.addAttribute("members", members);
            return "members.memberList";
        }
    ```
    
    회원 리스트 HTML
    
    ```html
    // memberList.html
    
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
    ```