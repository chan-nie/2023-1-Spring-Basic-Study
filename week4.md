# week4 md

[섹션 5 : 회원 웹 기능 3가지 구현하기]

1. 회원을 등록하고 조회할 수 있는 ‘버튼’ 구현
    
    → html 과 유사했던 강좌내용 메인임.
    
2. 회원 등록 기능 구현
    
    → 기능을 구현할때, MemberForm.java 의 실행을 위해 
    
    MemberController 에다가 일반 mapping 이 아닌 ‘post’ mapping 을 해줘야함
    
    (추가설명: 
    
    회원관리 => Members 에서 => http 의 get 방식으로 creatememberform 으로 돌아감. 
    그 다음, 템플릿을 실행시킴.
    그 결과, html 을 뿌림. 
    
    (html 내부의 폼태그 보면, 이를 알 수 있음.)
    
    - name=> 서버로 넘어올때 키가 됨.)
    - 즉,
        
        post⇒값이 들어오는 경우 사용함
        
        Get⇒ 조회할 때
        
3. 회원 등록 리스트 보여주기 (등록된 회원 보이게 하기)
    
    
    - MemberController 에다가 멤버스 웹페이지를 GetMapping 시키기.
    목록은 리스트로.
        - find 메서드 사용하면 모든 값을 다 가지고 올 수 있음
        - 겟 매핑을 하여, 리턴값으로 member/List 를 받기 때문에, templete 에서 관련 html 만들어줘야함.
        - **memberList html 에서 thymeleaf 엔진의 기능 이해하기**:
            
            
            템플릿 언어에서 멤버스를($) 읽어낸다.
            
            멤버스안에는 list 로 모든 회원정보가 있는데,
            첫번째 객체의 아이디(), 닉네임 을 뽑아내고 이걸 루프로 돎
            
    
    ---
    
    - 서버를 종료하고 재실행하게 되면, 저장되어 있던 회원 정보들 모두 사라짐( 메모리 안에 있기 때문임 )
        
        해결책=> 파일을 데이터 베이스 에 저장 … → section6 로 연결…
