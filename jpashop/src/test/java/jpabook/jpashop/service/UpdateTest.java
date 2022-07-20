package jpabook.jpashop.service;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jpabook.jpashop.domain.item.Book;

@SpringBootTest
class UpdateTest {
	@Autowired EntityManager em;
	@Test
	void test() {
		Book book = em.find(Book.class, 1L);
		
		book.setName("update NAME");
		
		//변경감지 == DIRTY CHECK 끝나면 업데이트문 날림
		
		
	}

}
