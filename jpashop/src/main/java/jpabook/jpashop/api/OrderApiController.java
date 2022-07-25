package jpabook.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.OrderService;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
	
	private final OrderRepository or;
	private final OrderService os;
	
	@GetMapping("/api/v1/orders")
	public List<Order> ordersV1(){
		List<Order> findAllqueryDsl = or.findAllqueryDsl(new OrderSearch());
		for (Order order : findAllqueryDsl) {
			order.getMember().getName();
			order.getDelivery().getAddress();
			
			List<OrderItem> orderitems = order.getOrderitems();
			orderitems.stream().forEach(o -> o.getItem().getName());
		}
		
		return findAllqueryDsl;
	}
	
	@GetMapping("/api/v2/orders")
	public List<OrderDto> ordersV2(){
		List<Order> findAllqueryDsl = or.findAllqueryDsl(new OrderSearch());
		List<OrderDto> orderDto = findAllqueryDsl.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
		
		return orderDto;
	}
	
	@GetMapping("/api/v3/orders")
	public List<OrderDto> ordersV3(){
		List<Order> findAllqueryDsl = or.findAllqueryDslWithItem(new OrderSearch());
		List<OrderDto> orderDto = findAllqueryDsl.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
		
		return orderDto;
	}
	
	@GetMapping("/api/v3.1/orders")
	public List<OrderDto> ordersV3_page(){
		List<Order> findAllqueryDsl = or.findAllqueryDsl(new OrderSearch());
		List<OrderDto> orderDto = findAllqueryDsl.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
		
		return orderDto;
	}
	
	@GetMapping("/api/v4/orders")
	public List<jpabook.jpashop.repository.OrderDto> ordersV4(){
		List<jpabook.jpashop.repository.OrderDto> findAllqueryDsl = or.findAllqueryDslWithDto2(new OrderSearch());
		//List<jpabook.jpashop.repository.OrderDto> orderDto = findAllqueryDsl.stream().map(o -> new jpabook.jpashop.repository.OrderDto(o)).collect(Collectors.toList());
		
		return findAllqueryDsl;
	}
	
	@Data
	static class OrderDto{
		
		private Long id;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
		private List<OrderItemDto> orderItem;
		
		public OrderDto(Order o) {
			id = o.getId();
			name = o.getMember().getName();
			orderDate = o.getOrderDate();
			orderStatus = o.getOrderStatus();
			address = o.getDelivery().getAddress();
			orderItem = o.getOrderitems().stream().map(i -> new OrderItemDto(i))
							.collect(Collectors.toList());
		}
	}
	
	@Data
	static class OrderItemDto{
		
		private String itemName;
		private int orderPrice;
		private int count;
		
		public OrderItemDto(OrderItem i) {
			itemName = i.getItem().getName();
			orderPrice = i.getOrderPrice();
			count = i.getCount();
		}
		
	}
}
