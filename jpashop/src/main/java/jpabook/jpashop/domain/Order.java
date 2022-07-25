package jpabook.jpashop.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="orders")
@Getter @Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
	@Id @GeneratedValue
	@Column(name="order_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member_id")
	private Member member;
	
	@BatchSize(size=10000)
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderitems = new ArrayList<>();
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="delivery_id")
	private Delivery delivery;
	
	private LocalDateTime orderDate; // 주문시간
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus; // 주문상태 [ORDER, CANCEL]
	
	//== 연관관계 메서드==//
	public void setMember(Member member) {
		this.member = member;
		member.getOrders().add(this);
	}
	
	public void addOrderItem(OrderItem orderItem) {
		orderitems.add(orderItem);
		orderItem.setOrder(this);
	}
	
	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
		delivery.setOrder(this);
	}
	
	//== 생성메서드 ==//
	public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItem) {
		Order order = new Order();
		order.setMember(member);
		order.setDelivery(delivery);
		for (OrderItem orderItem2 : orderItem) {
			order.addOrderItem(orderItem2);
		}
		order.setOrderStatus(OrderStatus.ORDER);
		order.setOrderDate(LocalDateTime.now());
		
		return order;
	}
	
	//==비즈니스 로직==//
	/*
	 * 주문취소
	*/
	public void cancel() {
		if (delivery.getStatus() == DeliveryStatus.COMP) {
			throw new IllegalStateException("이미 배송이 완료된 상품은 취소가 불가능합니다.");
		}
		
		this.setOrderStatus(OrderStatus.CANCEL);
		for (OrderItem orderItem : orderitems) {
			orderItem.cancel();
		}
	}
	
	//== 조회로직 ==//
	/*
	 * 전체주문가격
	*/
	public int getTotalPrice() {
		int totalPrice = 0;
		for (OrderItem orderItem : orderitems) {
			totalPrice += orderItem.getTotalPrice();
		}
		return totalPrice;
	}
	
}
