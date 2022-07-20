package jpabook.jpashop.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
	
	private final EntityManager em;
	
	public void save(Item item) {
		if (item.getId()==null) {
			em.persist(item);
		}else {
			//자동으로 영속성 엔티티를 가져와 전체 컬럼을 업데이트 침 영속성 엔티티를 반환함
			//모든 컬럼을 업데이트 쳐버리 때문에 위험함
			em.merge(item);
		}
	}
	
	public Item findOne(Long id) {
		return em.find(Item.class, id);
	}
	
	public List<Item> findAll(){
		return em.createQuery("select i from Item i",Item.class).getResultList();
	}
}
