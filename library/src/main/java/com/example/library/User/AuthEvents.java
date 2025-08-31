package com.example.library.User;


import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class AuthEvents {

    // Lyssnar på lyckad login
    @Component
    public static class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {
        @Override
        public void onApplicationEvent(AuthenticationSuccessEvent event) {
            Authentication auth = event.getAuthentication();
            System.out.println("✅ Inloggad: " + auth.getName());
        }
    }
}

