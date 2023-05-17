# 6주차

### JPA

---

- JPA는 기존의 반복 코드, 기본적인 SQL도 직접 만들어서 실행해줌
- SQL과 데이터 중심의 설계에서 객체 중심의 설계가 가능해진다
- 개발 생산성을 높일 수 있다

**build.gradle 파일에 JPA, h2 데이터베이스 관련 라이브러리 추가하고 refresh**

```java
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-web'
//	implementation 'org.springframework.boot:spring-boot-starter-jdbc'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa' 
	runtimeOnly 'com.h2database:h2'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

`spring-boot-starter-data-jpa` 내부에는 jdbc 관련 라이브러리가 포함되어있음.

**스프링 부트에 JPA 설정 추가** 

`resources/application.properties`

```java
spring.datasource.url=jdbc:h2:tcp://localhost/~/test
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa

spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=none
```

**스프링부트 2.4부터는 `spring.datasource.username=sa` 를 꼭 추가해주어야 한다. 그렇지않으면 오류가 발생.**

- `show-sql` : JPA가 생성하는 SQL을 출력
- `ddl-auto` : JPA는 테이블을 자동으로 생성하는 기능을 제공하는데 `none` 를 사용하면 해당 기능을 끔
    - `none` 대신 `create` 를 사용하면 엔티티 정보를 바탕으로 테이블도 직접 생성해줌

**JPA 엔티티 매핑**

```java
package hello.hellospring.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member {

@Id @GeneratedValue(strategy = GenerationType.IDENTITY) //DB가 자동으로 id를 생성해줌
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

`@GeneratedValue(strategy = GenerationType.IDENTITY)`

: 기본 키 생성을 데이터베이스에 위임, 기본 키 매핑

id 값을 null로 하면 DB가 알아서 AUTO_INCREMENT 해줌

`@Id`를 사용하면 id에만 자동생성

**JPA 회원 리포지토리**

```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository{

    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
```

**서비스 계층에 트랜잭션 추가**

```java
import org.springframework.transaction.annotation.Transactional

@Transactional
	public class MemberService {
}
```

- `org.springframework.transaction.annotation.Transactional` 사용
- 스프링은 해당 클래스의 메서드를 실행할 때 트랜잭션을 시작하고, 메서드가 정상 종료되면 트랜잭션을 커밋한다. 만약 런타임 예외가 발생하면 롤백.
- JPA를 통한 모든 데이터 변경은 트랜잭션 안에서 실행

**JPA를 사용하도록 스프링 설정 변경**

---

```java
package hello.hellospring;

import hello.hellospring.repository.*;
import hello.hellospring.repository.JdbcMemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    private EntityManager em;

    @Autowired
    public SpringConfig(EntityManager em) {
        this.em = em;
    }

    @Bean // 스프링 빈에 등록해줌
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository(); // 구현체
//        return new JdbcMemberRepository(dataSource);
//        return new JdbcTemplateMemberRepository(dataSource);
        return new JpaMemberRepository(em);
    }
}
```

### 스프링 데이터 JPA

---

- 스프링 부트와 JPA만 사용해도 개발 생산성이 정말 많이 증가하고, 개발해야할 코드도 확연히 줄어듭니다.
- 여기에 스프링 데이터 JPA를 사용하면, 기존의 한계를 넘어 마치 마법처럼, 리포지토리에 구현 클래스 없이 **인터페이스만으로** 개발을 완료할 수 있습니다.
- 그리고 반복 개발해온 **기본 CRUD 기능**도 스프링 데이터 JPA가 모두 제공합니다.
- 따라서 개발자는 **핵심 비즈니스 로직을 개발하는데, 집중**할 수 있습니다. 실무에서 관계형 데이터베이스를 사용한다면 스프링 데이터 JPA는 이제 선택이 아니라 필수 입니다.

> 주의: 스프링 데이터 JPA는 JPA를 편리하게 사용하도록 도와주는 기술입니다. **따라서 JPA를 먼저 학습한 후에 스프링 데이터 JPA를 학습**해야 합니다.
> 

**스프링 데이터 JPA 회원 리포지토리**

```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository{

    @Override
    Optional<Member> findByName(String name);
}
```

- 스프링 데이터 JPA가 구현체를 만들어줌. 우리는 그냥 쓰면 됨.

**스프링 데이터 JPA 회원 리포지토리를 사용하도록 스프링 설정 변경**

```java
package hello.hellospring;

import hello.hellospring.repository.*;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean // 스프링 빈에 등록해줌
    public MemberService memberService() {

        return new MemberService(memberRepository);
    }
```

- 스프링 데이터 JPA가 `SpringDataJpaMemberRepository`를 스프링 빈으로 자동 등록해준다.

**스프링 데이터 JPA 제공 클래스**

![https://velog.velcdn.com/images/qorgus31/post/2e7fbfe7-5df2-4b17-8f40-2afbd7b5dda9/image.png](https://velog.velcdn.com/images/qorgus31/post/2e7fbfe7-5df2-4b17-8f40-2afbd7b5dda9/image.png)

**스프링 데이터 JPA 제공 기능**

- 인터페이스만으로도 상상할 수 있는 기본적인 CRUD를 그냥 제공해줌
- `findByName()`, `findByEmail()` 처럼 이름 만으로 조회 기능 제공
- 페이징 기능 자동 제공

## AOP가 필요한 상황

---

- 모든 메소드의 호출 시간을 측정하고 싶을 때.
- **공통 관심 사항**(cross-cutting concern) vs **핵심 관심 사항**(core concern)
- 회원 가입 시간, 회원 조회 시간을 측정하고 싶다면?
    
    → 메소드는 무수히 많이 존재할 수 있기 때문에 모든 메소드에 시간 측정 로직을 집어넣을 수 없음.
    
    → AOP를 통해 공통 로직을 짜서 집어넣어야함.
    

![https://velog.velcdn.com/images/qorgus31/post/1ddf2eef-7869-4697-8243-33493f63a855/image.png](https://velog.velcdn.com/images/qorgus31/post/1ddf2eef-7869-4697-8243-33493f63a855/image.png)

**MemberService 회원 조회 시간 측정 추가**

```java
package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 가입
    public Long join(Member member) {
        //같은 이름이 있는 중복 회원X, null 가능성 -> Optional 안에 객체를 넣는 거임.

        /* 아래와 같은 의미의 코드
        Optional<Member> result = memberRepository.findByName(member.getName());
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다. ");
        });
        */

        long start = System.currentTimeMillis();

        try {
            // ctrl alt shift T
            validateDuplicateMember(member); // 중복 회원 검증
            memberRepository.save(member);
            return member.getId();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("join = " + timeMs + "ms");
        }

    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다. ");
                });
    }

    // 전체 회원 조회
    public List<Member> findMembers() {
        long start = System.currentTimeMillis();
        try {
            return memberRepository.findAll();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("findMembers = " + timeMs + "ms");
        }
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

//    public void deleteMember(Member member){
//        memberRepository.findByName(member.getName()).ifPresent(m -> {
//            memberRepository.delete(m.getName());
//        });
//    }
}
```

**이 코드의 문제점**

- 시간을 측정하는 기능은 **핵심 관심 사항**이 아님
- 시간 측정 로직은 **공통 관심 사항**
- 시간 측정 로직과 핵심 비지니스 로직이 섞여 **유지보수가 어려움**
- 시간 측정 로직을 변경할 때 모든 로직을 찾아가며 변경해야함

### AOP 적용

---

- AOP : Aspect Oriented Programming, 관점 지향 프로그래밍

**공통 관심 사항(cross-cutting concern) vs 핵심 관심 사항(core concern) 분리**

![https://velog.velcdn.com/images/qorgus31/post/f1c89b09-0859-4c31-9f27-6dc013ebfc12/image.png](https://velog.velcdn.com/images/qorgus31/post/f1c89b09-0859-4c31-9f27-6dc013ebfc12/image.png)

**시간 측정 AOP 등록**

```java
package hello.hellospring.aop;

import org.aspectj.lang.ProceedingJoinPoint;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeTraceAop {

    @Around("execution(* hello.hellospring..*(..))" )
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());
        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joinPoint.toString()+ " " + timeMs + "ms");
        }
    }
}
```

**문제 해결**

- 회원가입, 회원 조회등 **핵심 관심사항**과 시간을 측정하는 **공통 관심 사항을 분리**한다.
- 시간을 측정하는 로직을 **별도의 공통 로직**으로 만들었다.
- **핵심 관심 사항을 깔끔하게 유지할 수 있다.**
- 변경이 필요하면 이 로직만 변경하면 된다.
- 원하는 적용 대상을 선택할 수 있다.

**스프링 AOP 동작 방식**

- AOP 적용 전 의존 관계
    
    ![https://velog.velcdn.com/images/qorgus31/post/a8195b52-0eb7-4e09-a693-e2d0f7175a89/image.png](https://velog.velcdn.com/images/qorgus31/post/a8195b52-0eb7-4e09-a693-e2d0f7175a89/image.png)
    
    ![https://velog.velcdn.com/images/qorgus31/post/d42eccaf-cddc-471c-b83e-96691a55ac4c/image.png](https://velog.velcdn.com/images/qorgus31/post/d42eccaf-cddc-471c-b83e-96691a55ac4c/image.png)
    
- AOP 적용 후 의존 관계
    
    ![가짜 멤버 서비스(프록시)를 만듦, 이게 끝나면 진짜 실제를 호출해줌](https://velog.velcdn.com/images/qorgus31/post/316e0a74-bace-41e1-8665-48927bbd95ff/image.png)
    
    가짜 멤버 서비스(프록시)를 만듦, 이게 끝나면 진짜 실제를 호출해줌
    
    ![https://velog.velcdn.com/images/qorgus31/post/70e7f9be-1bc7-44f5-95ed-d3160bfe2f07/image.png](https://velog.velcdn.com/images/qorgus31/post/70e7f9be-1bc7-44f5-95ed-d3160bfe2f07/image.png)
    
- getClass()를 통해 프록시 확인하기

```java
public MemberController(MemberService memberService) {
        this.memberService = memberService;
        System.out.println("memberService = " + memberService.getClass());
    }
```