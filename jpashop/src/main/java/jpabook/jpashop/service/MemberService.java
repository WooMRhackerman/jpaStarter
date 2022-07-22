package jpabook.jpashop.service;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true) // 기본 트렌섹셔널은 읽기 작업 최적화를 위해 true, 읽기가 아닌 작업에는 트랜섹셔날 따로 부여
@RequiredArgsConstructor  //final이 붙은 변수 기반으로 생성자 자동 생성
public class MemberService {
	private final MemberRepository memberRepository;

	// 회원가입 
	// 등록, 업데이트 작업에는 트렌젝셔날 따로 부여
	@Transactional
	public Long join(Member member) {
		validateMemberName(member);
		memberRepository.save(member);
		return member.getId();
	}

	// 전체조회
	public List<Member> findMembers() {
		return memberRepository.findAll();
	}

	// 단건 조회
	public Member finOne(Long id) {
		return memberRepository.findOne(id);
	}

	private void validateMemberName(Member member) {
		// 에외등록
		List<Member> members = memberRepository.findByName(member.getName());
		if (!members.isEmpty()) {
			throw new IllegalStateException("동일한 이름의 회원이 있습니다.");
		}
	}
	@Transactional
	public void update(Long id, String name) {
		Member findOne = memberRepository.findOne(id);
		findOne.setName(name);
		
	}
}
