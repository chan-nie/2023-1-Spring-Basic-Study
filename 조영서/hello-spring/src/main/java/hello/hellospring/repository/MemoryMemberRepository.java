package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryMemberRepository implements MemberRepository {
    private static Map<Long, Member> store = new HashMap<>(); //저장
    private static long sequence = 0L; //key 값 생성

    @Override
    public Member save(Member member) {
        member.setId(++sequence); //ID는 시스템이 정해주는 것
        store.put(member.getId(), member);
        return member;
    }
    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id)); //store에서 꺼내기
    }
    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values()); //member 모두 반환
    }
    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream() //루프 돌기
                .filter(member -> member.getName().equals(name)) //같은 경우에만 필터링
                .findAny(); //반환
    }
    public void clearStore() {
        store.clear();
    }
}
