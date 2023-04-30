# 3주차

## 섹션 3. 회원 관리 예제 - 백엔드 개발

[IntelliJ IDEA 윈도우 단축키 Cheatsheet](https://sslblog.tistory.com/26)

## 목차

[비즈니스 요구사항 정리](https://www.notion.so/891de921d0eb47efaeae9cfb84024873) 

[회원 도메일과 리포지토리 만들기](https://www.notion.so/7097da4887b941f7a67f144d3c46da61) 

[회원 리포지토리 테스트 케이스 작성](https://www.notion.so/585f8a231a6d49e4b8ec44359a2e7e61) 

[회원 서비스 개발](https://www.notion.so/0bd1319457384716bd53b78b53d5080d) 

[회원 서비스 테스트](https://www.notion.so/1b710d6f5ab340c98e97f35dfe06cc8c) 

---

## 비즈니스 요구사항 정리

- 데이터: 회원 ID, 이름
- 기능: 회원 등록, 조회
- 아직 데이터 저장소가 선정되지 않음(가상의 시나리오)

![Untitled](3%E1%84%8C%E1%85%AE%E1%84%8E%E1%85%A1%20d0c9d81188df40a196676a56d62cecd2/Untitled.png)

- 컨트롤러: 웹 MVC의 컨트롤러 역할
- 서비스: 핵심 비즈니스 로직 구현
- 리포지토리: 데이터베이스에 접근, 도메일 객체를 DB에 저장하고 관리
- 도메인: 비즈니스 도메일 객체, 예) 회원, 주문, 쿠폰 등등 주로 데이터 베이스에 저장하고 관리됨

![Untitled](3%E1%84%8C%E1%85%AE%E1%84%8E%E1%85%A1%20d0c9d81188df40a196676a56d62cecd2/Untitled%201.png)

- 아직 데이터 저장소가 선정되지 않아, 우선 인터페이스로 구현 클래스를 변경할 수 있도록 설계
- 데이터 저장소는 RDB, NoSQL 등등 다양한 저장소를 고민중인 상황으로 가정 → 바꿀 수 있도록 인터페이스로 작업
- 개발을 진행하기 위해서 초기 개발 단계에서는 구현체로 가벼운 메모리 기반의 데이터 저장소 사용

---

## 회원 도메일과 리포지토리 만들기

************회원 객체************

```java
package hello.hellospring.domain;

public class Member {

    private Long id; //회원 ID
    private String name; //이름

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

****************************************************회원 리포지토리 인터페이스****************************************************

```java
package hello.hellospring.domain.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository { //기능 4가지 구현
    Member save(Member member); //회원이 저장소에 저장됨
    Optional<Member> findById(Long id); // (Java8) Null 처리: Optional 감싸기
    Optional<Member> findByName(String name);
    List<Member> findAll(); //지금까지 받은 모든 회원의 정보 반환
}
```

**********************회원 리포지토리 메모리 구현체**********************

```java
package hello.hellospring.domain.repository;

import hello.hellospring.domain.Member;

import java.util.*;

/* 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려 */

public class MemoryMemberRepository implements MemberRepository {
    //MemberRepository 위에 커서 올리고 Alt+enter

    //Quick import : ctrl+space
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L; //동시성 문제를 고려해야 하지만 단순하게 작업

    @Override
    public Member save(Member member) {
        member.setId(++sequence); // ID 값 세팅
        store.put(member.getId(), member); // map 에 저장한다.
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id)); // null 반환 가능성 있음 -> optional.ofNullable()
    }

    @Override
    public Optional<Member> findByName(String name) {
        // getName 이 파라미터로 받아온 name 과 같은지 확인, 같을 때만 필터링 됨.
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny(); //하나라도 찾으면 즉시 반환
    }

    @Override
    public List<Member> findAll() {
        // 반환할 때는 List 로 반환
        return new ArrayList<>(store.values()); // 모든 value 반환
    }
}
```

---

## 회원 리포지토리 테스트 케이스 작성

> 개발한 기능을 실행해서 테스트 할 때 자바의 main 메서드를 통해서 실행하거나, 웹 애플리케이션의 컨트롤러를 통해서 해당 기능을 실행한다. 이러한 방법은 준비하고 실행하는데 오래 걸리고, 반복 실행하기 어렵고 여러 테스트를 한번에 실행하기 어렵다는 단점이 있다. 자바는 **`JUnit`이라는 프레임워크로 테스트를 실행해서 이러한 문제를 해결**한다.
> 

************************************************************************************************회원 리포지토리 메모리 구현체 테스트************************************************************************************************

`src/test/java/하위폴더` 에 생성

```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import hello.hellospring.domain.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

// 굳이 public 으로 안해도 됨
class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach() {
        repository.clearStore(); // 저장소 지우기, 실행 순서가 상관 없게
    }

    @Test // save() 기능이 동작하는지 확인
    public void save() {
        Member member = new Member();
        member.setName("spring");

        repository.save(member);

        Member result = repository.findById(member.getId()).get();
        assertThat(member).isEqualTo(result); // Alt enter
    }

    @Test
    public void findByname() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member(); // 이름 바꾸기 shift F6
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();// ctrl + alt + v

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

- `@AfterEach` : 한번에 여러 테스트를 실행하면 메모리 DB에 직전 테스트의 결과가 남을 수 있다. 이렇게 되면 다음 이전 테스트 때문에 다음 테스트가 실패할 가능성이 있다. `@AfterEach` 를 사용하면 각 테스트가 종료될 때 마다 이 기능을 실행한다. 여기서는 메모리 DB에 저장된 데이터를 삭제한다.
- **테스트는 각각 독립적으로 실행되어야 한다. 테스트 순서에 의존관계가 있는 것은 좋은 테스트가 아니다.**
- **TDD** : ****테스트 주도 개발(Test-Driven-Development****)은 소프트웨어 개발 방법론 중의 하나로, 선 개발 후 테스트 방식이 아닌 **선 테스트 후 개발** 방식의 프로그래밍 방법 // 검증할 수 있는 틀을 먼저 만드는 것

---

## 회원 서비스 개발

```java
package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.boot.env.ConfigTreePropertySource.PropertyFile.findAll;

public class MemberService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();

    // 회원 가입
    public Long join(Member member) {
        //같은 이름이 있는 중복 회원X, null 가능성 -> Optional 안에 객체를 넣는 거임.

        /* 아래와 같은 의미의 코드
        Optional<Member> result = memberRepository.findByName(member.getName());
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다. ");
        });
        */

        // ctrl alt shift T
        validateDuplicateMember(member); // 중복 회원 검증

        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                        .ifPresent(m -> {
                            throw new IllegalStateException("이미 존재하는 회원입니다. ");
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

---

## 회원 서비스 테스트

기존에는 회원 서비스가 메모리 회원 리포지토리를 직접 생성하게 했다.

```java
public class MemberService {
   private final MemberRepository memberRepository =
   new MemoryMemberRepository();
}
```

*회원 리포지토리의 코드가

****************************************************************회원 서비스 코드를 DI 가능하게 변경한다.**************************************************************** 

```java
public class MemberService {

   private final MemberRepository memberRepository;

   public MemberService(MemberRepository memberRepository) {
	   this.memberRepository = memberRepository;
   }
   ...
}
```

**************************************회원 서비스 테스트**************************************

```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

// 굳이 public 으로 안해도 됨
class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach() {
        repository.clearStore(); // 저장소 지우기, 실행 순서가 상관 없게
    }

    @Test // save() 기능이 동작하는지 확인
    public void save() {
        Member member = new Member();
        member.setName("spring");

        repository.save(member);

        Member result = repository.findById(member.getId()).get();
        assertThat(member).isEqualTo(result); // Alt enter
    }

    @Test
    public void findByname() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member(); // 이름 바꾸기 shift F6
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();// ctrl + alt + v

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

`@BeforeEach`: 각 테스트 실행 전에 호출된다. 테스트가 서로 영향이 없도록 항상 새로운 객체를 생성하고, 의존관계도 새로 맺어준다

---

## 섹션 4. 스프링 빈과 의존 관계

### 목차

[컴포넌트 스캔과 자동 의존관계 설정](https://www.notion.so/31b071f0961a47d9a551256a5b8b4549) 

[자바 코드로 직접 스프링 빈 등록하기](https://www.notion.so/9570f4f8ed99443683feb4fc1904eed7) 

## 컴포넌트 스캔과 자동 의존관계 설정

회원 컨트롤러가 회원서비스와 회원 리포지토리를 사용할 수 있게 의존관계를 준비

****************************************************************회원 컨트롤러에 의존관계 추가****************************************************************

```java
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
```

- 생성자에 @Autowired 가 있으면 스프링이 연관된 객체를 스프링 컨테이너에서 찾아서 넣어준다. 이렇게 객체 의존관계를 외부에서 넣어주는 것을 DI (Dependency Injection), 의존성 주입이라 한다.
- 이전 테스트에서는 개발자가 직접 주입했고, 여기서는 @Autowired에 의해 스프링이 주입해준다.

****************오류 발생****************

> `Consider defining a bean of type 'hello.hellospring.service.MemberService' in your configuration.`
> 

**memberService가 스프링 빈으로 등록되어 있지 않다.**

![Untitled](3%E1%84%8C%E1%85%AE%E1%84%8E%E1%85%A1%20d0c9d81188df40a196676a56d62cecd2/Untitled%202.png)

> 참고: helloController는 스프링이 제공하는 컨트롤러여서 스프링 빈으로 자동 등록된다.
> 
> 
> `@Controller` 가 있으면 자동 등록
> 

**스프링 빈을 등록하는 2가지 방법**

- 컴포넌트 스캔과 자동 의존관계 설정
- 자바 코드로 직접 스프링 빈 등록하기

**컴포넌트 스캔 원리**

- `@Component` 애노테이션이 있으면 스프링 빈으로 자동 등록된다.
- `@Controller` 컨트롤러가 스프링 빈으로 자동 등록된 이유도 컴포넌트 스캔 때문이다.
- `@Component` 를 포함하는 다음 애노테이션도 스프링 빈으로 자동 등록된다
    - `@Controller`
    - `@Service`
    - `@Repository`

**회원 서비스 스프링 빈 등록**

```java
@Service
public class MemberService {

   private final MemberRepository memberRepository;

   @Autowired
   public MemberService(MemberRepository memberRepository) {
   this.memberRepository = memberRepository;
   }

}
```

> 참고: 생성자에 `@Autowired` 를 사용하면 객체 생성 시점에 스프링 컨테이너에서 **해당 스프링 빈을 찾아서 주입**한다. 생성자가 1개만 있으면 `@Autowired` 는 생략할 수 있다.
> 

**회원 리포지토리 스프링 빈 등록**

```java
@Repository
public class MemoryMemberRepository implements MemberRepository {}
```

**스프링 빈 등록 이미지**

![@Component를 포함하는 애노테이션이 있으면 스프링이 컨테이너에 등록을 하고
@Autowired 가 다 연결시켜준다. ](3%E1%84%8C%E1%85%AE%E1%84%8E%E1%85%A1%20d0c9d81188df40a196676a56d62cecd2/Untitled%203.png)

@Component를 포함하는 애노테이션이 있으면 스프링이 컨테이너에 등록을 하고
@Autowired 가 다 연결시켜준다. 

- `memberService` 와 `memberRepository` 가 스프링 컨테이너에 스프링 빈으로 등록되었다.

> 참고: 스프링은 스프링 컨테이너에 스프링 빈을 등록할 때, 기본으로 싱글톤으로 등록한다(**유일하게 하나만 등록해서 공유한다**) 따라서 같은 스프링 빈이면 모두 같은 인스턴스다. 설정으로 싱글톤이 아니게 설정할 수 있지만, 특별한 경우를 제외하면 대부분 싱글톤을 사용한다.
> 

---

## 자바 코드로 직접 스프링 빈 등록하기

회원 서비스와 회원 리포지토리의 `@Service`, `@Repository`, `@Autowired` 애노테이션을 제거하고 진행한다.

```java
package hello.hellospring;

import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean // 스프링 빈에 등록해줌
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository(); // 구현체
    }
}
```

> 참고: XML로 설정하는 방식도 있지만 최근에는 잘 사용하지 않으므로 생략한다.
> 

> 참고: DI에는 필드 주입, setter 주입, 생성자 주입 이렇게 3가지 방법이 있다. 의존관계가 실행중에 동적으로 변하는 경우는 거의(~~아예~~) 없으므로 **생성자 주입을 권장한다.**
> 

> 참고: 실무에서는 주로 정형화된 컨트롤러, 서비스, 리포지토리 같은 코드는 **컴포넌트 스캔**을 사용한다. 그리고 **정형화 되지 않거나, 상황에 따라 구현 클래스를 변경해야 하면 설정을 통해 스프링 빈으로 등록한다.**
> 

> 주의: `@Autowired` 를 통한 DI는 `helloController` , `MemberService` 등과 같이 스프링이 관리하는 객체에서만 동작한다. 스프링 빈으로 등록하지 않고 내가 직접 생성한 객체에서는 동작하지 않는다.
> 

> 스프링 컨테이너, DI 관련된 자세한 내용은 스프링 핵심 원리 강의에서 설명한다.
>