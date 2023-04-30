# 3주차

### 회원 관리 예제 - 백엔드 개발

- 비즈니스 요구사항 정리
    - 데이터: 회원ID, 이름
    - 기능: 회원 등록, 조회
    - 데이터 저장소 미정(이라는 가상 시나리오)
    
    ![일반적인 웹 애플리케이션 계층 구조](3%E1%84%8C%E1%85%AE%E1%84%8E%E1%85%A1%20ebe0bbe73fd64cd7a98b031e957c92d2/Untitled.png)
    
    일반적인 웹 애플리케이션 계층 구조
    
    - 컨트롤러: 웹 MVC의 컨트롤러 역할
    - 서비스: 비즈니스 도메인을 가지고 핵심 비즈니스 로직 구현, 예) 중복 가입 방지
    - 리포지토리: 데이터베이스에 접근, 도메인 객체를 DB에 저장하고 관리
    - 도메인: 비즈니스 도메인 객체, 예) 회원, 주분, 쿠폰 등 주로 데이터베이스에 저장하고 관리
    
    ![클래스 의존관계(회원 비즈니스 로직)](3%E1%84%8C%E1%85%AE%E1%84%8E%E1%85%A1%20ebe0bbe73fd64cd7a98b031e957c92d2/Untitled%201.png)
    
    클래스 의존관계(회원 비즈니스 로직)
    
    - 회원 저장(MemberRepositorry) → interface로 설계(데이터 저장소가 선정되지 않았기 때문에 인터페이스로 구현 클래스를 변경할 수 있도록 함)
    - 데이터 저장소는 RDB, NoSQL 등 다양한 저장소를 고민중인 상황으로 가정
    - 개발을 진행하기 위해서 초기 개발 단계에서는 구현체로 가벼운 메모리 기반의 데이터 저장소 사용
    
