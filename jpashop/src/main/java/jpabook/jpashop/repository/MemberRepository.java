package jpabook.jpashop.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
	//일반적인 @@RequiredArgsConstructor를 사용하지 않는 생성자 기반이 아닌 케이스의 인젝션
	//@PersistenceContext
	//private EntityManager em;
	
	//@RequiredArgsConstructor를 활용한 인젝션 
	private final EntityManager em;
	
	public void save(Member member) {
		em.persist(member);
	}
	
	public Member findOne(Long id) {
		return em.find(Member.class, id);
	}
	
	public List<Member> findAll(){
		return em.createQuery("select m from Member m", Member.class)
			.getResultList();
	}
	
	public List<Member> findByName(String name){
		return em.createQuery("select m from Member m where name = :name", Member.class)
				.setParameter("name", name).getResultList();
	}
}
