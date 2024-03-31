package com.my.column.dao;

import com.my.column.entity.FilmAccessRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FilmAccessRecordMapper {
 public void insertFilmAccessRecord(FilmAccessRecord film);
 public List<Map<String, Long>> getFilmFrequencyInLast20Hours();
}
