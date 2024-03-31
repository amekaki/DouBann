package com.my.column.dao;

import com.my.column.entity.Actor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IActorMapper {
	List<Actor> getActorById(String actor_id);
	List<Actor> getActorUnLucene();
	List<Actor> getActorByFilm(String film_id);
	void updateActorLucene(String actor_id);
}
