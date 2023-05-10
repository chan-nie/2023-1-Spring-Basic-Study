# 4주차 과제 - Update, Delete 기능 조사하기

## **CRUD란

- 대부분의 컴퓨터 소프트웨어가 가지는 기본적인 **데이터 처리 기능**인 **Create(생성), Read(읽기), Update(갱신), Delete(삭제)** 를 묶어서 일컫는 말
- 사용자 인터페이스가 갖추어야 할 기능(정보의 참조/검색/갱신)을 가리키는 용어로서도 사용됨
- 데이터 베이스에서는 기초적인 4가지 쿼리 형식을 의미함.

| 이름 | 조작 | SQL |
| --- | --- | --- |
| Create | 생성 | INSERT |
| Read | 조회 | SELECT |
| Update | 수정 | UPDATE |
| Delete | 삭제 | DELETE |
- 클라이언트 ↔ 서버간 HTTP 프로토콜을 이용해 `RESTful`하게 데이터를 전송할 때도 CRUD 개념이 활용됨

| 이름 | 조작 | Method |
| --- | --- | --- |
| Create | 생성 | POST |
| Read | 읽기 | GET |
| Update | 갱신 | PUT |
| Delete | 삭제 | DELETE |

## 1. Update

- 기존 데이터를 수정(갱신)하는 기능

## 2. Delete

- 데이터를 처리하는 기능
