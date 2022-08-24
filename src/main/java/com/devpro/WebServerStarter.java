package com.devpro;

import com.devpro.entities.ERole;
import com.devpro.entities.Role;
import com.devpro.entities.User;
import com.devpro.repositories.RoleRepository;
import com.devpro.repositories.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

/**
 * Entrypoint cho Spring-Boot(Là class phải chứa @SpringBootApplication)
 * 
 * -- Các bước tích hợp một module(web, jpa, security) vào spring-boot project:
 * ----- 1: Sử dụng starter(thêm dependency vào maven - pom.xml)
 * ----- 2: Cấu hình cho module đó
 */
@SpringBootApplication
public class WebServerStarter {
	@Autowired
	private static UserRepository userRepository;

	@Autowired
	private static RoleRepository roleRepository;

	public static  void createUser() throws NotFoundException {
		User user = new User();
		user.setEmail("admin@gmail.com");
		user.setUsername("admin");
		user.setPassword("admin");
		user.setActive(true);
		Set<Role> roles = new HashSet<>();

		Role adminRole = null;
		try {
			adminRole = roleRepository.findByName(ERole.ADMIN)
					.orElseThrow(() -> new NotFoundException("Error: Role is not found"));
		}catch (Exception err) {
			System.out.println("Role: " + err);
		}



		roles.add(adminRole);

		user.setRoles(roles);

		userRepository.save(user);
	}

	public static void main(String[] args) throws NotFoundException {

		SpringApplication.run(WebServerStarter.class, args);

		createUser();
	}
}
