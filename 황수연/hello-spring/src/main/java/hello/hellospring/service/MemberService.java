package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    // @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입
     **/
    public Long join(Member member){
        // 같은 이름이 있는 중복 회원은 안됨
        validateDuplicateMember(member);  // 중복 회원 검사

        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // findByName()이 Optional을 리턴하므로 바로 메소드 사용 가능
        // result가 null 이 아니고 어떤 값이 있다면 (result는 optional이기 때문에 가능)
        // 아래에는 뭔가 로직이 있음.. -> 메소드로 뽑는 것이 좋음.
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }

    /* 4주차 과제 - 회원 삭제 */

    public Optional<Member> findOneByName(String memberName){
        return memberRepository.findByName(memberName);
    }
    public void deleteMember(Member member){ ((MemoryMemberRepository)memberRepository).deleteMember(member);  }
}
