package jpabook.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/*
 * XToOne (ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
*/
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
	private final OrderService os;
	private final OrderRepository or;
	
	@GetMapping("/api/v1/simple-orders")
	public List<Order> ordersV1(){
		List<Order> findAllqueryDsl = or.findAllqueryDsl(new OrderSearch());
		for (Order order : findAllqueryDsl) {
			order.getMember().getName();
			order.getDelivery().getAddress();
		}
		
		return findAllqueryDsl;
	}
	
	@GetMapping("/api/v2/simple-orders")
	public List<SimpleOrderdto> ordersV2(){
		List<Order> findAllqueryDsl = or.findAllqueryDsl(new OrderSearch());
		/*
		 * order 2개 
		 * n+1 공식 -> order 1 + N(멤버, 주문) 
		 * 보내는 쿼리가 계속 증가한다
		*/
		List<SimpleOrderdto> collect = findAllqueryDsl.stream()
				.map(o -> new SimpleOrderdto(o))
				.collect(Collectors.toList());
		
		return collect;
	}
	
	@Data
	static class SimpleOrderdto{
		
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
		
		public SimpleOrderdto(Order o) {
			orderId = o.getId();
			name = o.getMember().getName();
			orderDate = o.getOrderDate();
			orderStatus = o.getOrderStatus();
			address = o.getDelivery().getAddress();
		}
	}
	
	
}
