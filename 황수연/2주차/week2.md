# 섹션 0. 강의 소개

- 스프링을 왜 해야 하나? - 실무에서 제대로 동작하는 웹 어플리케이션을 개발하기 위해서
- 간단한 웹 애플리케이션 개발을 다음과 같은 순서로 할 예정
    - 스프링 프로젝트 생성
    - 스프링 부트로 웹 서버 실행
    - 회원 도메인 개발
    - 웹 MVC 개발
    - DB 연동 - JDBC, JPA, 스프링 데이터 JPA
    - 테스트 케이스 작성
- 어떤 기술이 어떻게 사용되는지 전반적으로 감을 잡는 것이 목표!

## 강의 목표

- 스프링 학습의 제대로 된 첫 길잡이 역할
- 스프링을 어떻게 사용해야 하는지

## 학습 방법

- 처음부터 끝까지 직접 코딩하기

# 섹션 1. 프로젝트 환경설정

## 프로젝트 생성

- 스프링 부트 스타터 사이트로 이동하여 스프링 프로젝트 생성
    - [https://start.spring.io](https://start.spring.io) - springboot 기반으로 스프링 프로젝트를 만들어주는 사이트
    
    - maven, gradle이란 - 필요한 라이브러리를 떙겨서 오고, 빌드하는 라이프사이클까지 관리해주는 툴
        - 과거에는 maven을 많이 썼지만 요즘에는 gradle을 더 많이 쓰는 추세.
    - Spring Boot 버전 - SNAPSHOT은 아직 만들고 있는 버전, M1 등은 아직 정식 버전이 아닌 것.
    - Project Metadata - group에는 보통 기업 도메인명을 적음. artifact는 빌드되어 나올 결과물.(프로젝트명같은 것)
    - Dependencies - 스프링 부트 기반으로 프로젝트를 시작하는데, 어떤 라이브러리를 사용할지. spring web(웹 프로젝트이기 때문에), thymeleaf(html을 만들어주는 템플릿 엔진) 추가.
    - GENERATE 버튼 클릭 → 다운받음
- 인텔리제이에서 방금 만든 프로젝트의 `build.gradle` open
- 프로젝트의 구조

<img width="350" alt="스크린샷 2023-03-29 오전 1 05 29" src="https://user-images.githubusercontent.com/81567790/228515225-18a4542c-cc7d-4d19-ac26-119e4c06ea10.png">

- `.idea` - 인텔리제이가 사용하는 설정 파일
- `gradle` - gradle을 쓰는 폴더
- `src` - 요즘에는 main과 test라는 폴더를 나누는 것이 표준화되어 있음.
    - main - java, resources가 있고 java 밑에 실제 패키지와 소스 파일이 있음. resources 에는 자바 코드를 제외한 xml, properties 등 설정 파일들이 있음.
    - test - 테스트 코드들과 관련된 소스들이 들어가 있음. 즉, 요즘 개발 트렌드에서는 test 코드라는 것이 중요하다는 것.
- `build.gradle` - 중요! 깊게 공부하면 좋지만 나중에 해도 좋음. 지금은 버전 설정, 라이브러리 땡겨오는 것 정도만 알면 됨

### build.gradle

```groovy
plugins {
	id 'java'
	id 'org.springframework.boot' version '3.0.5'
	id 'io.spring.dependency-management' version '1.1.0'
}

group = 'hello'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '17'

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

tasks.named('test') {
	useJUnitPlatform()
}
```

- `repositories` - 아래의 라이브러리들을 다운받아오는 곳이 필요 → 이 곳이 mavenCentral이라는 공개된 사이트임.
- `dependencies` - 프로젝트 생성 때 선택했던 두 가지 dependencies. 요즘에는 test library가 자동으로 들어가는데, JUnit5 라는 라이브러리가 기본적으로 들어감.

### 실행해보기

- HelloSpringApplication의 main 메소드를 실행하면 됨.

- 로그에 `tomcat started on port(s): 8080` - localhost:8080에 접속해보기
    - 아무것도 없기 때문에 다음과 같이 에러 페이지(`Whitelabel Error Page`)가 나면 성공
    
- `main` 메소드를 실행하면 `run()`으로 인해 그 안의 `HelloSpringApplication.class`를 실행함(SpringBoot Application) → 안에 tomcat이라는 웹 서버가 내장되어 있음. 자체적으로 띄우면서 springboot가 같이 올라옴.

```java
@SpringBootApplication
public class HelloSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloSpringApplication.class, args);
	}
}
```

- Preferences 에서 Build and run using과 Run test using 을 IntelliJ로 변경하기

## 라이브러리 살펴보기

- Gradle과 같은 빌드 툴은 의존관계를 관리해줌
    - spring-boot-starter-web을 implement한다면, 이 라이브러리가 필요로 하는 다른 라이브러리들을 알아서 가져옴. (즉, spring-boot-starter-web과 의존 관계에 있는 다른 라이브러리들을 땡겨옴)
    - 오른쪽 사이드바의 Gradle에서 불러온 라이브러리의 의존성들 확인 가능
        
        ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3e974be0-45bd-408b-a3b4-400173e5fbc2/Untitled.png)
        
    - slf4j와 logback - slf4j는 인터페이스, 실제 로그를 어떻게 출력할지는 logback을 사용
    
    - test와 관련된 라이브러리들 - JUnit이라는 라이브러리를 대부분 사용함. 5로 넘어가는 추세임(강의 기준)
    - spring-test - 스프링과 통하면서 테스트할 수 있도록 도와주는 라이브러리

