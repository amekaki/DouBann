package com.my.column.dao;

import com.my.column.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {
    List<Comment> getAllComment();
}
