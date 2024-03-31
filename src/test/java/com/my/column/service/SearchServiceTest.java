package com.my.column.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.my.column.service.SearchService;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@MapperScan("com.my.column.dao")
@SpringBootTest
class SearchServiceTest {
    @Autowired IFilmService ifilmService;
    @Test
    void addFilm() {
        ifilmService.getFilmLucene();
    }

    @Test
    void searchActor() {
        SearchService.searchActor("流感",0,20);
    }
}