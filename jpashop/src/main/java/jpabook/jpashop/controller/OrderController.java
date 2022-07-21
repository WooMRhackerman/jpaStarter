package jpabook.jpashop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {
	private final OrderService os;
	private final MemberService ms;
	private final ItemService is;
	
	@GetMapping("/order")
	public String createForm(Model model) {
		List<Member> members = ms.findMembers();
		List<Item> items = is.findItem();
		
		model.addAttribute("members", members);
		model.addAttribute("items", items);
		
		return "order/orderForm";
	}
	
	@PostMapping("/order")
	public String order(@RequestParam("memberId") Long memberId,@RequestParam("itemId") Long itemId,
						@RequestParam("count") int count) {
		os.order(memberId, itemId, count);
		
		return "redirect:/orders";
	}
	
	@GetMapping("orders")
	public String orders(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
		List<Order> findOrders = os.findOrders(orderSearch);
		model.addAttribute("orders", findOrders);
		return "order/orderList";
	}
	
	@PostMapping("/orders/{orderId}/cancel")
	public String cancel(@PathVariable("orderId") Long id) {
		os.cancelOrder(id);
		
		return "redirect:/orders";
	}
}
