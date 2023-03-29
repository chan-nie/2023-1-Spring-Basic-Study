spring boot 3.0.5 버전 설정 후 프로젝트 생성시 빌드 오류(라이브러리) 발생
  -> 3.x 버전은 JDK 버전 17 이상만 가능 따라서 JDK 11이기에 2.7.10 버전 사용


라이브러리 살펴보기
dependencies: 라이브러리 의존관계
(*) 중복 제거
Gradle은 의존관계가 있는 라이브러리를 함께 다운로드

스프링 부트 라이브러리
- spring-boot-starter-web
  - spring-boot-starter-tomcat: 톰캣(웹서버)
  - spring-webmvc: 스프링 웹 MVC
- spring-boot-starterthymeleaf: 타임리프 템플릿 엔전(view)
- spring-boot-starter(공통): 스프링 부트 + 스프링 코어 + 로깅
  - spring-boot
    - spring-core
  - spring-boot-starter-logging
    - logback, slf4j

테스트 라이브러리
- spring-boot-starter-test
  - junit: 테스트 프레임워크(4->5)
  - mockito: 목 라이브러리
  - assertj: 테스트 코드를 좀 더 편하게 작성하게 도와주는 라이브러리
  - spring-test: 스프링 통합 테스트 지원


View 환경설정
welcome page 만들기
'static/index.html'을 올려두면 welcome page 기능 제공
tymeleaf 템플릿 엔진
공식 사이트: thymeleaf.org

동작 환경
- 컨트롤러에서 리턴 값으로 문자를 반환하면 뷰 리졸버('viewResolver')가 화면을 찾아서 처리
  - 스프링 부트 템플릿엔진 기본 viewName 매핑
  - resources:templates/+{ViewName}+.html
  참고) spring-boot-devtools라이브러리를 추가하면, html파일을 컴파일만 해주면 서버 재시작 없이 View 파 번경이 가능하다.
  인텔리J 컴파일 방법: 메뉴 build -> Recompile


빌드하고 실행하기
hello-spring 이동
gradlew.bat build
cd build
cd libs
java -jar hello-spring-0.0.1-SNAPSHOT.jar
실행 확인
빌드 폴더 삭제(gradlew.bat clean)
gradlew.bat clean build


스프링 웹 개발 기초
정적 컨텐츠(파일을 그대로 전달)
- 스프링 부트 정적 컨텐츠 기능

MVC(model, view, controller)와 템플릿 엔진(서버에서 변형 후 전달)
@RequestParam(" ") -> 외부 파라미터 받기
Absolute path 경로로 파일 열기(서버 없이 열 수 있다)
localhost:8080/hello-mvc?name=spring!!!
${ } 모델 꺼내기

API(제이슨(데이터 구조 포맷)으로 전달, 서버끼리 할때)
@ResponseBody http body에 직접 넣겠다는 의미
getter and setter 단축키 alt insert(+fn)
@ResponseBody 사용원리
- http의 body에 문자 내용을 직접 반환
- viewResolver 대신에 HttpMessageConverter가 동작
- 기본 문자처리: StringHttpMessageConverter
- 기본 객체처리: MappingJackson2HttpMessageConverter
- byte 처리 등등 기타 여러 HttpMessageConverter가 기본으로 등록되어 있음
  참고) 클라이언트의 HTTP Accept 헤더와 서버의 컨트롤러 반환 타입 정보 둘을 조합해서 HttpMessageConverter가 선택