package com.my.column.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface RatingMapper {

    @Select("SELECT user_id, COUNT(*) AS count FROM comment GROUP BY user_id ORDER BY count DESC LIMIT #{limit}")
    List<String> findTopUsers(int limit);

    @Select("SELECT film_id, COUNT(*) AS count FROM comment GROUP BY film_id ORDER BY count DESC LIMIT #{limit}")
    List<String> findTopMovies(int limit);

    @Select("SELECT star FROM comment WHERE user_id = #{userId} AND film_id = #{filmId}")
    String findScoreByUserIdAndFilmId(@Param("userId") String userId, @Param("filmId") String filmId);
}

