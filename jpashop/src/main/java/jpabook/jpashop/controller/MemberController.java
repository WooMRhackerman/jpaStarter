package jpabook.jpashop.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {

	private final MemberService ms;
	
	@GetMapping("/members/new")
	public String createForm(Model model) {
		model.addAttribute("memberForm", new MemberForm());
		
		return "member/createMemberForm";
	}
	
	@PostMapping("/members/new")
	public String create(@Valid MemberForm form, BindingResult result) {
		if (result.hasErrors()) {
			return "member/createMemberForm";
		}
	    Address address = new Address(form.getCity() , form.getStreet(), form.getZipcode());
		Member member = new Member();
		member.setName(form.getName());
		member.setAddress(address);
		
		ms.join(member);
		return "redirect:/";
	}
	
	@GetMapping("/members")
	public String members(Model model) {
		List<Member> members = ms.findMembers();
		model.addAttribute("members", members);
		
		return "member/memberList";
	}
}
