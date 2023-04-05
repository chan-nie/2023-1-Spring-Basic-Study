package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

// 굳이 public일 필요 없음
class MemoryMemberRepositoryTest {
    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach  // 하나의 test 메소드가 끝날 때마다 실행됨 (콜백함수 같이)
    public void afterEach() {
        repository.clearStore();
    }

    // save기능이 동작하는지 확인 -> @Test써주기 -> 아래 메소드가 실행됨
    @Test
    public void save(){
        Member member = new Member();
        member.setName("spring");

        repository.save(member);

        // 리턴 타입이 Optional이므로, Optional에서 꺼내기 위해 get()
        Member result = repository.findById(member.getId()).get();

        // 메소드 안에서 new로 만든 멤버와 DB에서 꺼낸 것이 같은지 검증 -> Assertions 사용하기
        // Assertions.assertEquals(member, result);  // member가 expected이고, 이게 result와 같아야 함

        // assertj의 Assertions
        assertThat(member).isEqualTo(result);
    }

    @Test
    public void findByName(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();

        assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2);
    }
}
