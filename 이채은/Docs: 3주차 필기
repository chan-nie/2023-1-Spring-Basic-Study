[회원 관리 예제 만들기]

회원 도메인, ⇒ 회원 리포지토리 객체

테스트 샘플 만들기 ( j.unit 이라는 프레임워크로 만들것임 )

---

1. 비즈니스 요구사항 ⇒ 데이터, 기능 ( 데이터 베이스가 결정되지 않은 상황 )
2. 서비스 : 회원은 중복가입이 안된다 등
3. 도메인

→ 계층형 구조

---

<회원 관리 예제 만들기>

1. Domain 폴더 내부에 Member class 만들어줌
2. Repository 폴더 내부에 MemberRepository인터페이스
    
    ( 고객의 정보를 담아두는 곳)
    
    그리고, MemoryMemberRepository class 파일을 만든다.
    
3. 만든 프로그램이 잘 실행되는지 확인해보기 위헤 test 데이터를 사용해서 test 를 진행한다.

  ( test 폴더에 repository 패기지 내의 M.M.R.Test class 파일 만들기)

- 과거: 자바의 메인 메서드 or 외부 애플리케이션의 컨트롤러 사용
- 현재: 자바에서 JUnit 이라는 test 코드를 만들어서 내부적으로 실행
    - JUnit 으로 테스트를 할때는 주로 테스트하려는것의이름.Test 라고 class 이름 짓는 게 일반적.

---

<테스트 전 프로그램을 만드는 과정에서 코드 뜯어보기>

1. 

```java
@Override*/** 첫번째 overide: ID 값 저장받아옴 by save.,, ID 는 시스템이 정한 값 */*
public Member save(Member member) {   
 member.setId(++*sequence*);    
*store*.put(member.getId(), member);   
 return member;
```

2.

```java
@Override*/**store 값이 null 일때 요즘은 optional 로 감싸서 반환을 해줌.*/*
public Optional<Member> findById(Long id) {
return Optional.*ofNullable*(*store*.get(id));}
```

3.

```java
Override*/**get 으로 받은 이름이 parameter 과 같은 값인지 체크해주는 부분*/*
public Optional<Member> findByName(String name) {    
return *store*.values().stream()            
.filter(member -> member.getName().equals(name))            
.findAny();    */**같은 게 찾아지면 반환 , 없으면 optional 사용함*/*}
```

<테스트 과정 주요 코드 뜯어보기>

- @Test  쓰고, jupiter api 를 import 받는다.
- Optional 에서 값을 꺼낼때는 get 을 사용한다
    - ex) `repository.findById(member.getId()).**get();**`
- new 에서 꺼낸거랑 D.B 에서 꺼낸거랑 같으면 성공
    
    → assertions 를 사용하면 org.assert 사용 ⇒ assertThat()
    
- 단축키: shift+enter 사용하면 구조 같은 두개에서 한개의 이름 rename 가능
- 모든 테스트는 순서와 상관없이 다 따로 실행이 되어야 함. (순서에 의존적으로 설계하면 안됨 )
    
    → test 가 하나 끝나고 나면 data 를 clear 시켜줘야함
    
    `public void clearStore(){    *store*.clear();` }
    
    `@AfterEach  */** 콜벡 메서드, 각각이 끝나고 다시 돌아오게 함 */*public void afterEach(){    repository.clearStore();}`
    

---

<test>  ⇒ 중요하니까 공부 꼼꼼히 하기 

메모리(구현 클래스) 작성 후 테스트
vs
테스트 작성 후 멤버 메모리 작성한다면? (순서 뒤집는다면? )
=> 테스트 주도 개발(TDD)

---

<내가 초반에 오류가 있어서 애먹었던 부분>

처음에 분명 domain에 Member 이라는 이름의 java class 를 만들었는데,
나중에 Member 사용하려고 보니까, 이게 인식이 안되는 거임.
그래서 일단 만들어놓았던 거 전부 초기화하고, 내가 직접 코드 타이밍 진행
-> 그 과정에서 alt+enter 을 통해 내가 계속 필요한 패키지들을 import 해줘야 하고, 여기서 쓰인 클래스가 어디의 어떤 클래스 인지를 지정해줘야
오류가 나지 않는다는 것을 알았음.

---

<회원 서비스 개발>

1. MemberService class 만들기 
- main 에 service package 만들고 그 안에 Memberservice class 만듬
- optional 에 여러 객체가 있음 → ex. Illegal~ method 사용
- optional 을 바로 받아서 반환하지 않고 ( result 같은거 필요없음 ) 바로 .ifPresnt 사용해줌
- 단축키 ctrl+alt+m ⇒ extract method
- service class 에서 사용되는 메서드들의 이름은 좀 비즈니스스러운 이름지음
1. MemberServiceTest 해주기
- Test 하려는 class 에서 ctrl+shift+T 누르면 test 자동으로 생성됨
- given(주어짐) when(실행했을때) then(결과)
- test 폴더에서 memberRepository 에 접근해야하는데 어려움
- test 는 예외처리가 중요함
- 예외를 잡으려면 try-catch 를 사용하는 방법 있음
- @BeforeEach 구현과정에서 DI 사용 볼 수 있음

---

스프링 빈과 의존관계

1. 컴포넌트 스캔과 자동 의존관계 설정 ⇒ annotation 사용
- controller 객체를 생성해서 spring bin 이 관리 된다 . ( 스프링 컨테이너가 관리를 한다 )
- new 해버리면 멤버 서비스를 그냥 가져다가 써버릴 수 있음.
- spring container 에 따로 등록해서 쓰기⇒ @ service
    - 하나만 등록해서 공유
    
    → 생성자 사용해서 구현 @Autowired (=스피링 컨테이너에 있는 멤버 서비스에 연결을 해줌)
    
- controller ( 외부 받고 ) service 연결 ( autowired 사용 ) : DI (의존관계주입)
- member service 는 member repository 가 필요
- (그림 참조 )

---

2.자바코드로 직접 스프링 빈 등록 

1. springconfig class 만들기
2. @Configuration 읽기
- 과거 : XML 사용했다
- 생성자 주입( 생성자를 통해서 controller 에 들어오게 됨) ← **권장**
- 필드주입 ( 별로 선호되지 않음 )
- Setter 주입 ⇒ 누군가가 호출했을때 불릴 수 있게 꼭 public 이여야함.

---

[정리]

spring 빈 만드는 법:

컴포넌트 스캔과 자동 의존관계 설정 ⇒ annotation 사용

자바코드로 직접 스프링 빈 등록
