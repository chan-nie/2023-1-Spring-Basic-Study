**03. 회원 관리 예제 - 백엔드 개발**

# 03-1. 비즈니스 요구사항 정리
 - 데이터 : 회원 ID, 이름
 - 기능 : 회원 등록, 조회
 - 아직 데이터 저장소가 선정되지 않음 (가상의 시나리오)
 
· 일반적인 웹 애플리케이션 계층 구조
: 컨트롤러 - 웹 MVC의 컨트롤러 역할 
  서비스 - 핵심 비즈니스 로직 구현 (비즈니스 도메인 객체 사용)
  리포지토리 - 데이터베이스에 접근, 도메인 객체를 DB에 저장하고 관리 
  도메인 - 비즈니스 도메인 객체, 
  ex) 회원, 주문, 쿠폰 등등 주로 데이터베이스에 저장하고 관리

· 클래스 의존 관계
: MemberService -> MemberRepository(Interface) -> MemoryMemberRepository

# 03-2. 회원 도메인과 리포지토리 만들기

 - id와 name을 받는 회원 객체 생성(Member class)

package hello.hellospring.domain;
public class Member {

    private Long id;
    private String name;
    public Long getId() {
        return id;
    }

    //generate -> Getter and Setter
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
*************************************************

 - 회원 리포지토리 인터페이스 생성(MemberRepository Interface)

 package hello.hellospring.repository;


import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    List<Member> findAll();
}
*************************************************
 - 회원 리포지토리 메모리 구현체 (MemoryMemberRepository class)

 package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Member save(Member member) { //store에 넣기 전 member id 값을 세팅하고 store에 저장
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override //Optional.ofNullable : 반환값이 없는 경우가 존재 가능하기 떄문에 사용
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override //parameter로 넘어온 name이랑 같은 경우에만 필터링 -> 찾으면 반환, 끝까지 없으면 NULL 반환
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override //store에 있는 멤버들 모두 반환
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }
}
****************************************************

# 03-3. 회원 레포지토리 테스트 케이스 작성

- 보통 개발한 기능을 실행해서 테스트 할 때 : 자바의 main 메서드를 통해 실행하거나, 웹 어플리케이션의 컨트롤러를 통해 해당 기능을 실행 -> 준비하고 실행하는데 오래 걸리고, 반복 실행하기 어렵고 여러 테스트를 한 번에 실행하기 어려움

=> 자바 : JUnit 이라는 프레임워크로 테스트를 실행해서 이러한 문제 해결

- test package에서 ~~test class를 만들어서 사용

@test
public void ~~ (){

}
****************************************************
@Test //save 테스트
    public void save(){
        Member member = new Member();
        member.setName("spring");

        repository.save(member);

        Member result = repository.findById(member. getId()).get();
        assertThat(member).isEqualTo(result); //result == member
    }

@Test //fileByName 테스트
    public void findByName(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

         Member result = repository.findByName("spring1").get();

         assertThat(result).isEqualTo(member1); //member1 == result
    }

    @Test //findAll 테스트
    public void findAll(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2); //member1과 member2를 넣었기 때문에 사이즈를 2로 두고 검사
    }
    ************************************************
- 전체 실행시 오류 : 순서 때문
- findAll에서 spring1, spring2가 이미 저장
=> 테스트 하나가 끝나고 나면 데이터를 삭제해줘야함 (한번에 여러 테스트를 실행하면 메모리 DB에 직전 테스트의 결과가 남을 수 있다. 이렇게 되면 다음 이전 테스트 때문에 다음 테스트가 실패할 가능성이 있다)

: @AfterEacg : 각 테스트가 종료될 때 마다 이 기능을 실행 (여기서는 DB에 저장된 데이터를 삭제)

@AfterEach
    public void afterEach(){
        repository.clearStore();
    }

# 03-4. 회원 서비스 개발

· 회원가입
public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }
    
    private void validateDuplicateMember(Member member) { //validateDuplicateMembe 메소드로 생성
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

****************************************************
· 전체 회원 조회
public List<Member> findMembers() {
        return memberRepository.findAll(); //findAll
    }
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
****************************************************

# 03-5. 회원 서비스 테스트

- test 생성 단축기 : ctrl+shift+T

- test 작성 방법 : given, when, then

· 회원 가입 테스트
@Test
    void 회원가입() { //한글로 작성해도 됨
        //given
        Member member = new Member();
        member.setName("spring");
        //when
        Long saveID = memberService.join(member);

        //then
        Member findMember = memberService.findOne(saveID).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }
***************************************************

· 중복 회원 예외
@Test
    public void 중복_회원_예외(){
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));
    }
****************************************************
- new 사용하지 않고 객체 생성
@BeforeEach
    public void beforeEach(){
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }
-> @BeforeEach : 각 테스트 실행 전에 호출

**04. 스프링 빈과 의존관계**
# 04-1. 컴포넌트 스캔과 자동 의존관계 설정

· 스프링 빈을 등록하는 2가지 방법
 - 컴포넌트 스캔과 자동 의존관계 설정하기
 - 자바 코드로 직접 스프링 빈 등록하기

· 컴포넌트 스캔 원리 
: @Component 애노테이션이 있으면 스프링 빈으로 자동 등록된다. 
@Controller 컨트롤러가 스프링 빈으로 자동 등록된 이유도 컴포넌트 스캔 때문이다.

​@Component 를 포함하는 다음 애노테이션도 스프링 빈으로 자동 등록된다. 
: @Controller @Service @Repository

· 회원 서비스 스프링 빈 등록

@Service
public class MemberService {
 private final MemberRepository memberRepository;
 @Autowired
 public MemberService(MemberRepository memberRepository) {
 this.memberRepository = memberRepository;
 }
}

- @Autowired : 객체 생성 시점에 스프링 컨테이너에서 해당 스프링 빈을 찾아서 주입

· 회원 리포지토리 스프링 빈 등록

@Repository
public class MemoryMemberRepository implements MemberRepository {}

- 스프링은 스프링 컨테이너에 스프링 빈을 등록할 때, 기본으로 싱글톤으로 등록(유일하게 하나만 등록해서 공유)
=> 같은 스프링 빈이면 모두 같은 인스턴스

# 04-2. 자바 코드로 직접 스프링 빈 등록하기

- SpringConfig 클래스
@Configuration
public class SpringConfig {

    @Bean //MemberService
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean //MemberRepository
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}

@Controller //컨트롤러
public class MemberController {

    private final MemberService memberService;
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}


​
