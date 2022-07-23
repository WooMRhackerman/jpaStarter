package jpabook.jpashop;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module.Feature;
import com.querydsl.jpa.Hibernate5Templates;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Configuration
public class BeanConfig {
	
	@PersistenceContext
	private EntityManager em;
	
	@Bean
	public JPAQueryFactory queryFactory() {
		return new JPAQueryFactory(em);
	}
	
	@Bean
	public Hibernate5Module hibernate5Module() {
		Hibernate5Module h5m = new Hibernate5Module();
		//h5m.configure(Feature.FORCE_LAZY_LOADING, true);
		return h5m;
	}
}
