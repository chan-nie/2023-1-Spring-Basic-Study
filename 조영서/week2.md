# Section 0

### **간단한 웹 애플리케이션 개발**

- 스프링 프로젝트 생성
- 스프링 부트로 웹 서버 실행
- 회원 도메인 개발
- 웹 MVC 개발
- DB 연동 - JDBC, JPA, 스프링 데이터 JPA
- 테스트 케이스 작성

+ 직접 코딩하고 전반적인 그림을 머릿속에 그리라 하심

+ 요즘은 웹으로 개발하기 때문에 중요함

# Section 1

**프로젝트 생성**

1. 사전 준비물 : Java11, IDE(InteliJ) 설치
2. 스프링 부트 스타터 → 스프링 프로젝트 생성 : [Spring Initializr](https://start.spring.io/)

![https://blog.kakaocdn.net/dn/b0kMtA/btrZxej4DL8/q8SjgGBhIKJSc1xTG1SSZ1/img.png](https://blog.kakaocdn.net/dn/b0kMtA/btrZxej4DL8/q8SjgGBhIKJSc1xTG1SSZ1/img.png)

- Maven, Gradle : 필요한 라이브러리를 가져오고 빌드하는 라이프 사이클까지 관리하는 툴, 요즘 추세는 Gradle
- Group : 기업 도메인명
- Artifact : 프로젝트
- Dependencies : 어떤 라이브러리를 가져와 쓸지 선택
- Thymeleaf : html을 만들어주는 템플릿 엔진

3. Generate하여 IntelliJ에서 build.gradle 파일을 프로젝트로 open

**Error**

~~그 사이에.. Error가 남 ㅋㅋ~~

A problem occurred configuring root project 어쩌구

**Solve**

버전 문제 ^^

스프링 부트와 자바의 버전을 맞춰줘야 했다.. ㅎㅎ

4. 프로젝트 구조

- .idea : IntelliJ가 사용하는 설정 파일
- src : main, test 폴더 구분
    - main : 실제 패키지와 관련 소스들
    - ***test: 테스트 코드와 관련된 소스들***
- resources : 실제 java 파일을 제외한 나머지
- ***buil.gradle***
    - 버전 설정과 라이브러리 가져오기
    - sourceCompatibility : Java의 버전
    - repositories : 라이브러리를 다운 받아오는 곳
    - dependencies : JUnit 5 라이브러리가 기본으로 포함됨
- .gitignore : 소스코드 관리, build 된 결과물 등을 제외하도록
- Compact Middle Package로 경로 단축 가능

![https://blog.kakaocdn.net/dn/qFvLw/btr6DLKoDiq/bhdzBMsJHW50ZvhdHxYg00/img.png](https://blog.kakaocdn.net/dn/qFvLw/btr6DLKoDiq/bhdzBMsJHW50ZvhdHxYg00/img.png)

5. 실행 후 localhost:8080 접속

![https://blog.kakaocdn.net/dn/MeWts/btr6OpZTQfW/baOayTG9mv6cXTIzFk9LwK/img.png](https://blog.kakaocdn.net/dn/MeWts/btr6OpZTQfW/baOayTG9mv6cXTIzFk9LwK/img.png)

![https://blog.kakaocdn.net/dn/p3fV6/btr6K3DqCJN/utv1jQHKIIGwfZPkIotKpk/img.png](https://blog.kakaocdn.net/dn/p3fV6/btr6K3DqCJN/utv1jQHKIIGwfZPkIotKpk/img.png)

- main 메소드 실행시 SpringApplication.run 실행 시 SpringBootApplication 실행
    - 톰캣이나 웹서버를 내장하고 있기 때문에 스프링 부트가 같이 올라 옴

6. Gradle 설정 변경 for 속도 향상

![https://blog.kakaocdn.net/dn/mx3OS/btr6N6zwtAk/Xx4yx1MuLEKK3qkwTRNUSK/img.png](https://blog.kakaocdn.net/dn/mx3OS/btr6N6zwtAk/Xx4yx1MuLEKK3qkwTRNUSK/img.png)

### **라이브러리 살펴보기**

- External Libraries 확인
    - Maven과 Gradle은 라이브러리의 의존 관계를 모두 관리 해줌
    - Gradle은 Spring Core까지 다 가지고 옴
- 의존 관계 확인
    - 라이브러리를 빌드해 웹 서버에 올리는 것으로 단순하게 작업 가능

![https://blog.kakaocdn.net/dn/bupBYV/btr6DX43GLG/prdQHOqaMKKQjf7azI1hP0/img.png](https://blog.kakaocdn.net/dn/bupBYV/btr6DX43GLG/prdQHOqaMKKQjf7azI1hP0/img.png)

- log를 사용해야 심각한 오류를 따로 확인 가능
    - slf4j : 인터페이스
    - logback : 어떤 구현체로 로그를 출력할지

![https://blog.kakaocdn.net/dn/oft5J/btr6Gp7XgVm/wPVZ7ix4nUyRVoeAlxQ8JK/img.png](https://blog.kakaocdn.net/dn/oft5J/btr6Gp7XgVm/wPVZ7ix4nUyRVoeAlxQ8JK/img.png)

- 스프링 부트 라이브러리
    - spring-boot-starter-web
        - spring-boot-starter-tomcat: 톰캣 (웹서버)
        - spring-webmvc: 스프링 웹 MVC
    - spring-boot-starter-thymeleaf: 타임리프 템플릿 엔진(View)
    - spring-boot-starter(공통): 스프링 부트 + 스프링 코어 + 로깅
- 테스트 라이브러리
    - spring-boot-starter-test
        - junit: 테스트 프레임워크 (junit5로 추세가 넘어가는 )
        - mockito: 목 라이브러리
        - assertj: 테스트 코드를 좀 더 편하게 작성하게 도와주는 라이브러리
        - spring-test: 스프링 통합 테스트 지원

### **View 환경설정**

1. resources/static/index.html 생성 (정적페이지)

```
<!DOCTYPE HTML>
<html>
<head>
    <title>Hello</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
Hello
<a href="/hello">hello</a>
</body>
</html>
```

![https://blog.kakaocdn.net/dn/GCedd/btr6DYJLe6X/ZHr04mkkjzQZtP6zvXTup0/img.png](https://blog.kakaocdn.net/dn/GCedd/btr6DYJLe6X/ZHr04mkkjzQZtP6zvXTup0/img.png)

- 스프링 부트는 기능이 많기 때문에 필요에 따라 찾는 능력이 중요
    - [Spring Boot](https://spring.io/projects/spring-boot#learn)

2. Thymleaf 템플릿 엔진 사용

- thymeleaf 공식 사이트: [https://www.thymeleaf.org/](https://www.thymeleaf.org/)
- 스프링 공식 튜토리얼: [https://spring.io/guides/gs/serving-web-content/](https://spring.io/guides/gs/serving-web-content/)
- 스프링부트 메뉴얼: [Spring Boot Features](https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-mvc-template-engines)

3. java/hello/hellospring/controller/HelloController.java 생성

- 웹 어플리케이션에 /hello 입력 시 메소드 실행
- key가 data, value가 hello!!

```
@Controller
public class HelloController {
    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello!!");
        return "hello";
    }
}
```

4. resources/templates/hello.html 생성

- ${data}가 hello!!로 치환

```
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <title>Hello</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<p th:text="'안녕하세요. ' + ${data}" >안녕하세요. 손님</p>
</body>
</html>
```

![https://blog.kakaocdn.net/dn/dpGaXB/btr6NZN78qz/GFEcwJOBPo5L7koqFLYkeK/img.png](https://blog.kakaocdn.net/dn/dpGaXB/btr6NZN78qz/GFEcwJOBPo5L7koqFLYkeK/img.png)

- 동작 환경 그림

![https://blog.kakaocdn.net/dn/l4Kx9/btr6EVTFwZf/OrSQkm60B4l2DGo24YvtjK/img.png](https://blog.kakaocdn.net/dn/l4Kx9/btr6EVTFwZf/OrSQkm60B4l2DGo24YvtjK/img.png)

- 컨트롤러에서 리턴 값으로 문자를 반환하면 뷰 리졸버(viewResolver)가 화면을 찾아서 처리
    - 스프링 부트 템플릿엔진 기본 viewName 매핑
    - resources:templates/ + {ViewName} + .html

### **빌드하고 실행하기**

1. cmd 창에서 프로젝트 위치로 이동
2. gradlew 입력
3. gradlew build 입력
4. build/libs로 이동
5. java -jar hello-spring-0.0.1-SNAPSHOT.jar 입력

**Error**

명령어를 찾을 수 없다 .. 어쩌구

**Solve**

1. PowerShell이 아닌 cmd에서 하고 있는지 확인

2. Java JDK 설치가 되어 있는지 확인

3. 고급 시스템 속성의 PATH 설정이 잘 되어 있는지 확인

# Section 2

### **정적 컨텐츠**

- 파일을 그대로 웹 브라우저에 전달
1. resources/static/hello-static.html 생성

```
<!DOCTYPE HTML>
<html>
<head>
  <title>static content</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
정적 컨텐츠 입니다.
</body>
</html>
```

![https://blog.kakaocdn.net/dn/WZk3u/btr6NkSVfx0/kK9vIP4kXZOgzeRZpdCbA0/img.png](https://blog.kakaocdn.net/dn/WZk3u/btr6NkSVfx0/kK9vIP4kXZOgzeRZpdCbA0/img.png)

- 구동 방식

![https://blog.kakaocdn.net/dn/bGKl6z/btr6FgKqwcL/5cyKQ7fOkidNcvbP8YpiZ1/img.png](https://blog.kakaocdn.net/dn/bGKl6z/btr6FgKqwcL/5cyKQ7fOkidNcvbP8YpiZ1/img.png)

### **MVC와 템플릿 엔진**

- MVC : Model, View, Controller
    - Model : 화면에 필요한 것만 담
    - View : 화면을 그리는 데에 집중
    - Controller : 비즈니스 로직 같은 내부적인 것에 집중
1. HelloController.java에 추가
    - @RequestParam : 외부에서 파라미터를 받을 것

```
@GetMapping("hello-mvc")
 public String helloMvc(@RequestParam("name") String name, Model model) {
 model.addAttribute("name", name);
 return "hello-template";
 }
```

2. resources/templates/hello-template.html 생성

- thymleaf 템플릿 : 서버 없이도 파일의 absolute path로 접속 시 껍데기를 볼 수 있음 (hello! empty가 뜸)

```
<html xmlns:th="http://www.thymeleaf.org">
<body>
<p th:text="'hello ' + ${name}">hello! empty</p>
</body>
</html>
```

![https://blog.kakaocdn.net/dn/O2YwM/btr6Nk6tNMY/0o9QDa6WGENGOSZjNxBcKK/img.png](https://blog.kakaocdn.net/dn/O2YwM/btr6Nk6tNMY/0o9QDa6WGENGOSZjNxBcKK/img.png)

- 구동 방식
    - 변환하여 웹 브라우저에 반환하는 것이 포인트

## **API**

1. HelloController.java에 추가

```
@GetMapping("hello-string")
@ResponseBody
public String helloString(@RequestParam("name") String name) {
    return "hello " + name;
}
```

- @ResponseBody : http의 body부분에 데이터를 직접 넣어주겠다는 뜻, 요청한 클라이언트에  내려감, 템플릿 엔진과 달리 View가 없음
    
    그대로
    

```
@GetMapping("hello-api")
@ResponseBody
public Hello helloApi(@RequestParam("name") String name) {//객체 생성
    Hello hello = new Hello();
    hello.setName(name);//파라미터로 넘어온 name을 넣음return hello;//객체를 넘김
}
static class Hello {
    private String name;//keypublic String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
```

- Json 방식으로 데이터를 넘김

Json은 속성-값 쌍, 배열 자료형 또는 기타 모든 시리얼화 가능한 값 또는 "키-값 쌍"으로 이루어진 데이터 오브젝트를 전달하기 위해 인간이 읽을 수 있는 텍스트를 사용하는 개방형 표준 포맷이다.

요즘 추세는 JSON 방식, Spring도 기본이 JSON 방식으로 반환하는 것이다.

- @ResponseBody 사용 원리

![https://blog.kakaocdn.net/dn/rmLKN/btr7QnmdX1z/HQWA0uOtNW1ptvC7gw2JJk/img.png](https://blog.kakaocdn.net/dn/rmLKN/btr7QnmdX1z/HQWA0uOtNW1ptvC7gw2JJk/img.png)

- HTTP의 BODY에 문자 내용을 직접 반환
- viewResolver 대신에 HttpMessageConverter 가 동작
- 기본 문자처리: StringHttpMessageConverter
- 기본 객체처리: MappingJackson2HttpMessageConverter (객체를 Json으로 바)
- byte 처리 등등 기타 여러 HttpMessageConverter가 기본으로 등록되어 있음
- 객체가 오면 Json 방식으로 데이터를 만들어 Http 응답에 반환하겠다는 것이 기본 정책
