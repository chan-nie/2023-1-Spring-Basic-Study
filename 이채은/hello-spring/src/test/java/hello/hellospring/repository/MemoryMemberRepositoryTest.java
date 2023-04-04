package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class MemoryMemberRepositoryTest {
    MemberRepository repository=new MemoryMemberRepository();

    @AfterEach  /** 콜벡 메서드, 각각이 끝나고 다시 돌아오게 함 */
    public void afterEach(){
        repository.clearStore();
    }

    @Test
    public void save(){
        Member member=new Member();
        member.setName("Spring");

        repository.save(member);
        Member result=repository.findById(member.getId()).get();
        Assertions.assertThat(member).isEqualTo(result);

    }
    @Test
    public void findByName(){  /**spring1, spring2 라는 회원이 존재하는 상황*/
        Member member1=new Member();
        member1.setName("Spring1");
        repository.save(member1);

        Member member2=new Member();
        member2.setName("Spring2");
        repository.save(member2);

        //when
        Member result = repository.findByName("spring1").get();
        //then
        Assertions.assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll(){
        //given
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);
        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);
        //when
        List<Member> result = repository.findAll();
        //then
        Assertions.assertThat(result.size()).isEqualTo(2);
    }
}
