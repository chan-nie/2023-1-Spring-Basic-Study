# week7.md

<스프링 기본 강의>

[스프링의 역사]

과거: EJB 

현재: Spring → sping boot ( 내장 서버 생성됨 )

[스프링 생태계]

필수 : spring boot(엔진역할) , spring framework

선택: 스프링 데이터 (crud 를 편리하게 사용하게 도와줌) ; spring jpa

: 스프링 배치처리 ; 여러개의 데이터를 빠르게 업데이트 하는데 도움을 줌- 

- 스프링 프레임워크 ← spring boot 이 지원함
    - 핵심기술
    - 웹기술
    - 데이터 접근 기루
- spring boot 의 장점
    - tomcat 서버가 설치될때부터 이미 존재 ( tomcat 설치 불필요 )
    - 외부 메이저 library + version 을 알아서 체크해서 저장되게 해줌
    - 설정이 쉽다는 것.
- spring boot
    - 기능만 제공
    - 단독으로 쓰이는 게 아니라, framework 를 도와주는 프로젝트임
- spring 상호교환 가능한 단어들
    - DI 컨테이너
    - spring framework
- spring 을 만든 이유
    - 객체 지향 개발에 큰 기여를 함
    

---

<좋은 프로그램이란?>

- 객체 지향 프로그래밍
    - 객체들끼리 메세지를 주고 받고 협력 가능
    - 변경 용이 ( 어떤 컴포넌트를 갈아끼우기만 하면 됨 ⇒ “다형성” )
        - 역할과 구현을 분리
        - 오버라이딩
