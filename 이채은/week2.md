# Week2(week2.md)

✏️ 강의 범위: section 0,1,2

## 개발환경 구축

### 프로젝트 환경 다운로드

- intellij 설치
- [spring.io](http://spring.io) 에서 원하는 templete 다운로드 후 hello-spring file 생성
- 다운로드 한 spring boot version 과 맞는 자바 버전 다운로드
    
    → ‘..test’ folder 에 있는 default 파일 내부 자바 버전 체크 가능
    
- java jdk 설치 후, 컴퓨터 속성 변경에서 환경변수 설정(PATH 만들기)

### **Gradle 전체 설정**

- ‘build.gradle’ file  run 해놓고, [http://localhost](http://localhost):8080 검색
    
    → 제대로 설치되었으면, error 발생
    
- 그 이외의 개발환경 설정들 구축
    - grandle 을 통해서 실행하는 default→ intellij 에서 실행하도록 변경
    - 자바 버전 제대로 설정되어있는지 체크 ( window: ctrl+alt+shift+s)

### [spring.io](http://spring.io) 에서 thymeleaf 설치하면서 받았던 templete 과 기본 내장 library 들 확인

- ex. junit , mockito, assertj, spring-test (test library 들임)
- 과거: 웹서버를 직접 서버에 설치하고, 자바코드를 밀어넣는 식 ( 개발환경과 라이브러리를 분리)
- 현재: 소스 라이브러리에 자체 웹서버(tomcat) 을 가지고 있음 ( 개발환경에 라이브러리, 서버가 포함되어있음 ) → 8080 실행 시, 확인가능
- spring boot: spring core 을 끌어쓸 수 있다.
- logging: system.out.pring() 대신 현업에서 많이 사용함. (=log 로 출력)
    
    : logging의 라이브러리 중 logback 을 많이 사용하는 추세임.
    

---

## 스프링 웹 개발 기초

: 크게 3가지 구현 방법이 존재한다.

### (1) 정적 컨텐츠

- 원하는 file 을 web browser 에다가 그대로 가져다가 줌
- 정적 컨텐츠 메커니즘

![01AC07EA-5D62-4C84-BC51-46575E5DC5CB.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e32c67de-aa50-4259-beac-6f40d9272cf4/01AC07EA-5D62-4C84-BC51-46575E5DC5CB.jpeg)

### (2) MVC ( with, templete )

- Model View Controller 의 약자
- 실행시키려는 html 파일에 조금의 변경을 가해서 view 시킬 수 있음
    
    (동적으로 html 을 전달하는 방식)
    
- 핵심은 controller(&model) 과 view 의 기능을 분리해서 개발하는 것.
    - Controller: 내부적인 것을 처리( 시스템적인 ) /controller, model pt.
        
        ex. 
        
        ![C838E186-E59D-4A5F-AA22-2636AA7A530D.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/403ebc08-dbe8-40ff-9356-760465c70d8c/C838E186-E59D-4A5F-AA22-2636AA7A530D.jpeg)
        
    
    - View: 사용자의 화면과 관련됨
        
        ex.  
        
        ![99A478DC-8171-434B-AD88-00D5ED204656.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/fed4d94f-2921-4f41-87ca-4ad133080632/99A478DC-8171-434B-AD88-00D5ED204656.jpeg)
        

- MVC 메커니즘

![3FB05F48-E6A4-4AC0-9E81-2FA085020136.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0a621f44-39b3-4b47-bdd3-c1832361c3d8/3FB05F48-E6A4-4AC0-9E81-2FA085020136.jpeg)

### (3) API

- view 기능은 거의 신경쓰지 않는 방식 ( 서버끼리 통신할 때 주로 사용 )
- data 만을 보내야할 때 유용
- @Responsebody , getter. setter 기능 포함해야함
    - @Responsebody : 사용자가 주는 값을 받아서 서버에 적용하게 함.
    - getter, setter : java 에서의 쓰임처럼, public 이 아닌 것의 property 에 접근 가능하게 함.
- API 메커니즘 (+@Responsebody 이 적용되는 방식)

![9C1257F5-E822-4CD8-B395-BF6DF0334F7F.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/cb6e1108-8559-4b12-b3e6-89c252d883df/9C1257F5-E822-4CD8-B395-BF6DF0334F7F.jpeg)

‼️**결론:** 
  
  랜더링된 html ⇒ MVC , templete 방식 사용

  객체를 반환하는 경우 ⇒ API 방식 사용 (json으로 반환)
