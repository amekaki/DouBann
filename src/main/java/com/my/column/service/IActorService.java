package com.my.column.service;

import com.my.column.entity.Actor;

import java.util.List;

public interface IActorService {
	Actor getActorById(String actor_id);
	List<Actor> getActorUnLucene();
	List<Actor> getActorByFilm(String film_id);
	void updateActorLucene(String actor_id);
}
