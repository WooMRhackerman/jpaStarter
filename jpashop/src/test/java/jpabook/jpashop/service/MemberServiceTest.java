package jpabook.jpashop.service;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;

@SpringBootTest
@Transactional
@TestPropertySource(properties = "spring.config.location=classpath:application-test.yml" )
class MemberServiceTest {
	@Autowired
	private MemberService ms;
	
	@Autowired
	private MemberRepository mr;
	
	@Autowired
	private EntityManager em;
	
	@Test
	public void 회원가입() {		
		Member member = new Member();
		member.setName("park");
		
		Long saveId =  ms.join(member);
		
		em.flush();
		assertEquals(member, mr.findOne(saveId));
	}
	
	@Test
	public void 중복회원가입확인() {
		Member member = new Member();
		member.setName("park");
		
		Member member2 = new Member();
		member2.setName("park");
		
		ms.join(member);
		IllegalStateException e = assertThrows(IllegalStateException.class,() -> ms.join(member2));
		assertEquals("동일한 이름의 회원이 있습니다.", e.getMessage());
	}
}