### 스프링 부트 라이브러리

- spring-boot-starter-web
    - spring-boot-starter-tomcat: 톰캣(웹서버)
    - spring-webmvc: 스프링 웹 MVC
- spring-boot-starter-thymeleaf: 타임리프 템플릿 엔진
- spring-boot-starter(공통): 스프링부트 + 스프링 코어 + 로깅
    - spring-boot
        - spring-core
    - spring-boot-starter-logging
        - logback, slf4j

### 테스트 라이브러리

- spring-boot-starter-test
    - junit: 테스트 프레임워크 (junit5 사용하는 추세)
    - mockito: 목 라이브러리
    - assertj: 테스트 코드를 좀 더 편하게 작성하게 도와주는 라이브러리
    - spring-test: 스프링 통합 테스트 지원

## View 환경설정

### Welcome Page 만들기

- 스프링 부트는 resources/static/index.html 을 넣어두면 이 페이지를 웰컴 페이지로 해줌.

```html
<!DOCTYPE HTML>
<html>
<head>
    <title>Hello</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<body>
Hello
<a href="hello">hello</a>
</body>
</html>
```

- 스프링부트는 스프링 생태계 자체를 감싸서 편리하게 사용하도록 도와주는데, 스프링은 자바 엔터프라이즈 웹 어플리케이션 개발과 관련된 전반의 생태계를 다 제공하기 때문에 양이 어마어마함 → 필요한 내용은 그때그때 찾으면서 해야 함. 찾는 능력이 중요 [docs.spring.io/spring-boot](http://docs.spring.io/spring-boot)

### thymeleaf 템플릿 엔진

- 템플릿 엔진을 쓰면 원하는 대로 (루프를 넣는 등..) 할 수 있음
- `@Controller` - 웹 애플리케이션에서 첫번째 진입점이 Controller임.
- controller 패키지 만들고 `HelloController.java`
    - `@GetMapping(”hello”)` - 웹에서 /hello 를 들어오면 이 메소드를 호출해줌
    
    ```java
    package hello.hellospring.controller;
    
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    
    @Controller
    public class HelloController {
        @GetMapping("hello")
        public String hello(Model model){
    				model.addAttribute("data", "hello");
            return "hello";
        }
    }
    ```
    
- main/resources/templates에서 hello.html 만들기
    - `th`는 thymeleaf를 사용한 것.

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Hello</title>
</head>
<body>
<p th:text="'안녕하세요. ' +  ${data}">안녕하세요 손님.</p>
</body>
</html>
```

### 동작 환경 과정

<img width="467" alt="스크린샷 2023-03-29 오전 2 11 07" src="https://user-images.githubusercontent.com/81567790/228515728-c50339fb-ddbe-4514-8131-6ddc9d4aa9cf.png">

웹 브라우저에서 [localhost:8080/hello를](http://localhost:8080/hello를) 가면, 스프링 부트에 내장되어 있는 톰캣 서버에서 이를 받음 → 스프링에 의해 helloController에 있는 hello() 메소드가 실행됨 → 이때 스프링이 Model이라는 것을 만들어서 넘겨주고, 이 model에 key인 data, 값인 hello!!를 attribute 로 넣음 → `return “hello”;` 는 `**resources/templates**`에 있는 `hello.html`을 찾아서 실행시키라는 것 → 이 파일을 thymeleaf 템플릿 엔진이 처리하고, 이런 과정을 동작하도록 스프링 부트가 세팅을 기본적으로 해둠.

- 컨트롤러에서 리턴 값으로 문자를 반환하면 viewResolver가 화면을 찾아서 처리해줌
    - 스프링 부트 템플릿엔진은 기본적으로 viewName 매핑이 됨.
    - `resources/templates/` + {ViewName} + `.html`

** spring-boot-devtools 라이브러리를 추가하면 ‘html’파일을 컴파일만 해주면 서버 재시작 없이 view 파일 변경이 가능함

## 빌드하고 실행하기

- 콘솔로 이동 → `./gradlew build` → `cd build/libs` → `java -jar hello-spring-0.0.1-SNAPSHOT.jar` → 실행 확인

<img width="858" alt="스크린샷 2023-03-29 오전 2 24 56" src="https://user-images.githubusercontent.com/81567790/228516290-41b431d9-e08c-48ad-9c47-97087e5a3b3f.png">

- 서버 배포할 때는 이 파일을 복사해서 서버에 넣어두고 `java -jar` 을 통해 실행시키면 됨.

# 섹션 2. 스프링 웹 개발 기초

- 웹을 개발하는 데에는 세 가지 방법이 있음
    - 정적 컨텐츠 - welcome page 같이 파일 그대로를 웹 브라우저에 띄우는 것
    - MVC와 템플릿 엔진 - 가장 많이 하는 방식. JSP, PHP 같은 템플릿 엔진들. 서버에서 프로그래밍해서 Html을 동적으로 바꾸는 것. 이를 위해 controller, model, 템플릿 엔진 화면 등을 Model View Controller 라고 함
        - 얘는 서버에서 HTML을 변형해서 브라우저에 내려놔주는 방식
    - API - 클라이언트와 개발할 때, JSON이라는 데이터 포맷으로 클라이언트에 보내줌. 이 방식이 API.
        - 뷰, 리액트 를 사용할 때 API로 데이터 내려주면 화면은 클라이언트가 알아서.
        - 서버끼리 통신할 때 (HTML 필요 없으므로)

## 정적 컨텐츠

- 스프링 부트는 정적 컨텐츠 기능을 제공함.
    - src/resources/static 아래에 두는 HTML 파일들.
    - hello-static.html의 경우 [localhost:8080/hello-static](http://localhost:8080/hello-static) 으로 접속하면 됨
        
- 정적 컨텐츠 작동 방식
    - 웹 브라우저에서 해당 웹 페이지에 접속 → 내장 톰캣 서버가 요청을 받고, 스프링 부트에게 요청 사항을 넘김 → 스프링은 컨트롤러 쪽에 hello-static이 있는지 찾아봄(우선순위) → 매핑된 컨트롤러가 없으므로 내부에 있는 resources:static/hello-static.html을 찾아서 띄워줌

## MVC와 템플릿 엔진

- MVC: Model, View, Controller
    - View는 화면을 그리는데 모든 역량을 집중해야 함
    - Controller, Model 은 내부적인 것을 처리하는데 집중해야 함.
    
    ⇒ 세 가지로 쪼개놔야 함. 
    
- HelloController에 helloMVC 만들기
    
    ```html
    <!-- hello-template.html -->
    <!DOCTYPE html>
    <html xmlns:th="http://www.thymeleaf.org">
    <body>
    <p th:text="'hello' + ${name}">hello! empty</p>
    </body>
    </html>
    ```
    
    ```java
    @GetMapping("hello-mvc")  // 외부에서 파라미터 받기
        public String helloMvc(@RequestParam("name") String name, Model model){
            model.addAttribute("name", name);
            return "hello-template";
        }
    ```
    
    - 그냥 [localhost:8080/hello-mvc](http://localhost:8080/hello-mvc) 접속하면 에러남 → RequestParam의 required 속성이 true가 디폴트이기 때문
    - localhost:8080/hello-mvc?name=spring! 과 같이 접속해야 함.
        
        ⇒ Controller에서 name 값이 “spring!!” 으로 바뀜 → 모델에 담김 → 템플릿으로 넘어가서 ${name}은 모델의 키 값이 name인 것에서 값을 꺼내기 때문에 여기에 “spring!!”이 들어가게 됨.
        
- MVC, 템플릿 엔진 작동 방식
    - 웹 브라우저에서 접속 → 내장 톰캣 서버는 먼저 hello-mvc라는 것이 왔다고 스프링에게 알려줌 → 스프링은 helloController에 저 메소드와 매핑되어 있음을 확인하고 그 메소드 호출 → return문과 model을 스프링에 넘겨줌 → viewResolver가 동작. 뷰를 찾아주고 템플릿 엔진 연결해줌. → viewResolver가 템플릿 위치에 있는 파일 중 똑같은 파일을 찾고, thymeleaf 에게 처리 요청하며 넘김 → **템플릿 엔진이 렌더링해서 변환한 html을 웹브라우저에 반환** (⇒ 정적 컨텐츠와 차이점!)

## API

### @ResponseBody 문자 반환

- `@ResponseBody` 에는 http의 바디 부분에 이 데이터를 직접 넣겠다는 의미임.
    
    ⇒ 데이터 그대로를 웹 브라우저에 띄워줌.
    

```java
@GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam("name") String name){
        return "hello" + name;
    }
