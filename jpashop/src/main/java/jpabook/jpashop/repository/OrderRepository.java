package jpabook.jpashop.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.api.OrderSimpleApiController;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.QDelivery;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QOrder;
import jpabook.jpashop.domain.QOrderItem;
import jpabook.jpashop.domain.item.QItem;
import jpabook.jpashop.repository.OrderDto.OrderItemDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
	private final EntityManager em;
	
	@Autowired JPAQueryFactory queryFactory;
	
	private QOrder qOrder = QOrder.order;
	
	private QMember qMember = QMember.member;
	
	private QDelivery qDelivery = QDelivery.delivery;
	
	private QOrderItem qOrderItem = QOrderItem.orderItem;
	
	private QItem qItem = QItem.item;
	

	public void save(Order order) {
		em.persist(order);
	}

	public Order findOne(Long id) {
		return em.find(Order.class, id);
	}

	// CREATEOQUERY 방식
//	public List<Order> findAll(OrderSearch orderSearch){
//		return em.createQuery("select o from order join o.member m", Order.class)
//						.setParameter("status", orderSearch.getOrderStatus())
//						.setParameter("name", orderSearch.getMemberName())
//						.setMaxResults(1000)
//						.getResultList();
//		
//	}
//	public List<Order> findAllCretia(OrderSearch orderSearch) {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
//		Root<Order> o = cq.from(Order.class);
//		Join<Order, Member> m = o.join("member", JoinType.INNER); // 회원과 조인
//		List<Predicate> criteria = new ArrayList<>();
//		// 주문 상태 검색
//		if (orderSearch.getOrderStatus() != null) {
//			Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
//			criteria.add(status);
//		}
//		// 회원 이름 검색
//		if (StringUtils.hasText(orderSearch.getMemberName())) {
//			Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
//			criteria.add(name);
//		}
//		cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
//		TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); // 최대
//		return query.getResultList();
//	}
	//jpa.properties.hibernate.default_batch_fetch_size=100 <----프로퍼티나 yml에 해당 설정을 해두면
	//100개까지는 in절로 셀렉트 한다.
	public List<Order> findAllqueryDsl(OrderSearch os){
		return queryFactory.selectFrom(qOrder)
								.leftJoin(qOrder.member , qMember).fetchJoin()
								.leftJoin(qOrder.delivery , qDelivery).fetchJoin()
							.where(qName(os),qStatus(os))
							.orderBy(qOrder.orderDate.desc())
							.fetch();
												
	}
	
	public List<SimpleOrderDto> findAllqueryDslWithDto(OrderSearch os){
		return queryFactory.select(Projections.constructor(SimpleOrderDto.class
				, qOrder.id, qOrder.member.name, qOrder.orderDate, qOrder.orderStatus, qOrder.delivery.address))
							.from(qOrder)
								.leftJoin(qOrder.member , qMember)
								.leftJoin(qOrder.delivery , qDelivery)
							.where(qName(os),qStatus(os))
							.orderBy(qOrder.orderDate.desc())
							.fetch();
												
	}
	
	// 1대N의 경우 fetch 조인을 해도 결과값이 증가된다.
	// 이 경우 모든 결과를 메모리에 올리고 시작하기에 메모리 오버플로우의 위험
	// 또한 페이징 처리가 불가능(중복적으로 나온 데이터의 문제로
	// distinct를 쓸 경우 그 결과값은 원하는 만큼 줄일 수 있지만 역시 메모리 문제 + 페이징 해결은 힘듬
	// N대1 혹은 1대1은 fetch join을 사용해도 괜찮다.
	//jpa.properties.hibernate.default_batch_fetch_size=100 <----프로퍼티나 yml에 해당 설정을 해두면
	//100개까지는 in절로 셀렉트 한다.
	public List<Order> findAllqueryDslWithItem(OrderSearch os) {
		
		return queryFactory.selectFrom(qOrder).distinct()
				.leftJoin(qOrder.member , qMember).fetchJoin()
				.leftJoin(qOrder.delivery , qDelivery).fetchJoin()
				.leftJoin(qOrder.orderitems , qOrderItem).fetchJoin()
				.leftJoin(qOrderItem.item , qItem).fetchJoin()
			.where(qName(os),qStatus(os))
			.orderBy(qOrder.orderDate.desc())
			.fetch();
	}
	
	// N대1 1대1의 경우는 최적화가 어렵지 않기에 조인으로 뺴옴
	// 1대N 혹은 N대N의 경우 조회 ROW수가 변하기 때문에 별로의 메소드로 조회
	public List<OrderDto> findAllqueryDslWithDto2(OrderSearch os){
		 List<OrderDto> fetch = queryFactory.select(Projections.constructor(OrderDto.class
				, qOrder.id, qOrder.member.name, qOrder.orderDate, qOrder.orderStatus, qOrder.delivery.address))
							.from(qOrder)
								.leftJoin(qOrder.member , qMember)
								.leftJoin(qOrder.delivery , qDelivery)
							.where(qName(os),qStatus(os))
							.orderBy(qOrder.orderDate.desc())
							.fetch();
		 

		 fetch.forEach(o -> {
				o.setOrderItems(findItems(o.getId()));
		 });
			
		return fetch;										
	}
	
	private List<OrderItemDto> findItems(Long id){
		return queryFactory.select(Projections.constructor(OrderItemDto.class 
							,qOrderItem.order.id , qOrderItem.item.name , qOrderItem.orderPrice , qOrderItem.count))
							.from(qOrderItem)
							.where(qOrderItem.order.id.eq(id))
							.fetch();
	}
	
	public List<Order> findAllWithMemberDelivery() {
		return em.createQuery("select o from Order o"+	
						" join fetch o.member m"+ 
						" join fetch o.delivery d", Order.class).getResultList();
	}
	
	
	// 1대N의 경우 fetch 조인을 해도 결과값이 증가된다.
	// 이 경우 모든 결과를 메모리에 올리고 시작하기에 메모리 오버플로우의 위험
	// 또한 페이징 처리가 불가능(중복적으로 나온 데이터의 문제로
	// distinct를 쓸 경우 그 결과값은 원하는 만큼 줄일 수 있지만 역시 메모리 문제 + 페이징 해결은 힘듬
	// N대1 혹은 1대1은 fetch join을 사용해도 괜찮다.
	public List<Order> findAllWithItem(OrderSearch os) {
		
		return em.createQuery("select distinct o from Order o"+ 
							" join fetch o.member m"+
							" join fetch o.delivery d"+
							" join fetch o.orderitems oi"+
							" join fetch oi.item i", Order.class).getResultList();
	}
	

	public List<SimpleOrderDto> findOrderDto() {
		return em.createQuery("select new jpabook.jpashop.repository.SimpleOrderDto(o.id , m.name , o.orderDate , o.orderStatus , d.address)"+
				" from Order o"+	
				" join o.member m"+ 
				" join o.delivery d", SimpleOrderDto.class).getResultList();
	}

	
	//동적쿼리용 메소드 START
	//이름
	private BooleanExpression qName(OrderSearch os) {
		if (StringUtils.isNullOrEmpty(os.getMemberName())) {
			return null;
		}
		return qOrder.member.name.contains(os.getMemberName());
	}
	
	private BooleanExpression qStatus(OrderSearch os) {
		if (Objects.isNull(os.getOrderStatus())) {
			return null;
		}
		return qOrder.orderStatus.eq(os.getOrderStatus());
	}

	


}
