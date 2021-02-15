package io.peppermint100.server.filter;

import io.peppermint100.server.constant.Util;
import io.peppermint100.server.entity.CustomUserDetails;
import io.peppermint100.server.exception.User.UserNotExistException;
import io.peppermint100.server.service.CustomUserDetailsService;
import io.peppermint100.server.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException, UserNotExistException {
        String header = httpServletRequest.getHeader(Util.AUTHORIZATION);
        String token = null;
        String email = null;

        if (header != null && header.startsWith(Util.BEARER)){
            token = header.substring(7);
            email = jwtUtil.getUserEmail(token);
        }

        // if email is null then do filter without getting any authentication
        // so security configs throw authenticationEntrypoint exception
        // then the handler catches exception then pass the exception to exception controller
        // then @ControllerAdvice notifies
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            if (jwtUtil.validateToken(token, email)){
                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
