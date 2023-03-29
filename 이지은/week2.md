**01. 프로젝트 환경설정**
# 01-1. 프로젝트 생성

·스프링 프로젝트 생성 
 : spring stater 사이트(https://start.spring.io/) 사용 -> 
 Project : Gadle-Groovy, Language : Java, Spring Boot : 2.7.10(SNAPSHOT．M! : 미정식 버전, 3.0이상 사용시 JAVA 17이상을 사용해야함 -> java 11 사용시 오류 발생)
Dependencies : Spring Web & Thymeleaf

=> InteliJ에서 open

·InteliJ
- main 메소드 run -> localhost:8080

- InteliJ Gradle 대신에 자바 직접 실행 
 : 실행속도가 더 빨라짐. File -> Setting -> Build and run Using, Run tests Using을 Gradle에서 InteliJ IDEA로 변경

# 01-2. 라이브러리 살펴보기
 : Gradle은 의존관계가 있는 라이브러리를 함께 다운로드 함
 
 "스프링 부트 라이브러리"
  - spring-boot-starter-web
 
    - spring-boot-starter-tomcat: 톰캣(웹서버)
 
    - spring-webmvc: 스프링 웹 mvc
 
  - spring-boot-starter-thymeleaf: 타임리프 템플릿 엔진(View)
 
  - spring-boot-starter(공통): 스프링부트 + 스프링 코어 + 로깅
 
    - spring-boot
 
       - spring-core
 
    spring-boot-starter-logging
 
       - logback, slf4j
 
 "테스트 라이브러리"
  - spring-boot-starter-test
 
       - junit : 테스트 프레임워크
 
       - mockito : 목 라이브러리
 
       - assertj : 테스트 코드를 좀 더 편하게 작성하게 도와주는 라이브러리
 
       - spring-test : 스프링 통합 테스트 지원

# 01-03. View 환경설정

· Welcome 페이지 만들기
: main -> resources -> static -> index.html (static/index.html)
=> localhost:8080에 welcome page가 생성됨

· Controller
: main -> java -> hello.hellospring -> controller -> HelloController(javaclass) 
· thymeleaf 템플릿 엔진
: main -> resources -> templates -> hello.html 
=> controller에서 return hello가 실행되면 templates 아래에서 hello.html을 찾아 실행.
 controller의 data 값이 hello.html의 data에 치환되어 화면에 나타남. (resources:templates/ +{ViewName}_ .html)
 (localhost:8080/hello)

 # 01-04. 빌드하고 실행하기

· cmd에서 빌드하기
: cd를 이용하여 hello-spring 폴더로 이동 -> gradlew build를 통해 빌드
=> BUILD SUCCESSFUL 
cd build로 폴더를 이동하여 dir로 libs 파일 확인 가능
cd libs로 폴더 이동 -> .jar 파일 확인 가능
-> java -jar 파일명 

 - build 폴더를 없애고 싶을 때 : cd ..으로 build의 상위 폴더로 이동하여 gradlew clean

**02. 스프링 웹 개발 기초**
# 02-1. 정적 컨텐츠

 - 스프링 부트는 기본적으로 정적 컨텐츠 기능을 제공
 : resources -> static -> hello-static.html(localhost:8080/hello-static.html)
 (컨트롤러에서 hello-static을 먼저 찾아보고 resources로 이동)

 # 02-2. MVC와 템플릿 엔진

 - MVC : Model, View, Controller
· HelloController -> @GetMapping("hello-mvc)
: @RequestParam("name") -> localhost:8080/hello-mvc => 오류
파라미터 값('name')을 전달해줘야 함 -> localhost:8080/hello-mvc?name=
(templates -> hello-template.html)

# 02-3. API

- @ResponseBody : HTTP의 BODY에 문자 내용을 직접 반환
-> @ResponseBody를 사용하고, 객체를 반환하면 객체가 JSON으로 변환됨(localhost:8080/hello-api?name=spring)
- 사용 원리:
@ResponseBody 를 사용 
 - HTTP의 BODY에 문자 내용을 직접 반환 
 - viewResolver 대신에 HttpMessageConverter 가 동작 
 - 기본 문자처리: StringHttpMessageConverter 
 - 기본 객체처리: MappingJackson2HttpMessageConverter 
 - byte 처리 등등 기타 여러 HttpMessageConverter가 기본으로 등록되어 있음