```

### @ResponseBody 객체 반환

- 아래와 같이 객체를 반환하는 것을 api라고 부르는 것..

```java
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
```


- JSON 형식으로 되어 있음 (`key:value`)

### @ResponseBody 사용 원리


- 웹 브라우저에서 접속 → 톰캣 서버에서 `hello-api`가 왔다고 스프링에 알려줌 → 스프링은 hello-api가 있음을 확인, `@ResponseBody` 어노테이션이 붙어있음을 확인 → http 응답에 그대로 이 데이터를 넣도록 동작함. `HttpMessageConverter`가 동작하는데, 이때 문자이면 `StringConverter`가, 객체이면 `JsonConverter`가 동작. 디폴트로 JSON 방식으로 데이터를 만들어서 http응답에 반환함.(기본 정책) → 나를 요청한 서버나 웹 브라우저에 응답.
- HTTP의 BODY에 문자 내용 직접 반환 → `viewResolver`대신 `HttpMessageConverter`가 동작 → 기본 문자는 `StringHttpMessageConverter`, 기본 객체는 `MappinJackson2HttpMessageConverter`가 처리 → byte 처리 등등 기타 여러 `HttpMessageConverter`가 기본으로 등록되어 있음.


**노션 링크 첨부합니다!**
[2주차 Spring Basic Study] (https://www.notion.so/2-0-1-2-9b8f8ce8e1664df599c44d13d6f15c7b?pvs=4)
