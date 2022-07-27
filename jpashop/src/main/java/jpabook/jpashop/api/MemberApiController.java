package jpabook.jpashop.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.websocket.server.PathParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
	private final MemberService ms;
	
	@GetMapping("/api/v1/members")
	public List<Member> MemberV1(@RequestBody @Valid Member member) {
		return ms.findMembers();
	}
	
	@GetMapping("/api/v2/members")
	public Result MemberV2() {
		List<Member> findMembers = ms.findMembers();
		List<MemberDto> collect = findMembers.stream().map(m -> new MemberDto(m.getName()))
							.collect(Collectors.toList());
		
		return new Result(collect.size(), collect);
	}
	
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
	
	@PutMapping("/api/v2/members/{id}")
	public UpdateMemberResponse updateMemberv1(@PathVariable("id") Long id, @RequestBody @Valid  UpdateMemberRequest umr) {
		
		ms.update(id,umr.getName());
		System.out.println();
		Member finOne = ms.finOne(id);
		return new UpdateMemberResponse(finOne.getId(),finOne.getName());
	}
	
	@Data
	@AllArgsConstructor
	static class Result<T>{
		private int count;
		private T data;
	}
	
	@Data
	@AllArgsConstructor
	static class MemberDto{
		private String name;
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
	
	@Data
	@AllArgsConstructor
	static class UpdateMemberResponse {
		private Long id;
		private String name;
	}
	
	@Data
	static class UpdateMemberRequest{
		private String name;
		
	}
}
