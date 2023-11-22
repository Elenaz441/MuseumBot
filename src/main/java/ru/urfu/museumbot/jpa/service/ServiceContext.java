package ru.urfu.museumbot.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сборник сервисов
 */
@Service
public class ServiceContext {

    private final EventService eventService;
    private final ReviewService reviewService;
    private final UserService userService;

    @Autowired
    public ServiceContext(EventService eventService,
                          ReviewService reviewService,
                          UserService userService) {
        this.eventService = eventService;
        this.reviewService = reviewService;
        this.userService = userService;
    }

    public EventService getEventService() {
        return eventService;
    }

    public ReviewService getReviewService() {
        return reviewService;
    }

    public UserService getUserService() {
        return userService;
    }
}
