package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository { //기능 4가지 구현
    Member save(Member member); //회원이 저장소에 저장됨
    Optional<Member> findById(Long id); // (Java8) Null 처리: Optional 감싸기
    Optional<Member> findByName(String name);
    List<Member> findAll(); //지금까지 받은 모든 회원의 정보 반환
    void delete(String name);
}
