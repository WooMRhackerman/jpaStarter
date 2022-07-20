package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.item.Book;
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
	
	@Transactional //변경감지를 통한 방법...아이디로 영속성 엔티티를 다시 만들어 변경시키기 영속성 엔티티 반환하면 좋음
	public void updateItem(Long itemId, String name, String author, String isbn, int price, int stockQuantity) {
		Book findItem =  (Book) ir.findOne(itemId);
		findItem.setId(itemId);
		findItem.setName(name);
		findItem.setAuthor(author);
		findItem.setIsbn(isbn);
		findItem.setPrice(price);
		findItem.setStockQuantity(stockQuantity);
	}
	
	public List<Item> findItem(){
		return ir.findAll();
	}
	
	public Item findOne(Long id) {
		return ir.findOne(id);
	}
}
