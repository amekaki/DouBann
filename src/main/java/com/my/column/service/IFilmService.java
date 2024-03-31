package com.my.column.service;

import com.my.column.entity.FilmAccessRecord;
import com.my.column.entity.Film;
import com.my.column.entity.MovieUserMatrix;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IFilmService {
	Film getFilmById(String film_id);
	List<Film> getFilmsByKeyword(String keyword);
	List<Film> getAllFilms();

	public MovieUserMatrix generateMatrix();
	List<Film> getFilmUnLucene();
	void updateFilmLucene(String film_id);
	void getFilmLucene();
	List<Film> buildHot20();
	void insertFilmAccessRecord(String film_id, Long user_id, Date access_time);
}