- 회원 도메인과 리포지토리 만들기
    
    ```jsx
    // Member.java
    
    package hello.hellospring.domain;
    
    public class Member {
    
    //  id 식별자(시스템이 정하는 ID)
        private Long id;
    //  회원이 넣는 이름
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
    
    ```jsx
    // interface MemberRepository.java
    
    package hello.hellospring.repository;
    
    import hello.hellospring.domain.Member;
    
    import java.util.List;
    import java.util.Optional;
    
    public interface MemberRepository {
    //  저장소에 회원 저장
        Member save(Member member);
    //  optional java 8 기능(null 값을 감싸서 반환)
    //  회원 id 이름 찾기
        Optional<Member> findById(Long id);
        Optional<Member> findByName(String name);
    //  지금까지 저장된 모든 파일 리스트 반환
        List<Member> findAll();
    }
    ```
    
    ```jsx
    // MemoryMemberRepository.java
    
    package hello.hellospring.repository;
    
    import hello.hellospring.domain.Member;
    
    import java.util.*;
    
    public class MemoryMemberRepository implements MemberRepository {
    
        private static Map<Long, Member> store = new HashMap<>();
    //  sequence: 키값 생성
        private static long sequence = 0l;
    
        @Override
    //  store에 넣기 전에 id 값 세팅
        public Member save(Member member) {
            member.setId(++sequence);
            store.put(member.getId(), member);
            return member;
    
        }
    
        @Override
        public Optional<Member> findById(Long id) {
    //      null 값을 감싸서 반환
            return Optional.ofNullable(store.get(id));
        }
    
        @Override
        public Optional<Member> findByName(String name) {
    //     루프를 돌렸는데도 없으면 optional에 null 반환
           return store.values().stream()
    //              member.getName()이 name과 같은지 확인, 같은 경우에 필터링
                   .filter(member -> member.getName().equals(name))
    //               찾으면 반환
                   .findAny();
        }
    
        @Override
        public List<Member> findAll() {
    //      Member 반환
            return new ArrayList<>(store.values());
        }
    }
    ```
    
- 회원 리포지토리 테스트 케이스 작성
    
    개발한 기능을 실행해서 테스트 할 때
    
    - main 메서도 통해 실행
    - 웹 애플리케이션의 컨트롤러 통해 해당 기능 실행
        
        → 준비하고 실행에 시간이 오래 소요되고, 반복 실행에 어려움이 있으며 여러 테스트를 한번에 실행하기 어렵다는 단점이 있다.
        
    
    JUnit이라는 프레임워크 테스트를 실행해 이러한 문제 해결
    
    ```jsx
    // MenmoryMemberRepositoryTest.java
    
    package hello.hellospring.repository;
    
    import hello.hellospring.domain.Member;
    import org.junit.jupiter.api.AfterEach;
    import org.junit.jupiter.api.Test;
    
    import java.util.List;
    
    import static org.assertj.core.api.Assertions.*;
    
    class MenmoryMemberRepositoryTest {
        MemoryMemberRepository repository = new MemoryMemberRepository();
    
        @AfterEach
    //    테스트 실행되고 끝날때마다 repository 삭제
        public void afterEach() {
            repository.clearStore();
        }
    
        @Test
        public void save() {
            Member member = new Member();
            member.setName("spring");
    
            repository.save(member);
    //      new memory와 받은 meomory가 같으면 참
            Member result = repository.findById(member.getId()).get();
    //      soutv
            System.out.println("result = " + (result == member));
            assertThat(member).isEqualTo(result);
        }
    
        @Test
        public void findByName() {
            Member member1 = new Member();
            member1.setName("spring1");
            repository.save(member1);
    
            Member member2 = new Member();
            member2.setName("spring2");
            repository.save(member2);
    
            Member result = repository.findByName("spring1").get();
    
            assertThat(result).isEqualTo(member1);
        }
    
        @Test
        public void findAll() {
            Member member1 = new Member();
            member1.setName("spring1");
            repository.save(member1);
    
            Member member2 = new Member();
            member2.setName("spring2");
            repository.save(member2);
    
            List<Member> result = repository.findAll();
    
            assertThat(result.size()).isEqualTo(2);
        }
    
    }
    ```
    
    ```jsx
    // MemoryMemberRepository.java 추가
    
    public void clearStore() {
            store.clear();
        }
    ```
    
    Test는 서로 순서 의존관계 없이 설계 되어야 한다. → clearStore() /  @AfterEach: 각 Test가 종료될 때 마다 실행
    
    TDD: 테스트 주도 개발(테스트를 먼저 만들고 구현 class를 만들어 돌린다)
    
- 회원 서비스 개발
    
    ```jsx
    // MemberService.java
    
    package hello.hellospring.service;
    
    import hello.hellospring.domain.Member;
    import hello.hellospring.repository.MemberRepository;
    import hello.hellospring.repository.MemoryMemberRepository;
    
    import java.util.List;
    import java.util.Optional;
    
    public class MemberService {
    
        private final MemberRepository memberRepository = new MemoryMemberRepository();
    
        //    회원가입
        public Long join(Member member) {
            // 중복 회원 검증
            validateDuplicatMember(member);
            memberRepository.save(member);
            return member.getId();
        }
    
        private void validateDuplicatMember(Member member) {
            // result가 null이 아니라 값이 있으면 동작
            memberRepository.findByName(member.getName())
                    .ifPresent(m -> {
                        throw new IllegalStateException("이미 존재하는 회원입니다.");
                    });
        }
    
        // 전체 회원 조회
        public List<Member> findMembers() {
            return memberRepository.findAll();
        }
    
        public Optional<Member> findOne(Long memberId) {
            return memberRepository.findById(memberId);
        }
    
    }
    ```
    
- 회원 서비스 테스트
    
    ```jsx
    // MemberServiceTest.java
    
    package hello.hellospring.service;
    
    import hello.hellospring.domain.Member;
    import hello.hellospring.repository.MemoryMemberRepository;
    import org.junit.jupiter.api.AfterEach;
    import org.junit.jupiter.api.Assertions;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    
    import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
    import static org.junit.jupiter.api.Assertions.*;
    
    class MemberServiceTest {
    
        MemberService memberService;
        MemoryMemberRepository memberRepository;
        
        @BeforeEach
        public void beforeEach() {
            memberRepository = new MemoryMemberRepository();
            memberService = new MemberService(memberRepository);
        }
    
        @AfterEach
        public void afterEach() {
            memberRepository.clearStore();
        }
    
        @Test
        void 회원가입() {
            //given
            Member member = new Member();
            member.setName("hello");
    
            //when
            Long saveId = memberService.join(member);
    
            //then
            Member findMember = memberService.findOne(saveId).get();
            assertThat(member.getName()).isEqualTo(findMember.getName());
        }
    
        @Test
        public void 중복_회원_예외() {
            //given
            Member member1 = new Member();
            member1.setName("spting");
    
            Member member2 = new Member();
            member2.setName("spting");
    
            //when
            memberService.join(member1);
            IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
    
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    
    //        try {
    //            memberService.join(member2);
    //            fail();
    //        } catch (IllegalStateException e) {
    //            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    //        }
    
            //then
    
        }
    
        @Test
        void findMembers() {
        }
    
        @Test
        void findOne() {
        }
    }
    ```
    
    ```jsx
    // MemberService.java 수
    
    public MemberService(MemberRepository memberRepository) {
            this.memberRepository = memberRepository;
        }
    ```
    
    @BeforeEach: 각 Test 실행 전 호출, Test가 서로 영향이 없도록 항상 새로운 객체를 생성하고 의존관계도 새로 맺어준다.
    

### 스프링 빈과 의존관계

- 컴포넌트 스캔과 자동 의존관계 설정
    
    ```jsx
    package hello.hellospring.controller;
    
    import hello.hellospring.service.MemberService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    
    @Controller
    public class MemberController {
    
        private final MemberService memberService;
    
        @Autowired
        public MemberController(MemberService memberService) {
            this.memberService = memberService;
        }
    }
    
    // 오류 발생
    ```
    
    @Autowired: 스프링이 연관된 객체를 스프링 컨테이너에서 찾아서 넣어준다.(DI(의존성 주입): 객체 의존관계를 외부에서 넣어주는 것
    
    오류:
    
    Consider defining a bean of type 'hello.hellospring.service.MemberService' in
    your configuration.
    
    ![Untitled](3%E1%84%8C%E1%85%AE%E1%84%8E%E1%85%A1%20ebe0bbe73fd64cd7a98b031e957c92d2/Untitled%202.png)
    
    참고: helloController는 스프링이 제공하는 컨트롤러여서 스프링 빈으로 자동 등록된다. @Controller 가 있으면 자동 등록.
    
    ```jsx
    // 오류 해결
    // MemberService.java
    @Service
    public class MemberService {
    
    		@Autowired
        public MemberService(MemberRepository memberRepository) {
            this.memberRepository = memberRepository;
        }
    
    }
    // MemoryMemberRepository.java
    @Repository
    public class MemoryMemberRepository implements MemberRepository {}
    ```
    
    스프링 빈 등록(스프링 컨테이너)
    
    helloController → memberService → memberRepository
    
    - @Component: 애노테이션이 있으면 스프링 빈으로 자동 등록.
    - @Controller: 컨트롤러가 스프링 빈으로 자동 등록 된 이유도 컴포넌트 스캔 때문.
    - @Component를 포함하는 다음 애노테이션도 스프링 빈으로 자동 등록된다.
        - @Controller
        - @Service
        - @Repository
    
    참고: 스프링은 스프링 컨테이너에 스프링 빈을 등록할 때, 기본으로 싱글톤으로 등록한다.(하나만 등록해서 공유) 따라서 같은 스프링 빈이면 모두 같은 인스턴스다. 설정으로 싱글톤이 아니게 설정할 수 있지만, 특별한 경우를 제외하면 대부분 싱글톤을 사용한다.
    
- 자바 코드로 직접 스프링 빈 등록하기
    
    ```jsx
    // SpringConfig.java
    
    package hello.hellospring;
    
    import hello.hellospring.repository.MemberRepository;
    import hello.hellospring.repository.MemoryMemberRepository;
    import hello.hellospring.service.MemberService;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    
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
    
    참고: XML(문서)로 설정하는 방식도 있지만 최근에는 잘 사용하지 않음
    
    참고: DI에는 필드 주입, setter 주입, 생성자 주입 이렇게 3가지 방법이 있다. 의존관계가 실행중에 동적으로 변하는 경우는 거의 없으므로 생성자 주입을 권장
    
    참고: 주로 정형화된 컨트롤러, 서비스, 리포지토리 같은 코드는 컴포넌트 스캔을 사용한다. 정형화 되지 않거나, 상황에 따라 구현 클래스를 변경해야 하면 설정을 통해 스프링 빈으로 등록
    
    주의: @Autowired를 통한 DI는 helloController , memberService 등과 같이 스프링이 관리하는 객체에서만 동작한다. 스프링 빈으로 등록하지 않고 내가 직접 생성한 객체에서는 동작하지 않음