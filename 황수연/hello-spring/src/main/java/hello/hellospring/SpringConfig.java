package hello.hellospring;

import hello.hellospring.repository.*;
import hello.hellospring.service.MemberService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    //private DataSource dataSource;
    //private EntityManager em;
    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

//    @Autowired
//    public SpringConfig(EntityManager em) {
//        this.em = em;
//    }

//    public SpringConfig(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }


    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }

//    @Bean
//    public MemberRepository memberRepository(){
//        /* 4주차 과제 위해 주석 처리 취소 */
//        //return new MemoryMemberRepository();
//        //return new JdbcMemberRepository(dataSource);
//        //return new JdbcTemplateMemberRepository(dataSource);
//        return new JpaMemberRepository(em);
//    }
}
