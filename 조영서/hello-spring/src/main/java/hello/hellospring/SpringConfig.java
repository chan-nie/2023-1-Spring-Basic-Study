package hello.hellospring;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import hello.hellospring.repository.JdbcMemberRepository;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {
    private final DataSource dataSource;
    @Autowired
    public SpringConfig(DataSource dataSource) { //스프링이 제공
        this.dataSource = dataSource;
    }

    @Bean //스프링 빈에 등록하기
    public MemberService memberService() {
        return new MemberService(memberRepository()); //스프링 빈에 등록
    }
    @Bean //스프링 빈에 등록하기
    public MemberRepository memberRepository() {

        //return new MemoryMemberRepository();'
        return new JdbcMemberRepository(dataSource);
    }
}