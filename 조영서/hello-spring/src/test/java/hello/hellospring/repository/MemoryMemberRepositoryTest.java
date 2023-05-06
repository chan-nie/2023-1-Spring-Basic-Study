package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


public class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository(); //MemoryMemberRepository만 테스트할거니까
    @AfterEach
    public void afterEach() { //테스트가 끝날 때마다 데이터 지우기
        repository.clearStore();
    }
    @Test //실행 가능해짐
    public void save() {
        //given
        Member member = new Member();
        member.setName("spring"); //이름세팅
        //when
        repository.save(member); //리포지토리에 member를 save
        //then
        Member result = repository.findById(member.getId()).get(); //저장하면 Id 세팅되었으니까, Optional의 반환 타입은 get으로 꺼낼 수 있음
        assertThat(result).isEqualTo(member);
        //Assertions.assertEquals(member, result);
        //Assertions.assertThat(member).isEqualsTo(result);
    }
    @Test
    public void findByName() {
        //given
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);
        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);
        //when
        Member result = repository.findByName("spring1").get(); //이름으로 찾기
        //then
        assertThat(result).isEqualTo(member1);
    }
    @Test
    public void findAll() {
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
        assertThat(result.size()).isEqualTo(2);
    }

}
