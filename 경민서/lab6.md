# AOP

[[Spring] AOP(Aspect Oriented Programming)란? 스프링 AOP란?](https://code-lab1.tistory.com/193)

### AOP란?

---

- Aspect Oriented Programming, 관점 지향 프로그래밍
- 핵심적인 관점, 부가적인 관점으로 나누어, 이를 기준으로 모듈화하는 것
- 핵심적인 관점: 비즈니스 로직 등
- 부가적인 관점: 핵심 로직을 실행하기 위한 DB연결, 로깅, 파일 입출력 등

### AOP가 필요한 상황

---

> 흩어진 관심사(Crosscutting Concerns)를 모듈화하자
> 

![Untitled](AOP%205d97276915144fe7a1e19cd0ede04e12/Untitled.png)

각각의 클래스들을 중복되는 메서드가 있을 수 있다. 클래스 A의 주황을 수정해야한다면 B, C의 주황색 부분도 다 찾아서 수정해야 하므로 유지보수가 어렵고 귀찮아진다. 

→ 각 공통되는 부분을 모듈화(Aspect)하고 넣기만 하면 해결. 

### AOP 적용 방법

---

**런타임 적용**

A라는 클래스 타입의 Bean을 만들 때 A 타입의 Proxy Bean을 만든다.

Proxy Bean은 Aspect 코드를 추가하여 동작하게 됨. 

`@Aspect` : 해당 클래스가 Aspect라는 것을 명시

`@Around` : 적용 범위 지정