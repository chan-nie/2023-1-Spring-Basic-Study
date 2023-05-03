# 의존성 주입(DI, Dependency Injection)

**의존성 주입**
: 필요한 객체를 직접 생성하는 것이 아닌 외부로부터 객체를 받아서 사용하는 것
 - 객체 간의 의존 관계를 미리 설정해두면 스프링 컨테이너가 의존관계를 자동으로 연결해줌
 => 객체 간의 결합도를 줄이고 코드의 재사용성을 높일 수 있다. 

*@Autowired*
주입하려고 하는 객체의 타입이 일치하는 객체를 loc 컨테이너 안에 존재하는 Bean을 자동으로 주입한다. 

1. Field 주입

~~~java
@Autowired
private SampleObject sampleObject;
~~~
간단하지만 단점
    * 불변성 위반 : 필드 주입 객체는 final 선언 불가
    * 순환 의존성 알 수 없음
    * DI 컨테이너의 결합성과 테스트 용이성 위배
    * 단일 책임의 원칙을 위반할 확률이 높다

2. Setter 주입

~~~java
private InjectSampleService injectSampleService;

	@Autowired  // spring 4.3 버전 이상부터는 생략 가능
	public void setInjectSampleService(InjectSampleService injectSampleService) {
		this.injectSampleService= injectSampleService;
	}
~~~
주로 사용하지 않는 방법
    * setXXX 메서드를 public으로 열어두어야 하기 때문에 언제 어디서든 변경 가능
    * 애플리케이션 동작 중에 상태값이 변경될 수 있음
    * 주입받는 객체가 변경될 가능성이 있는 경우 사용

3. 생성자 주입

~~~java
private InjectSampleService injectSampleService;

	@Autowired  // spring 4.3 버전 이상부터는 생략 가능
	public TestController(InjectSampleService injectSampleService) {
		this.injectSampleService= injectSampleService;
	}	
~~~

* 권장 이유
    * 순환 참조 방지
    * 테스트에 용이
    * final 선언 가능
    * 오류 방지(불변 객체 또는 null 방지 기능)

