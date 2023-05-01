# 3주차 과제 - 스프링의 의존성 주입 방법 조사하기

## 💡 의존성 주입(Dependency Injection) - [스프링 공식 문서](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-factory-collaborators)

🧐 **의존성 주입**이란,
1) **생성자** 매개변수
2) **메소드** 매개변수
3) 새 인스턴스가 생성된 후 또는 메소드의 리턴 값으로서 리턴됐을 때 인스턴스의 **프로퍼티**
 
  와 같은 방식으로 객체들이 자신의 dependency(자신과 같이 작동하는 다른 객체들)를 정의하는 과정

- 스프링 컨테이너는 스프링 빈을 생성할 때 이 dependency들을 주입한다.
- 의존성 주입 규칙을 따르면 코드가 깔끔해진다.

## 1. 생성자 주입

- **의존 관계가 변경되지 않을 경우 사용하는 방법!**

- 스프링에서 **권장하는 이유**
    - 어플리케이션 컴포넌트들을 불변 객체로 구현할 수 있게 해줌 - 객체의 생성자는 객체 생성 시 최초 1회만 호출되기 때문
    - dependency들이 `null`이 아님을 보장해줌 (`NullPointerException` 방지 가능)
    - 또한, 생성자 주입된 컴포넌트들은 호출된 곳에 initialized된 상태로 리턴됨.

- 각각의 매개변수가 dependency를 의미하는 생성자 메소드를 통해 의존성 주입
    - 생성자에 `@Autowired` 어노테이션 → 스프링 컨테이너에 `@Component`로 등록된 빈들 중 생성자에 필요한 빈들을 주입

## 2. 수정자 주입(setter 주입)

- 클래스 내에서 디폴트 값들이 지정될 수 있는 optional한 dependency에만 사용되어야 함. → 그렇지 않으면 not-null 체크가 dependency를 쓰는 모든 곳에서 이루어져야 함


- `@Component`를 통해 실행하는 클래스를 스프링 빈으로 등록 → `@Autowired`가 있는 `setter`메소드에 의존관계 주입
- 선택과 변경 가능성이 있는 의존 관계에 사용
- `@Autowired`를 입력하지 않으면 실행되지 않음

```java
public class SimpleMovieLister {

    // the SimpleMovieLister has a dependency on the MovieFinder
    private MovieFinder movieFinder;

    // a setter method so that the Spring container can inject a MovieFinder
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // business logic that actually uses the injected MovieFinder is omitted...
}
```

## 3. 필드 주입

- **필드에 `@Autowired`** 를 붙여서 바로 주입하는 방법

- 외부에서 변경 불가능하기 때문에 테스트하기 어려움
- DI 프레임워크가 없으면 아무것도 할 수 없음
- 애플리케이션의 실제 코드와 상관없는 특정 테스트를 하고 싶을 때 사용
- 정상적으로 작동되게 하려면 결국 setter가 필요함

```java
@Component
public class CoffeeService {
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final CoffeeRepository coffeeRepository;
}
```

- **필드 주입을 사용하지 않는 이유**
    - 필드 주입을 하게 되면 DI 컨테이너 안에서만 작동함 → 순수 자바 코드로 테스트하기 어려움
    - `final` 키워드라고 불변 속성으로 볼 수도 없고, setter로 가변 속성이라고 볼 수도 없는 애매한 상황이 발생함

## 4. 일반 메소드 주입

- 일반 메소드 앞에 `**@Autowired**` 를 붙여서 의존관계 주입(`@Autowired` 는 모든 메소드에서 사용 가능)

- 한 번에 여러 필드를 주입할 수 있음
- **일반 메소드 주입을 사용하지 않는 이유**
    - 필드 주입과 비슷하게 애매한 상황 발생할 수도 있음
    - 여러 필드를 주입받을 것이라면 차라리 생성자 주입을 사용하는 것이 좋음