package com.my.column.entity;

import java.util.Date;


public class FilmAccessRecord {
    private String film_id;
    private String user_id;
    private Date access_time;

    public FilmAccessRecord(String film_id, String user_id, Date access_time) {
        this.film_id = film_id;
        this.user_id = user_id;
        this.access_time = access_time;
    }

    public String getFilm_id() {
        return film_id;
    }

    public void setFilm_id(String film_id) {
        this.film_id = film_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getAccess_time() {
        return access_time;
    }

    public void setAccess_time(Date access_time) {
        this.access_time = access_time;
    }
}
