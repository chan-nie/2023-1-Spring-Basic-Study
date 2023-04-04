package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    /**
     * @param member
     * @return
     */
    @Override
    /** 첫번째 overide: ID 값 저장받아옴 by save.,, ID 는 시스템이 정한 값 */
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }


    @Override
    /**store 값이 null 일때 요즘은 optional 로 감싸서 반환을 해줌.*/
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    /**get 으로 받은 이름이 parameter 과 같은 값인지 체크해주는 부분*/
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();    /**같은 게 찾아지면 반환 , 없으면 optional 사용함*/
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }
}
