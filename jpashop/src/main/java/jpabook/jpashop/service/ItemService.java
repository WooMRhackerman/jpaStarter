package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
	private final ItemRepository ir;
	
	@Transactional //쓰기는 따로 적용
	public void saveItem(Item item) {
		ir.save(item);
	}
	
	public List<Item> findItem(){
		return ir.findAll();
	}
	
	public Item findOne(Long id) {
		return ir.findOne(id);
	}
}
