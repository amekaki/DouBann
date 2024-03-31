package com.my.column.util;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovieEventSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMovieViewEvent(String movieId, String movieName) {
        String message = movieName + ":" + movieId;
        rabbitTemplate.convertAndSend("movie-view-events", message);
    }
}
