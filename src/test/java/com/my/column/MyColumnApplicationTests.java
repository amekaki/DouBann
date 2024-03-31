package com.my.column;

import com.my.column.entity.Film;
import com.my.column.service.ArticleService;
import com.my.column.service.SearchService;
import com.my.column.service.impl.FilmServiceImpl;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@MapperScan("com.my.column.dao")
class MyColumnApplicationTests {

	@Autowired
	private  FilmServiceImpl filmService;

	@Test
	void contextLoads() {
		filmService.generateMatrix();
		System.out.println("SUCCESS");
	}

}
