package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.DeliveryStatus;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository or;
	
	private final MemberRepository mr; 
	
	private final ItemRepository ir;
	
	//주문
	@Transactional
	public Long order(Long memberId, Long itemId, int count) {
		//회원 엔티티 조회
		Member member = mr.findOne(memberId);
		Item item = ir.findOne(itemId);
		
		//배송정보조회
		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());
		delivery.setStatus(DeliveryStatus.READY);
		//주문상품 생성
		OrderItem oi = OrderItem.createOrderItem(item, item.getPrice(), count);
		//주문 생성
		Order order = Order.createOrder(member, delivery, oi);
		
		//주문 저장
		or.save(order);
		
		return order.getId();
	}
	
	//주문취소 
	@Transactional
	public void cancelOrder(Long orderId) {
		Order order = or.findOne(orderId);
		order.cancel();
	}
	
	//주문검색
	public List<Order> findOrders(OrderSearch orderSearch){
		return or.findAllqueryDsl(orderSearch);
	}
}
