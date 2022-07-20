package jpabook.jpashop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {
	private final ItemService is;
	
	@GetMapping("/items/new")
	public String createForm(Model model) {
		model.addAttribute("form", new BookForm());
		return "items/createItemForm";
	}
	
	@PostMapping("/items/new")
	public String create(BookForm book) {
		Book books = new Book();
		books.setName(book.getName());
		books.setAuthor(book.getAuthor());
		books.setIsbn(book.getIsbn());
		books.setPrice(book.getPrice());
		books.setStockQuantity(book.getStockQuantity());
		
		is.saveItem(books);
		
		return "redirect:/items";
	}
	
	@GetMapping("/items")
	public String items(Model model) {
		List<Item> items = is.findItem();
		model.addAttribute("items", items);
		return "items/itemList";
	}
	
	@GetMapping("/items/{itemId}/edit")
	public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
		Book book =  (Book) is.findOne(itemId);
		
		BookForm books = new BookForm();
		books.setId(book.getId());
		books.setName(book.getName());
		books.setPrice(book.getPrice());
		books.setAuthor(book.getAuthor());
		books.setIsbn(book.getIsbn());
		books.setStockQuantity(book.getStockQuantity());
		
		model.addAttribute("form", books);
		
		return "items/updateItemForm";
	}
	
	@PostMapping("/items/{itemId}/edit")
	public String updateItem(@PathVariable("itemId") Long itemId,@ModelAttribute("form")BookForm form, Model model) {
		//Long itemId, String name, String author, String isbn, int price, int stockQuantity
		
		is.updateItem(itemId, form.getName(), form.getAuthor() , form.getIsbn(), form.getPrice(), form.getStockQuantity());
		
		return "redirect:/items";
	}
}
