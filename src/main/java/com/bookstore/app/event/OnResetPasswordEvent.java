package com.bookstore.app.event;

import com.bookstore.app.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnResetPasswordEvent extends ApplicationEvent {
    private User user;
    private String appUrl;

    public OnResetPasswordEvent(User user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }
}
