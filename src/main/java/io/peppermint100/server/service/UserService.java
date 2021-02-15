package io.peppermint100.server.service;

import io.peppermint100.server.entity.CustomUserDetails;
import io.peppermint100.server.entity.Request.User.LoginRequest;
import io.peppermint100.server.entity.Request.User.SignUpRequest;
import io.peppermint100.server.entity.Response.User.UserInfo;
import io.peppermint100.server.entity.User;
import io.peppermint100.server.exception.EmptyValueExistException;
import io.peppermint100.server.exception.User.LoginFailException;
import io.peppermint100.server.exception.User.PasswordNotMatchException;
import io.peppermint100.server.exception.User.UserAlreadyExistException;
import io.peppermint100.server.exception.User.UserNotExistException;
import io.peppermint100.server.repository.UserRepository;
import io.peppermint100.server.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private CustomUserDetailsService userDetailsService;

    public void signUp(SignUpRequest signUpRequest) {
        String email = Optional.ofNullable(signUpRequest.getEmail()).orElseThrow(EmptyValueExistException::new);
        String password = Optional.ofNullable(signUpRequest.getPassword()).orElseThrow(EmptyValueExistException::new);
        String confirmPassword = Optional.ofNullable(signUpRequest.getConfirmPassword()).orElseThrow(EmptyValueExistException::new);
        String firstName = Optional.ofNullable(signUpRequest.getFirstName()).orElseThrow(EmptyValueExistException::new);
        String lastName = Optional.ofNullable(signUpRequest.getLastName()).orElseThrow(EmptyValueExistException::new);
        String address = Optional.ofNullable(signUpRequest.getAddress()).orElseThrow(EmptyValueExistException::new);

        boolean alreadyExist = userRepository.existsByEmail(email);

        if(alreadyExist){
            throw new UserAlreadyExistException();
        }

        if(!password.equals(confirmPassword)){
            throw new PasswordNotMatchException();
        }

        String encodedPassword = passwordEncoder.encode(password);

        User newUser = new User(
                email,
                firstName,
                lastName,
                encodedPassword,
                address
        );

        userRepository.save(newUser);
    }

    public String loginAndGenerateToken(LoginRequest loginRequest) throws Exception {
        String email = Optional.ofNullable(loginRequest.getEmail()).orElseThrow(EmptyValueExistException::new);
        String password = Optional.ofNullable(loginRequest.getPassword()).orElseThrow(EmptyValueExistException::new);

        Optional<User> user = userRepository.findByEmail(email);

        if(!user.isPresent()){
            throw new UserNotExistException();
        }

        try{
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
            ));
        }catch(Exception e){
            throw new LoginFailException();
        }

        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(user.get().getEmail());

        String token = jwtUtil.generateToken(email);

        return token;
    }

    public UserInfo me(String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String email = userDetails.getEmail();

        String parsedToken = token.substring(7);

        boolean isValidateToken = jwtUtil.validateToken(parsedToken, email);

        if (!isValidateToken) {
            throw new UserNotExistException();
        }

        UserInfo userInfo = null;

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()){
            throw new UserNotExistException();
        }

        userInfo = new UserInfo(
                user.get().getUserId(),
                user.get().getEmail(),
                user.get().getFirstName(),
                user.get().getLastName(),
                user.get().getAddress()
        );

        return userInfo;
    }
}
