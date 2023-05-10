# lab3.md

## DI 주입

스프링에서 제공하는 DI 는 3가지로 사용가능

1. 필드 주입
2. Setter 주입
3. Constructor(생성자) 주입
4. 일반 메서드 주입

---

[1. Field 주입]

- 멤버 객체에 @Autowired 작성 → 강의에서 함

---

[2. Setter 주입]

- Setter 메서드 위에 @Autowired 선언 → setter 메서드의 파라미터에 해당하는 객체를 bean 에서 가지고 옴.

---

[3. Constructor 주입]

1,2 방법: Bean 이 ‘먼저’ 만들어지고 Bean 에서 의존 객체를 가지고 와서 주입

but. 3번 방법 → 만들어지는 순간, 동시에 모든 의존관계를 Bean 을 통해 가지고 와야함.

장점: 

- 순환 참조를 막을 수 있음
- NullPointerException  방지 가능
    
    ( NullPointerException : 첫 번째 줄처럼 선언만 하고 아무것도 참조하지 않는 null 상태인 변수에서 내용을 참조하려고 할 때 발생)
    

---

[4. 일반 메서드 주입]

- 일반 메서드를 통해 의존관계를 주입하는 방법
- @Autowired 는 모든 메서드에서 사용할 수 있기 때문에 일반 메서드 주입이 가능
