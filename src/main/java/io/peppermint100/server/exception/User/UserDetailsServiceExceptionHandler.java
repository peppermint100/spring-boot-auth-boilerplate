package io.peppermint100.server.exception.User;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserDetailsServiceExceptionHandler {

    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/exception/userdetails");
    }
}
