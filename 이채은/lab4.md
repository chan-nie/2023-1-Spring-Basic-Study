# lab4.md

CRUD

[1. CRUD 란]

- **CRUD**는 대부분의 컴퓨터 소프트웨어가 가지는 기본적인 데이터 처리 기능인
    
    ‘**Create(생성)’**
    
    ‘**Read(읽기)’**
    
    ‘ **Update(갱신)’**
    
    ‘ **Delete(삭제)’** 를 묶어서 일컫는 말.
    
- 사용자 인터페이스가 갖추어야 할 기능(정보의 참조/검색/갱신)을 가리키는 용어로서도 사용됨.
- 기본적으로, 스프링 부트로 CRUD 를 구현하기 위해서는 아래 과정이 선행되어야 한다.
    - 1. Entity class 정의
    - 2. Repository interface 생성
    - 3. @RestController 코드 작성

---

- 현재 수강중인 강의에서 JPA 와 JDBA templete 등을 사용해서 데이터 베이스를 구축하였다.
    - C: insert
    - R: select
    
    ---
    
    이제 U, D 의 구현에 대해 알아보겠다.
    
    - U: update
    - D: delete

[2. U]

update 기능을 구현하는 방법은 아래와 같다 .

- 1. Entity 객체 조회
    - Repository 인터페이스의 findById() 메서드를 사용
- 2. Entity 객체 수정
    - 수정하고자 하는 방식에 맞게 필드값을 변경
    - ‘DI’ 중 setter 메서드 or 필드 주입 사용
- 3. 수정된 Entity 저장
    - 수정된 enitity 를 데이터베이스에 저장해야함.→ save() 메서드 사용

[3. D]

delete 기능을 구현하는 방법은 아래와 같다

- 1. Entity 객체 조회
    - 해당 Entity 를 데이터베이스에서 조회 ( 위와 같은 방법으로)
- 2. Entity 삭제
    - Repository 인터페이스의 delete() 메서드 사용하기
 -------------
 =======
 <  D 코드 구현 >
 //[JPA Entity mapping 코드에 del 코드 넣음]
 //스프링 데이터 JPA로 구현하려고 하니까, delete() 메서드 내에서 EntityManager를 사용하여 해당 멤버 정보를 삭제하도록 만들것임
 package hello.hellospring.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // 생성자, Getter, Setter 생략

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
    
    // 삭제 기능 추가
    public void delete() {
    // EntityManager 주입
    @PersistenceContext 
    private EntityManager entityManager;

    entityManager.remove(this);  // 엔티티 메니저를 사용해서 멤버 삭제하는 코드 ('추가한 부분')
    }
}
