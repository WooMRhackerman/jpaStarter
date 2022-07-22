package jpabook.jpashop;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitDb {
	
	private final InitService is;
	
	@PostConstruct
	public void init() {
		is.dbInit1();
		is.dbInit2();
	}
		

	@Component
	@Transactional
	@RequiredArgsConstructor
	static class InitService{
		private final EntityManager em;
		
		public void dbInit1() {
			Member member = new Member();
			member.setName("userA");
			member.setAddress(new Address("서울", "도봉로", "123457"));
			em.persist(member);
			
			Book book = new Book();
			book.setName("JPA1 BOOK");
			book.setPrice(10000);
			book.setStockQuantity(100);
			em.persist(book);
			
			Book book2 = new Book();
			book2.setName("JPA2 BOOK");
			book2.setPrice(10000);
			book2.setStockQuantity(100);
			em.persist(book2);
			
			OrderItem createOrderItem = OrderItem.createOrderItem(book, 10000, 1);
			OrderItem createOrderItem2 = OrderItem.createOrderItem(book2, 20000, 2);
			
			Delivery delivery = new Delivery();
			delivery.setAddress(member.getAddress());
			
			Order createOrder = Order.createOrder(member, delivery, createOrderItem ,createOrderItem2);
			em.persist(createOrder);
			
			
		}
		
		public void dbInit2() {
			Member member = new Member();
			member.setName("userB");
			member.setAddress(new Address("부산", "소동로", "223457"));
			em.persist(member);
			
			Book book = new Book();
			book.setName("SPRING1 BOOK");
			book.setPrice(10000);
			book.setStockQuantity(100);
			em.persist(book);
			
			Book book2 = new Book();
			book2.setName("SPRING2 BOOK");
			book2.setPrice(10000);
			book2.setStockQuantity(100);
			em.persist(book2);
			
			OrderItem createOrderItem = OrderItem.createOrderItem(book, 30000, 3);
			OrderItem createOrderItem2 = OrderItem.createOrderItem(book2, 30000, 3);
			
			Delivery delivery = new Delivery();
			delivery.setAddress(member.getAddress());
			
			Order createOrder = Order.createOrder(member, delivery, createOrderItem ,createOrderItem2);
			em.persist(createOrder);
			
		}
	}
}
