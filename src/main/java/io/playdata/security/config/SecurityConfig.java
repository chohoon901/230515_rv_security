package io.playdata.security.config;

import io.playdata.security.model.Account;
import io.playdata.security.repository.AccountRepository;
import io.playdata.security.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); // 해싱
    }


    // The dependencies of some of the beans in the application context form a cycle : 순환참조의 오류.
    // SecurityConfig에서 LoginService 참조중
    // LoginService 에서 passwordEncoder -> SecurityConfig. => 꼬임
//    @Autowired
//    private LoginService loginService;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 로그인 시 로직 처리
//        auth.inMemoryAuthentication()
//                .withUser("username")
//                .password(passwordEncoder.encode("password"))
//                .roles("USER");
        // 지금 내가 사용하는 DB에 있는 유저(Account)를 쓰도록 변경
        auth.userDetailsService(
                username -> {
//                    Account account = loginService.findUserByUsername(username);
                    Account account = accountRepository.findUserByUsername(username);
                    if(account == null){
                        throw new UsernameNotFoundException("해당 유저가 없습니다.");
                    }
                    return User.withUsername(username) // 내가 만든 유저가 아닌 Spring Security의 유저
                            .password(account.getPassword())
                            .roles(account.getRole())
                            .build(); // 위 조건을 만족시키는 User를 만들어서 Session으로 돌려줌.
                }).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login").permitAll() // 로그인 페이지는 모든 사용자에게 허용
                .antMatchers("/join").permitAll() // 가입 페이지는 모든 사용자에게 허용
                .antMatchers("/basic").hasAnyRole("basic","gold") // 가입 페이지는 모든 사용자에게 허용
                .antMatchers("/gold").hasRole("gold") // 가입 페이지는 모든 사용자에게 허용
                .anyRequest().authenticated() // 나머지 요청은 인증이 필요
                .and()
                .formLogin()
                .loginPage("/login") // 커스텀 로그인 페이지 경로
                .defaultSuccessUrl("/home") // 로그인 성공 후 이동할 페이지 경로
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login") // 로그아웃 성공 후 이동할 페이지 경로
                // 로그아웃 요청을 받을 경로
                .logoutUrl("/logout") // post
                // 세션과 쿠키를 어떻게 할 것인가
                .invalidateHttpSession(true) // 세션 비활성화
                .deleteCookies("JSESSIONID") // Java를 통해 생성된 세션 ID의 쿠키 지우기
                .permitAll();
    }
}
