# 스프링 의존성 주입: dependency injection

### Spring의 핵심 프로그래밍 모델

- IoC/DI : object의 생명 주기와 의존관계
- 서비스 추상화 : 하위 시스템의 공통점을 뽑아 분리시키는 것
- AOP : Aspect-Oriented Programming, 관점 지향 프로그래

### DI (Dependency Injection, 의존성 주입)

- 객체 간 의존성을 개발자가 객체 내부에서 직접 호출(new연산자)하는 대신, 외부(스프링 컨테이너)에서 객체를 생성해서 넣어주는 방식이다.
- 외부에서 두 객체 간의 관계를 결정해주는 디자인 패턴
- 인터페이스를 사이에 둬서 클래스 레벨에서는 의존관계가 고정되지 않도록함
- 런타임 시에 관계를 주입하여 유연성을 확보하고 결합도를 낮춤

### “의존한다” 의미

```java
public class Controller{
   private Service service;

   service.test();
   
   }
}
```

“Controller는 Service에 의존성이 있다.”

= Service가 변하면 Controller의 기능에도 영향을 미친다

### DI 방법 1. 생성자 주입 (권장)

```java
@Controller 
public class Controller{
   private Service service;

   @Autowired 
   public Controller(Service service){
     this.service = service; 
   }
}
```

`**@Autowired**`

- 인스턴스 생성 시 반드시 1번은 호출됨
- 주입받은 객체가 변하지 않거나, 반드시 객체주입이 필요한 경우 강제하기 위해 사용

---

**생성자 주입이 권장되는 이유**

**1. 객체 불변성 확보** 

- 생성자로 한번 의존관계를 주입하면, 생성자가 다시 생성될 일이 없어 불변 객체를 보장함

**2. 테스트 용이**

- 필드 주입의 경우 단위 테스트가 불가능하다.

**3. 순환참조 에러 방지**

- **순환참조** :  A객체는 B객체를 참조하고, B객체는 A객체를 서로 동시에 참조하는 경우 -> **StackOverFlow**
- 순환참조 예시 코드
    
    ```java
    @Service
    public class ServiceA{
      @Autowired
      private ServiceB serviceB;
     
      public void test(){
       serviceB.test(); //A가 B의 메서드 호출 
      }
    }
    ```
    
    ```java
    @Service
    public class ServiceB{
      @Autowired
      private ServiceA serviceA;
     
      public void test(){
       serviceA.test(); //B가 A의 메서드 호출 
      }
    }
    ```
    
- 사실 3가지 방법 모두 에러가 발생하지만 생성자 주입에서는 compile 에러 발생시 프로그램 실행 자체가 되지 않기 때문에 개발자 입장에서는 실제 서비스 되기 전, 순환 참조 문제를 해결하도록 한다.

### DI 방법 2. 필드 주입

```java
@Controller
public class Controller{
  @Autowired 
  private Service service;
}
```

- 코드가 간결하고 편하지만 의존관계를 정확히 파악하기 힘들다.
- 필드 주입 시 `final` 키워드를 선언할 수 없어 객체가 변할 수 있다.

### DI 방법 3. ****수정자(setter) 주입****

```java
@Controller 
public class Controller{
   private Service service;

   @Autowired 
   public setService(Service service){
     this.service = service; //setter
   }
}
```

- setter 는객체가 변경될 필요성이 있을 때만 사용한다.