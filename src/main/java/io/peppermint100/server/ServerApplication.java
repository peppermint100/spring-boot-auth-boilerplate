package io.peppermint100.server;

import io.peppermint100.server.entity.User;
import io.peppermint100.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class ServerApplication {

	@Autowired
	private UserRepository userRepository;

//	@PostConstruct
//	public void initializeUser(){
//		List<User> users = Stream.of(
//			new User(101L, "pepper@naver.com", "In Gyu", "Lee", "123123", "경기도 시흥시 대야동 어딘가"),
//			new User(102L, "iksu@naver.com", "Ik Su", "Jo", "123123", "경기도 부천시 도당동 어딘가"),
//			new User(103L, "hoke@naver.com", "Ho Yong", "Jeon", "123123","강원도 속초시 어딘가")
//		).collect(Collectors.toList());
//
//		userRepository.saveAll(users);
//	}

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
}
