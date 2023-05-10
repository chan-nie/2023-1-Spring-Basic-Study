package hello.hellospring.repository;
import hello.hellospring.domain.Member;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
public class JpaMemberRepository implements MemberRepository {
    private final EntityManager em;
    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }
    public Member save(Member member) {
        em.persist(member);
        return member;
    }
    import org.springframework.transaction.annotation.Transactional
    @Transactional
    public class MemberService {}
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);  // id 조회하기
        return Optional.ofNullable(member);
    }
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
    public Optional<Member> findByName(String name) {   //특별한 jpa quary 를 사용해야함.
        List<Member> result = em.createQuery("select m from Member m where
                m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny();
    }
}


