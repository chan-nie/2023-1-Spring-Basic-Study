# JPA
- JPA는 기존의 반복 코드는 물론이고, 기본적인 SQL도 JPA가 직접 만들어서 실행해준다.
-  SQL과 데이터 중심의 설계에서 객체 중심의 설계

- 스프링 부트에 JPA 관련 설정 추가
~~~java
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=none
~~~
    - show-sql : JPA가 생성하는 SQL을 출력한다.
    - ddl-auto : JPA는 테이블을 자동으로 생성하는 기능을 제공하는데 none 를 사용하면 해당 기능을 끈다(create를 사용하면 엔티티 정보를 바탕으로 테이블을 직접 생성해줌)

- JPA 엔티티 매핑
~~~java
@Entity
public class Member {
 @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //identity -> DB가 알아서 채워줌
 private Long id;
}
 ~~~

- JPA 회원 리포지토리
~~~java
private final EntityManager em; 
~~~
jpa를 사용하려면 EntityManager를 주입해야한다.
~~~java
public List<Member> findAll() {
 return em.createQuery("select m from Member m", Member.class)
 .getResultList();
 }

 public Optional<Member> findByName(String name) {
 List<Member> result = em.createQuery("select m from Member m where 
m.name = :name", Member.class) //m 객체 자체를 select
 .setParameter("name", name)
 .getResultList();
 return result.stream().findAny();
 }
 ~~~

- MemberService에 트랜잭션 추가
~~~java
 import org.springframework.transaction.annotation.Transactional
@Transactional
public class MemberService {}
~~~
JPA를 통한 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.

- 스프링 설정 변경(SpringConfig)
~~~java
private final EntityManager em;

@Bean
 public MemberRepository memberRepository() {
 return new JpaMemberRepository(em);
 }
~~~

# 스프링 데이터 JPA

- SpringDataJpaMemberRepository 인터페이스 생성
~~~java
package hello.hellospring.repository;
import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface SpringDataJpaMemberRepository extends JpaRepository<Member,
Long>, MemberRepository {
 Optional<Member> findByName(String name);
}
~~~

- 스프링 설정 변경
스프링 데이터 JPA 회원 리포지토리를 사용하도록
~~~java
package hello.hellospring;
import hello.hellospring.repository.*;
import hello.hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class SpringConfig {
 private final MemberRepository memberRepository;
 public SpringConfig(MemberRepository memberRepository) {
 this.memberRepository = memberRepository;
 }
 @Bean
 public MemberService memberService() {
 return new MemberService(memberRepository);
 }
}
~~~
스프링 데이터 JPA가 SpringDataJpaMemberRepository를 스프링 빈으로 자동 등록

- 스프링 데이터 JPA 제공기능
    -   인터페이스를 통한 기본적인 CRUD
    - findByName() , findByEmail() 처럼 메서드 이름 만으로 조회 기능 제공
    - 페이징 기능 자동 제공

# AOP
공통 관심사항과 핵심 관심사항을 분리시켜줌

- AOP를 사용하지 않고 회원 가입 시간, 회원 조회 시간을 측정할 때
~~~java
package hello.hellospring.service;
@Transactional
public class MemberService {
 /**
 * 회원가입
 */
 public Long join(Member member) {
 long start = System.currentTimeMillis();
 try {
 validateDuplicateMember(member); //중복 회원 검증
 memberRepository.save(member);
 return member.getId();
 } finally {
 long finish = System.currentTimeMillis();
 long timeMs = finish - start;
 System.out.println("join " + timeMs + "ms");
 }
 }
 /**
 * 전체 회원 조회
 */
 public List<Member> findMembers() {
 long start = System.currentTimeMillis();
 try {
 return memberRepository.findAll();
 } finally {
 long finish = System.currentTimeMillis();
 long timeMs = finish - start;
 System.out.println("findMembers " + timeMs + "ms");
 }
 }
}
~~~
    - 시간 측정 기능은 핵심 관심 사항이 아니고 유지 보수가 어려워짐
    
# AOP 적용
AOP : Aspect Oriented Programming

- TimeTraceAop 클래스 추가
~~~java
package hello.hellospring.aop;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
@Component
@Aspect
public class TimeTraceAop {
 @Around("execution(* hello.hellospring..*(..))")
 public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
 long start = System.currentTimeMillis();
 System.out.println("START: " + joinPoint.toString());
 try {
 return joinPoint.proceed();
 } finally {
 long finish = System.currentTimeMillis();
 long timeMs = finish - start;
 System.out.println("END: " + joinPoint.toString()+ " " + timeMs +
"ms");
 }
 }
}
~~~

    - 시간을 측정하는 로직을 별도의 공통 로직으로 만듦
    - 변경이 필요한 경우 이 로직만 변경하면 됨
    - 원하는 적용 대상을 선택할 수 있다
        - "execution(* hello.hellospring..*(..))" 문을 변경

- 현재 상태에서의 출력 결과 
|START: execution(String hello.hellospring.controller.MemberController.list(Model))
START: execution(List hello.hellospring.service.MemberService.findMembers())
START: execution(List org.springframework.data.jpa.repository.JpaRepository.findAll())
Hibernate: select member0_.id as id1_0_, member0_.name as name2_0_ from member member0_
END: execution(List org.springframework.data.jpa.repository.JpaRepository.findAll()) 84ms
END: execution(List hello.hellospring.service.MemberService.findMembers()) 89ms
END: execution(String hello.hellospring.controller.MemberController.list(Model)) 100ms|

- MemberService 아래의 것만 시간 측정
"execution(* hello.hellospring.service..*(..))"
|START: execution(List hello.hellospring.service.MemberService.findMembers())
Hibernate: select member0_.id as id1_0_, member0_.name as name2_0_ from member member0_
END: execution(List hello.hellospring.service.MemberService.findMembers()) 108ms|

