package com.devpro.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecureConf extends WebSecurityConfigurerAdapter {

	@Autowired private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests() // thực hiện xác thực request ngưười dùng gửi lên.
			
			// không thực hiện xác thực đối với các url này.
            .antMatchers("/css/**", "/js/**", "/images/**", "/summernote/**", "/file/upload/**","/vendors/**").permitAll()
            
            // thực hiện xác thực với các url kiểu ..../admin/....
//            .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
            .antMatchers("/adminss/**").authenticated()
            
            .and() // kết hợp với điều kiện.
            
            // khi click vào button logout thì không cần login.
            // khi click vào button này thì dữ liệu user trên session sẽ bị xoá.
            .logout()
            .logoutUrl("/cai-nay-la-thuoc-tinh-href-trong-the-a-logout")
            .logoutSuccessUrl("/admin/login")
            .invalidateHttpSession(true) // xoá hết dữ liệu trên seesion
            .deleteCookies("JSESSIONID") // xoá hết dữ liệu trên cokies.
            .permitAll()
            
            .and() // kết hợp với điều kiện.
            
            .formLogin() // thực hiện xác thực qua form(username và password)
            .loginPage("/admin/login") // trang login do mình thiết kế, trỏ vào request-mapping trong controller.
            .loginProcessingUrl("/cai-nay-la-thuoc-tinh-action-trong-form-login") // link action for form post.
            .defaultSuccessUrl("/adminss", true) // when user success authenticated then go to this url.
            .failureUrl("/cai-nay-la-mapping-trong-adminlogincontroller?co_loi_xay_ra=true") // nhập username, password sai thì redirect về trang nào.
            .permitAll();
	}
	
	/**
	 * Spring-Secure sẽ sử dụng Encoder này để mã hoá mật khẩu người dùng nhập từ trang login.
	 * Sau khi mã hoá xong sẽ dùng chuỗi đã được Encoder và so sánh xem có khớp với dữ liệu
	 * trong database không.
	 * @return
	 */
	@Bean public PasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
		return encoder;
    }
	
	@Autowired public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
}
