package jpabook.jpashop.api;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
	private final MemberService ms;
	
	@PostMapping("/api/v1/members")
	public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
		Long id = ms.join(member);
		return new CreateMemberResponse(id);
	}
	
	@PostMapping("/api/v2/members")
	public CreateMemberResponse saveMemberV2(@RequestBody @Valid  CreateMemberRequest smr) {
		Member member = new Member();
		member.setName(smr.getName());
		Long id = ms.join(member);
		return new CreateMemberResponse(id);
	}
	
	@Data
	static class CreateMemberRequest{
		@NotEmpty
		private String name;
	}
	
	@Data
	static class CreateMemberResponse{
		private Long id;
		
		public CreateMemberResponse(Long id) {
			this.id = id;
		}
	}
}
