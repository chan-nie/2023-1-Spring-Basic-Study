# 섹션 5. 회원 관리 예제 - 웹 MVC 개발

[회원 웹 기능 - 홈 화면 추가](https://www.notion.so/11d8b1f706094f858cbdeb35b15963ba) 

[회원 웹 기능 - 등록](https://www.notion.so/14db177158754aa68f24860b7822c4a4) 

[회원 웹 기능 - 조회](https://www.notion.so/252e855f85ae46d3802c9d666c650599) 

## 회원 웹 기능 - 홈 화면 추가

**************************************홈 컨트롤러 추가**************************************

```java
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

**회원 관리용 홈**

```html
> 참고: 컨트롤러가 정적 파일보다 우선순위가 높다.<!DOCTYPE HTML>
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

> 요청이 오면 내장 톰캣 서버는 스프링 컨테이너에서 관련 컨트롤러가 있는지 찾고, 없으면 static 파일을 찾게 되어 있음
참고: 컨트롤러가 정적 파일보다 우선순위가 높다.
> 
- `````실행 : [http://localhost:8080/](http://localhost:8080/)`
    
    ![Untitled](%E1%84%89%E1%85%A6%E1%86%A8%E1%84%89%E1%85%A7%E1%86%AB%205%20%E1%84%92%E1%85%AC%E1%84%8B%E1%85%AF%E1%86%AB%20%E1%84%80%E1%85%AA%E1%86%AB%E1%84%85%E1%85%B5%20%E1%84%8B%E1%85%A8%E1%84%8C%E1%85%A6%20-%20%E1%84%8B%E1%85%B0%E1%86%B8%20MVC%20%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%203ebaed5fe6ef48c993752b62a697deb1/Untitled.png)
    
    ![Untitled](%E1%84%89%E1%85%A6%E1%86%A8%E1%84%89%E1%85%A7%E1%86%AB%205%20%E1%84%92%E1%85%AC%E1%84%8B%E1%85%AF%E1%86%AB%20%E1%84%80%E1%85%AA%E1%86%AB%E1%84%85%E1%85%B5%20%E1%84%8B%E1%85%A8%E1%84%8C%E1%85%A6%20-%20%E1%84%8B%E1%85%B0%E1%86%B8%20MVC%20%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%203ebaed5fe6ef48c993752b62a697deb1/Untitled%201.png)
    

## 회원 웹 기능 - 등록

### ********************************************회원 등록 폼 개발********************************************

****************************************회원 등록 폼 컨트롤****************************************

```java
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
```

**회원 등록 폼 HTML** ( `resources/templates/members/createMemberForm` )

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<div class="container">
	 <form action="/members/new" method="post">
		 <div class="form-group">
			 <label for="name">이름</label>
			 <input type="text" id="name" name="name" placeholder="이름을 입력하세요">
		 </div>
		 <button type="submit">등록</button>
	 </form>
</div> <!-- /container -->
</body>
</html>
```

### **회원 등록 컨트롤러**

**웹 등록 화면에서 데이터를 전달 받을 폼 객체**

```java
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

**회원 컨트롤러에서 회원을 실제 등록하는 기능**

```java
@PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);

        return "redirect:/";
    }
```

`@PostMapping` : html <form>에서 값을 post 방식으로 받아올 때 사용

## 회원 웹 기능 - 조회

**회원 컨트롤러에서 조회 기능**

```java
@GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberlist";
    }
```

**회원 리스트 HTML**

```html
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

`th:each` 루프를 돌면서 실행 thymleaf 문법

- [`http://localhost:8080/members`](http://localhost:8080/members)
    
    ![Untitled](%E1%84%89%E1%85%A6%E1%86%A8%E1%84%89%E1%85%A7%E1%86%AB%205%20%E1%84%92%E1%85%AC%E1%84%8B%E1%85%AF%E1%86%AB%20%E1%84%80%E1%85%AA%E1%86%AB%E1%84%85%E1%85%B5%20%E1%84%8B%E1%85%A8%E1%84%8C%E1%85%A6%20-%20%E1%84%8B%E1%85%B0%E1%86%B8%20MVC%20%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%203ebaed5fe6ef48c993752b62a697deb1/Untitled%202.png)
    

> 참고: HTTP, HTML form등 웹 MVC와 관련된 자세한 내용은 스프링 웹 MVC 강의에서 다룰 예정이다.
>