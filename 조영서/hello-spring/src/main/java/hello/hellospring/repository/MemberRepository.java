package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member); //회원 저장 시 저장된 회원이 반환
    Optional<Member> findById(Long id); //ID로 회원 찾기
    Optional<Member> findByName(String name); //Optional로 감싸서 반환
    List<Member> findAll(); //모든 회원 반환

}
