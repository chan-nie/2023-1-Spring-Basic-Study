package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository{

    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        // persist는 영구 저장한다는 뜻.
        em.persist(member);  // 리턴 값 없음
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        // find()에 타입과 조회할 식별자(pk) 값 넣기
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        // 이때는 JPQL이라는 쿼리 언어를 사용해야 함
        // 테이블 대상이 아닌 객체 대상으로 쿼리문을 날림 -> SQL문으로 번역됨

        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
