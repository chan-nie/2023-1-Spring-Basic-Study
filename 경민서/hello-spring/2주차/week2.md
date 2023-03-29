# 2주차

# 섹션 0. 강의 소개

## 강의소개

### 간단한 웹 애플리케이션 개발

- 스프링 프로젝트 생성
- 스프링 부트로 웹 서버 실행
- 회원 도메일 개발
- 웹 MVC 개발
- DB연동 - JDBE, JPA, 스프링 데이터 JPA
- 테스트 케이스 작성

### 강의목표

스프링 학습의 제대로 된 첫 길잡이 역할

- 스프링 기술 그 자체에 매몰 X
- 어떻게 사용하는지
- 오래된, 마이너한 스프링 기술X

### 학습방법

처음부터 끝까지 직접 코딩

### 스프링 완전 정복 로드맵

- 스프링 입문
- 스프링 핵심 원리
- 스프링 웹 MVC
- 스프링 DB 데이터 접근 기술
- 실전! 스프링 부트

---

# 섹션 1. 프로젝트 환경설정

## 프로젝트 생성

[](https://start.spring.io/)

- Project : Gradle-Groovy
- Spring : 2.7.10
- Lan: Java
- Packaging: Jar
- Java: 11 (** 기본이 17로 되어있어서 주의)
- Project Metadata
    - Groupid : hello
    - Antifactid : hello-spring

Dependencies : Spring Web, Thymeleaf

### Gradle 전체 설정

`build.gradle`

```jsx
plugins {
      id 'org.springframework.boot' version '2.3.1.RELEASE'
      id 'io.spring.dependency-management' version '1.0.9.RELEASE'
      id 'java'
}
    group = 'hello'
    version = '0.0.1-SNAPSHOT'
    sourceCompatibility = '11'
    repositories {
      mavenCentral()
}
    dependencies {
      implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
      implementation 'org.springframework.boot:spring-boot-starter-web'
      testImplementation('org.springframework.boot:spring-boot-starter-test') {
exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
	} 
}

test {
	useJUnitPlatform()
}
```

동작 확인

- 기본 메인 클래스 실행
- 스프링 부트 메인 실행 후 `[http://localhost:8080](http://localhost:8080)` → **Whitelabel Error Page 이면 성공**

**Setting:**  IntelliJ Gradle 대신에 자바 직접 실행 - 더 빠름

## 라이브러리 살펴보기

Gradle은 의존관계가 있는 라이브러리를 함께 다운로드 한다. 

### **스프링 부트 라이브러리**

External libraries 보면 엄청 많은 라이브러리가 떙겨져 있음

********************************************************************************************************의존 관계가 있는 모든 라이브러리를 다 땡겨와 줌********************************************************************************************************

오른쪽 옆에 `Gradle`

- spring-boot-starter-tomcat: 톰캣(웹서버)
- Spring-webmvc : 스프링 웹 MVC
- spring-boot-starter-thymeleaf: 타임리프 템플릿 엔진(View)
- sping-boot-starter(공통): 스프링 부트+스프링코어+로깅
    - spring-boot
        - spring-core
    - **spring-boot-starter-logging**
        - 실무에서 사용됨
        - logback-classic, slf4j
- **Junit 5**

### **테스트 라이브러리**

- spring-boot-starter-test
    - junit: 테스트 프레임워크
    - mockito: 목 라이브러리
    - assertj: 테스트 코드를 좀 더 편하게 작성하게 도와주는 라이브러리
    - spring-test: 스프링 통합 테스트 지원

## View 환경설정

### Welcome Page 만들기

`resources/static/index.html`

```html
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

- 스프링 부트가 제공하는 Welcome Page 기능
    - `static/index.html` 을 올리면 Welcome page 기능을 제공
    - 검색하면서 학습하길 바람
    
    [Spring Boot Features](https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-mvc-welcome-page)
    

**thymeleaf 템플릿 엔진**

- thymeleaf 공식 사이트: [https://www.thymeleaf.org/](https://www.thymeleaf.org/)
- 스프링 공식 튜토리얼: [https://spring.io/guides/gs/serving-web-content/](https://spring.io/guides/gs/serving-web-content/)
- 스프링부트 메뉴얼: [https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/
html/spring-boot-features.html#boot-features-spring-mvc-template-engines](https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-mvc-template-engines)

```java
@Controller
public class HelloController {

	 @GetMapping("hello")
	 public String hello(Model model) {
		 model.addAttribute("data", "hello!!");
		 return "hello";
	 }
}
```

`resources/templates/hello.html`

```java
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

**thymeleaf 템플릿엔진 동작 확인**

- [http://localhost:8080/hello](http://localhost:8080/hello)

************************동작 환경************************

![Untitled](2%E1%84%8C%E1%85%AE%E1%84%8E%E1%85%A1%2095acf37685cf4bccbb276560f96b880e/Untitled.png)

- 컨트롤러에서 리턴 값으로 문자를 반환하면 뷰 리졸버 `viewResolver`가 화면을 찾아서 처리한다.
    - 스프링 부트 템플릿엔진 기본 viewName 매핑
    - `resources:templates/` +{ViewName}+ `.html`
    

## 빌드하고 실행하기

1. **cmd**
2. `cd C:\Users\sookmyung\Documents\GitHub\2023-1-Spring-Basic-Study\경민서\hello-spring\hello-spring`
3. `gradlew build`
4. `cd bulid`
5. `cd libs`
6. `java -jar hello-spring-0.0.1-SNAPSHOT.jar`

---

# 섹션 2. 스프링 웹 개발 기초

## 정적 컨텐츠

- 스프링 부트 정적 컨텐츠 기능/ 자동으로 제공해줌

[Spring Boot Features](https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-mvc-static-content)

`resources/static/hello-static.html`

```html
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

- 실행 [`http://localhost:8080/hello-static.html`](http://localhost:8080/hello-static.html)

![내장 톰캣 서버가 요청을 받고 스프링한테 넘김/ hello-static 이라는 컨트롤러를 찾음/ 없으면 내부에 있는 hello-static.html을 찾아서 줌](2%E1%84%8C%E1%85%AE%E1%84%8E%E1%85%A1%2095acf37685cf4bccbb276560f96b880e/Untitled%201.png)

내장 톰캣 서버가 요청을 받고 스프링한테 넘김/ hello-static 이라는 컨트롤러를 찾음/ 없으면 내부에 있는 hello-static.html을 찾아서 줌

## MVC와 템플릿 엔진

- MVC: Model, View(화면 그리기), Controller(내부적 처리)
- 예전과 다르게(model1 방식) controller와 view를 쪼개서 사용

**Controller**

```java
@Controller
public class HelloController {
 
	@GetMapping("hello-mvc")
	public String helloMvc(@RequestParam("name") String name, Model model) {
		model.addAttribute("name", name);
		return "hello-template";
	 }
}
```

********View********

`resources/templates/hello-template.html`

```html
<html xmlns:th="http://www.thymeleaf.org">
<body>
<p th:text="'hello ' + ${name}">hello! empty</p>
</body>
</html>
```

******실행******

[`http://localhost:8080/hello-mvc`](http://localhost:8080/hello-mvc?name=spring) → WARN 9196 : Required request parameter…

`@RequestParam("name")`: 무조건 값을 넘겨 줘야 작동

[`http://localhost:8080/hello-mvc?name=spring`](http://localhost:8080/hello-mvc?name=spring)로 실행해야 함

![톰캣이 넘김/ 스프링이 helloController에 mapping 이 되어 있음을 확인 / method 호출/ viewReslover(view 찾기 템플릿 엔진 연결)가 동작/ 변환을 한 HTML을 반환함(↔ 정적 컨텐츠)](2%E1%84%8C%E1%85%AE%E1%84%8E%E1%85%A1%2095acf37685cf4bccbb276560f96b880e/Untitled%202.png)

톰캣이 넘김/ 스프링이 helloController에 mapping 이 되어 있음을 확인 / method 호출/ viewReslover(view 찾기 템플릿 엔진 연결)가 동작/ 변환을 한 HTML을 반환함(↔ 정적 컨텐츠)

## API

### @ResponseBody 문자 반환

```java
@Controller
public class HelloController {

	@GetMapping("hello-string")
	@ResponseBody
	public String helloString(@RequestParam("name") String name) {
	return "hello " + name;
	}

}
```

- `@ResponseBody` 를 사용하면 viewResolver 사용 X
- HTTP에서 HEAD와 BODY가 있음 : BODY에 내용을 직접 넣어주겠다는 것임

실행 [`http://localhost:8080/hello-string?name=spring`](http://localhost:8080/hello-string?name=spring)

### @ResponseBody 객체 반환

```java
@Controller
public class HelloController {
	@GetMapping("hello-api")
  @ResponseBody
  public Hello helloApi(@RequestParam("name") String name) { //API 방식
      Hello hello = new Hello();
      hello.setName(name);
      return hello;
  }

  static class Hello {
      private String name;

      public String getname() {
          return name;
      }

      public void setName(String name) {
          this.name = name;
      }
   }
}
```

- `@ResponseBody` 를 사용하고, 객체를 반환하면 객체가 **JSON(↔ XML)**으로 변환됨

실행 [`http://localhost:8080/hello-api?name=spring`](http://localhost:8080/hello-api?name=spring)

![@ResponseBody가 있으면 그대로 넘겨야 겠구나/ 근데 반환이 문자가 아니라 객체/ default는 JSON 방식으로 만들어서 반환](2%E1%84%8C%E1%85%AE%E1%84%8E%E1%85%A1%2095acf37685cf4bccbb276560f96b880e/Untitled%203.png)

@ResponseBody가 있으면 그대로 넘겨야 겠구나/ 근데 반환이 문자가 아니라 객체/ default는 JSON 방식으로 만들어서 반환

`@ResponseBody` 를 사용

- HTTP의 BODY에 문자 내용을 직접 반환
- viewResolver 대신에 HttpMessageConverter 가 동작
- 기본 Srting처리: StringHttpMessageConverter
- 기본 객체처리: MappingJackson2HttpMessageConverter : JSON으로 반환
- byte 처리 등등 기타 여러 HttpMessageConverter가 기본으로 등록되어 있음