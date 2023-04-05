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

## 자바코드로 직접 스프링 빈 등록하기

- @Service, @Repository, @Autowired 제거 후 진행

1. [SpringConfig.java](http://SpringConfig.java) 생성

```java
@Configuration
public class SpringConfig {
@Bean
public MemberService memberService() {
return new MemberService(memberRepository());
}
@Bean
public MemberRepository memberRepository() {
return new MemoryMemberRepository();
}
}
```
