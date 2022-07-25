package jpabook.jpashop.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

@Data
public class OrderDto {
	
	private Long id;
	private String name;
	private LocalDateTime orderDate;
	private OrderStatus orderStatus;
	private Address address;
	private List<OrderItemDto> orderItems;
	
	public OrderDto(Long id, String name,LocalDateTime orderDate, OrderStatus orderStatus,  Address address) {
		this.id = id;
		this.name = name;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.address = address;
		//orderItems = orderItem.stream().map(i -> new OrderItemDto(i)).collect(Collectors.toList());
	}
	@Data
	public static class OrderItemDto{
		private Long orderId;
		private String itemName;
		private int orderPrice;
		private int count;
		
		public OrderItemDto(Long id, String name, int orderPrice, int count) {
			this.orderId = id;
			this.itemName = name;
			this.orderPrice = orderPrice;
			this.count = count;
		}
		
	}
}