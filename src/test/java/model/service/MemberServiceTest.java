package model.service;

import model.domain.Member;
import model.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.util.AssertionErrors.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:appConfig.xml")
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //Given
        Member member= new Member();
        member.setName("Lee");

        //when
        Long saveId= memberService.join(member);
        //then
        assertEquals(member,memberRepository.findOne(saveId));
    }
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //Given
        Member member1= new Member();
        member1.setName("lee");
        Member member2= new Member();
        member2.setName("lee");

        //when
        memberService.join(member1);
        memberService.join(member2); //예외 발생
        //Then
        fail("예외 발생");
    }

}
