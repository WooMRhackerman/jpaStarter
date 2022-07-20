package jpabook.jpashop.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;

@SpringBootTest
@Transactional
@TestPropertySource(properties = "spring.config.location=classpath:application-test.yml" )
class OrderServiceTest {
	
	@Autowired EntityManager em;
	@Autowired OrderService os;
	@Autowired OrderRepository or;
	
	Logger logger = LoggerFactory.getLogger(OrderServiceTest.class);
	
	@Test
	void 상품주문() {
		Member member = createMember();
		Item item = createBook("시골 JPA", 10000, 10); //이름, 가격, 재고
		
		int orderCount = 2;
		Long orderId  = os.order(member.getId(), item.getId(), orderCount);
		Order findOrder = or.findOne(orderId);
		
		assertEquals(OrderStatus.ORDER, findOrder.getOrderStatus(), "상품 주문시 상태는 ORDER");
		assertEquals(1, findOrder.getOrderitems().size(), "주문 상품 숫자는 정확해야 한다.");
		assertEquals(10000 * orderCount, findOrder.getTotalPrice(), "주문 가격은 수량 * 가격.");
		assertEquals(8, item.getStockQuantity(), "재고 숫자는 재고 - 주문수량.");
		
		OrderSearch search = new OrderSearch();
		search.setMemberName(findOrder.getMember().getName());
		search.setOrderStatus(findOrder.getOrderStatus());
		
		List<Order> orders = os.findOrders(search);
		logger.info("orders size is " + orders.size());
		for (Order order : orders) {
			logger.info(order.toString());
		}
	}
	
//	@Test
//	void 상품초과주문() {
//		Member member = createMember();
//		Item item = createBook("시골 JPA", 10000, 10); //이름, 가격, 재고
//		
//		int orderCount = 12;
//		
//		NotEnoughStockException e = assertThrows(NotEnoughStockException.class,() -> os.order(member.getId(), item.getId(), orderCount));
//		assertEquals("재고 부족", e.getMessage());
//	}
//	
//	@Test
//	void 주문취소() {
//		Member member = createMember();
//		Item item = createBook("시골 JPA", 10000, 10); //이름, 가격, 재고
//		
//		int orderCount = 2;
//		Long orderId  = os.order(member.getId(), item.getId(), orderCount);
//		
//		os.cancelOrder(orderId);
//		
//		Order order = or.findOne(orderId);
//		assertEquals(OrderStatus.CANCEL, order.getOrderStatus(),"취소시 주문 상태는 CANCEL");
//		assertEquals(10, item.getStockQuantity(),"취소시 재고 수량 원복");
//	}
//	
//	@Test
//	void 재고수량초과() {
//	}
	
	private Member createMember() {
		 Member member = new Member();
		 member.setName("회원1");
		 member.setAddress(new Address("서울", "강가", "123-123"));
		 em.persist(member);
		 return member;
	}
	private Book createBook(String name, int price, int stockQuantity) {
		 Book book = new Book();
		 book.setName(name);
		 book.setStockQuantity(stockQuantity);
		 book.setPrice(price);
		 em.persist(book);
		 return book;
	}

}
