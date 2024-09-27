package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository {
    //MemberRepository 위에 커서 올리고 Alt+enter

    //Quick import : ctrl+space
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L; //동시성 문제를 고려해야 하지만 단순하게 작업

    @Override
    public Member save(Member member) {
        member.setId(++sequence); // ID 값 세팅
        store.put(member.getId(), member); // map 에 저장한다.
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id)); // null 반환 가능성 있음 -> optional.ofNullable()
    }

    @Override
    public Optional<Member> findByName(String name) {
        // getName 이 파라미터로 받아온 name 과 같은지 확인, 같을 때만 필터링 됨.
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny(); //하나라도 찾으면 즉시 반환
    }

    @Override
    public List<Member> findAll() {
        // 반환할 때는 List 로 반환
        return new ArrayList<>(store.values()); // 모든 value 반환
    }

//    @Override
//    public void delete(String name) {
//        store.remove(name);
//    }

    public void clearStore() {
        store.clear();
    }
}
