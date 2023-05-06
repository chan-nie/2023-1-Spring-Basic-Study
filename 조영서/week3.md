# Section3

## **비즈니스 요구사항 정리**

- 데이터 : 회원 ID, 이름
- 기능 : 회원 등록, 조회
- 아직 데이터 저장소가 선정되지 않았다는 조건
- 일반적인 웹 애플리케이션 계층 구조

![https://blog.kakaocdn.net/dn/bwJ67i/btr7R4UluID/Sq9fxk0PpJGBYaGAE1YmQk/img.png](https://blog.kakaocdn.net/dn/bwJ67i/btr7R4UluID/Sq9fxk0PpJGBYaGAE1YmQk/img.png)

- 컨트롤러 : 웹 MVC의 컨트롤러 역할
- 서비스 : 핵심 비즈니스 로직 구현
- 리포지토리 : 데이터베이스에 접근, 도메인 객체를 DB에 저장하고 관리
- 도메인 : 비즈니스 도메인 객체
- 클래스 의존관리

![https://blog.kakaocdn.net/dn/MVu0B/btr7C0M1RE7/Dz5Yt4ogImS7O1eKw5zBoK/img.png](https://blog.kakaocdn.net/dn/MVu0B/btr7C0M1RE7/Dz5Yt4ogImS7O1eKw5zBoK/img.png)

- MemberRepository : 회원을 저장하는 곳, 인터페이스로 설계(아직 데이터 저장소가 선정되지 않아서)
- Memory MemberRepository : MemberRepository의 구현체, 단순 메모리로 넣다 뺐다 할 수 있게, 나중에 바꾸기 위해 인터페이스가 필요한 것

## **회원 도메인과 리포지토리 만들기**

1. src/main/java/hello/hellospring/domain/Member.java 생성

```
public class Member {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
```

2. src/main/java/hello/hellospring/repository/MemberRepository.java 생성 (인터페이스)

```
public interface MemberRepository {

    Member save(Member member);//회원 저장 시 저장된 회원이 반환Optional<Member> findById(Long id);//ID로 회원 찾기Optional<Member> findByName(String name);//Optional로 감싸서 반환List<Member> findAll();//모든 회원 반환

}
```

3. src/main/java/hello/hellospring/repository/MemoryMemberRepository.java 생성

```
public class MemoryMemberRepository implements MemberRepository {
    private static Map<Long, Member> store = new HashMap<>();//저장private static long sequence = 0L;//key 값 생성@Override
    public Member save(Member member) {
        member.setId(++sequence);//ID는 시스템이 정해주는 것
        store.put(member.getId(), member);
        return member;
    }
    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));//store에서 꺼내기
    }
    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());//member 모두 반환
    }
    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()//루프 돌기
                .filter(member -> member.getName().equals(name))//같은 경우에만 필터링
                .findAny();//반환
    }
    public void clearStore() {
        store.clear();
    }
}
```

- null 반환 가능성이 있으면 Optional 함수 사용

## **회원 리포지토리 테스트 케이스 작성**

- 테스트 케이스를 작성하는 이유
    - main 메소드나 컨트롤러를 통해 기능 실행 시 반복 실행의 어려움과 시간이 다소 소요된다는 단점 발생
    - Java의 JUnit이라는 프레임워크로 테스트를 실행하여 위와 같은 문제 해결

1. src/test/java/hello/hellospring/repository/MemoryMemberRepositoryTest.java 작성

- 테스트 클래스명은 '테스트 하고자 하는 파일 + Test'가 관례
- @Test : 실행 가능해짐
- main 메소드 작성과 유사
- 테스트는 순서 상관 없이 따로 동작하도록 설계 해야 함
    - 순서 의존 X
    - 하나의 테스트 이후에는 데이터 clear 필요

```
public class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();//MemoryMemberRepository만 테스트할거니까@AfterEach
    public void afterEach() {//테스트가 끝날 때마다 데이터 지우기
        repository.clearStore();
    }
    @Test//실행 가능해짐public void save() {
//given
        Member member = new Member();
        member.setName("spring");//이름세팅//when
        repository.save(member);//리포지토리에 member를 save//then
        Member result = repository.findById(member.getId()).get();//저장하면 Id 세팅되었으니까, Optional의 반환 타입은 get으로 꺼낼 수 있음
        assertThat(result).isEqualTo(member);
//Assertions.assertEquals(member, result);//Assertions.assertThat(member).isEqualsTo(result);
    }
    @Test
    public void findByName() {
//given
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);
        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);
//when
        Member result = repository.findByName("spring1").get();//이름으로 찾기//then
        assertThat(result).isEqualTo(member1);
    }
    @Test
    public void findAll() {
//given
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);
        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);
//when
        List<Member> result = repository.findAll();
//then
        assertThat(result.size()).isEqualTo(2);
    }

}
```

## **회원 서비스 개발**

- 서비스 클래스는 비즈니스에 가까운 용어를 사용해야 함 => 협업을 위해

1. src/main/java/hello/hellospring/service/MemberService.java 생성

```
public class MemberService {

    private final MemberRepository memberRepository = new
            MemoryMemberRepository();
/**
     * 회원가입
     */public Long join(Member member) {
        validateDuplicateMember(member);//중복 회원 검증
        memberRepository.save(member);//저장return member.getId();//Id 반환 (임의)
    }
    private void validateDuplicateMember(Member member) {//같은 이름이 있는 중복 회원 X
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {//null이 아닌 값이 있으면throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
/**
     * 전체 회원 조회
     */public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

}
```

## **회원 서비스 테스트**

1. Test 생성 단축기 : Ctrl + Shirt + t

- 테스트 코드의 메소드명은 한글로 해도 무방, 전체 코드에 포함되지 않음

![https://blog.kakaocdn.net/dn/BG3eG/btr7TlwVTSF/wokQ6ARnhmVz35hk1lseGK/img.png](https://blog.kakaocdn.net/dn/BG3eG/btr7TlwVTSF/wokQ6ARnhmVz35hk1lseGK/img.png)

2. src/main/java/hello/hellospring/service/MemberService.java 코드 수정

```
public class MemberService {

    private final MemberRepository memberRepository;
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

}
```

3. src/test/java/hello/hellospring/service/MemberServiceTest.java 생성

```
class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }
    @AfterEach
    public void afterEach() {//데이터 비우기
        memberRepository.clearStore();
    }
    @Test
    public void 회원가입() throws Exception {
//Given
        Member member = new Member();
        member.setName("hello");
//When
        Long saveId = memberService.join(member);//Id가 튀어나오겠지//Then
        Member findMember = memberRepository.findById(saveId).get();
        assertEquals(member.getName(), findMember.getName());
    }
    @Test
    public void 중복_회원_예외() throws Exception {//예외 중요//Given
        Member member1 = new Member();
        member1.setName("spring");
        Member member2 = new Member();
        member2.setName("spring");
//When
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));//예외가 발생해야 한다.
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}
```

- given : 주어진 것
- when : 실행했을 때
- then : 결과로 나와야 하는 것
- Test는 예외 플로우도 중요 => 예외가 나오는 것을 봐야 함

# Section 4

## 컴포넌트 스캔과 자동 의존관계 설정

- 화면을 붙이려면 Controller와 View template이 필요
- Member Controller가 Member Service를 통해 회원가입하고 조회할 수 있어야 함 ⇒ 의존 관계가 있다 표현 == 멤버 컨트롤러가 멤버 서비스를 의존한다

1. /src/main/java/hello/hellospring/controller/MemberController.java 생성
- MemberService를 Spring Contaioner로부터 받아 쓰도록 ⇒ 새로 생성할 필요 X, 하나만 가져와서 같이 쓰는 것이 나음
- 결론 : 스프링 컨테이너에 하나만 등록하여 가져다 쓰기!

```java
@Controller
public class MemberController {
    private final MemberService memberService; //Memberservice 가져와 쓰기
    @Autowired //Spring Container의 MemberService와 연결
    public MemberController(MemberService memberService) { //생성자 호출
        this.memberService = memberService;
    }
}
```

1. MemberService.java에 @Service 추가

```java
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
...
}
```

- **Spring이 Spring Container에 MemberService를 등록하게 함**

1. MemoryMemberRepository.java에 @Repository 추가

```java
@Repository
public class MemoryMemberRepository implements MemberRepository { ... }
```

- Controller을 통해 외부 요청을 받고 ⇒ Service에서 비즈니스 로직을 만들고 ⇒ Repository에서 데이터를 저장 = 정형화된 패턴
- Spring이 위 Controller, Service, Repository를 가지고 올라 옴 ⇒ @Autowired을 통해 연결 == Dependency Injection (의존관계 주입)

- 스프링 빈 등록 이미지

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/f22507cf-ec64-4790-842a-8d4d444c4bbd/Untitled.png)

- @Autowired 로 memberController가 memberService를 쓸 수 있게 함
- @Autowired 로 memberSeriv가 memberRepository를 쓸 수 있게 함

- 스프링 빈을 등록하는 2가지 방법
    - 컴포넌트 스캔과 자동 의존관계 설정 ⇒ 지금 한 것!
    - 자바 코드로 직접 스프링 빈 등록하기

- 컴포넌트 스캔 원리
    - @Component 이 있으면 스프링 빈으로 자동 등록
    - @Controller 가 스프링 빈으로 자동 등록된 이유도 컴포넌트 스캔이기 때문

- @Component 를 포함하는 아래 어노테이션도 스프링 빈으로 자동 등록
    - @Controller
    - @Service
    - @Repository

- Component 스캔 대상 X
    - 우리가 실행하는 파일의 패키지의 하위 패키지들은 자동으로 스프링 빈으로 등록
    - 하위 패키지가 동일하거나 아닌 것들은 기본적으로 스프링 빈으로 Componenet 스캔 X

- 참고
    - 스프링은 스프링 컨테이너에 스프링 빈을 등록할 때 기본으로 싱글톤으로 등록
    - 같은 스프링 빈이면 같은 인스턴스
    - 추가 설정은 가능

## 자바코드로 직접 스프링 빈 등록하기

- @Service, @Repository, @Autowired 제거 후 진행

1. [SpringConfig.java](http://SpringConfig.java) 생성

```java
@Configuration
public class SpringConfig {
    @Bean //스프링 빈에 등록하기
    public MemberService memberService() {
        return new MemberService(memberRepository()); //스프링 빈에 등록
    }
    @Bean //스프링 빈에 등록하기
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}
```

- memberRepository와 memberService 둘 다 스프링 빈에 등록 ⇒ 스프링 빈에 등록되어 있는 memberRepository를 memberService에 넣어줌 ⇒ 위 ‘스프링 빈 등록 이미지’ 완성!

- DI(Dependency Injection) : 필드 주입, setter 주입, 생성자 주입
    - 의존관계가 실행중에 동적으로 변하는 경우는 아예 없으므로 생성자 주입을 권장

- 실무에서는 주로 정형화된 컨트롤러, 서비스, 리포지토리 같은 코드는 컴포넌트 스캔을 사용, 정형화 되지 않거나 상황에 따라 구현 클래스를 변경해야 하면 설정을 통해 스프링 빈으로 등록

- @Autowired 를 통한 DI는 helloController , memberService 등과 같이 스프링이 관리하는 객체에서만 동작, 스프링 빈으로 등록하지 않고 내가 직접 생성한 객체에서는 동작 X



# Section 4 - Lab

## 스프링 의존성 주입 3가지 방법

### 의존성 주입 = Dependency Injection = DI

- ~~프로그램 디자인이 결합도를 느슨하게 되도록하고 의존관계 역전 원칙과 단일 책임 원칙을 따르도록 클라이언트의 생성에 대한 의존성을 클라이언트의 행위로 분리하는 것 by 위키피디아~~
- 의존 관계란?
    - A가 B를 의존한다 = 의존대상 B가 변하면 그것이 A에 영향을 미친다
    - 다양하게 의존 관계를 맺으려면 인터페이스로 추상화 해야 함 ⇒ 실제 구현 클래스와의 관계는 느슨해지고 결합도가 낮아짐
- 의존 관계 주입이란?
    - 의존관계를 외부에서 결정하고 주입하는 것
    - 클래스 변수를 결정하는 방법 = DI를 구현하는 방법
    - 런타인 시점의 의존관계를 외부에서 주입하여 DI 구현 완성
- DI 장점
    - 의존성/종속성 ↓ : 변화가 있더라도 수정할 일이 적다
    - 재사용성 ↑ : 다른 클래스에서도 사용 가능
    - 테스트 용이
    - 유연한 코드 by 낮은 결합도
- 참고 : [의존관계 주입(Dependency Injection) 쉽게 이해하기 (techcourse.co.kr)](https://tecoble.techcourse.co.kr/post/2021-04-27-dependency-injection/)

### 방법

1. Field(필드) 주입
2. Setter(수정자) 주입
3. Constructor(생성자) 주입

### Field 주입

- 멤버 객체에 @Autowired 추가
- Spring 런타임시 ⇒ 타입이 같은 객체를 검색해 사용 or 없으면 생성 후 등록
- 코드가 간결하나 외부에서 변경이 어려움
- 프레임 워크에 의존적
- 객체지향적으로 좋지 않음

### Setter 주입

- setter 메소드에 @Autowired 선언 ⇒ setter 메소드의 파라미터에 해당하는 객체를 빈에서 불러옴
- 변경 용이

### Constructor 주입

- 빈이 만들어지는 시점에 모든 의존 관계를 빈에서 가져와야 함
- 스프링 프레임워크에서 권장
    - 순환 참조 방지
    - 변경의 가능성 배제, 불변성 보장
    - 테스트 용이
