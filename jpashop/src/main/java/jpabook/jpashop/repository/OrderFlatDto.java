package jpabook.jpashop.repository;

import java.time.LocalDateTime;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class OrderFlatDto {
	private Long id;
	private String name;
	private LocalDateTime orderDate;
	private OrderStatus orderStatus;
	private Address address;
	
	private String itemName;
	private int orderPrice;
	private int count;
	public OrderFlatDto(Long id, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address,
			String itemName, int orderPrice, int count) {
		super();
		this.id = id;
		this.name = name;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.address = address;
		this.itemName = itemName;
		this.orderPrice = orderPrice;
		this.count = count;
	}
	
}
