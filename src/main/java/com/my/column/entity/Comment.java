package com.my.column.entity;

public class Comment {
    public String  comment_id;
    public String film_id;
    public String user_id;

    public int star;

    public Comment(String comment_id,String film_id, String user_id, int star) {
        this.comment_id=comment_id;
        this.film_id = film_id;
        this.user_id = user_id;
        this.star = star;
    }

}
