package tacos.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import tacos.data.UserRepository;

/**
 * 실습과제 23 통합 SecurityConfig.
 *
 *  ▪ 기본 프로파일 (= 실습과제 23-2)
 *    UserRepository 를 lookup 하는 람다형 UserDetailsService 빈을 등록.
 *    /register 로 가입한 사용자가 BCrypt 해시된 패스워드로 /login 한다.
 *
 *  ▪ "inmemory" 프로파일 (= 실습과제 23-1)
 *    --spring.profiles.active=inmemory 로 실행하면 활성화. buzz / woody 두 명을
 *    InMemoryUserDetailsManager 로 띄운다. 강의 슬라이드 12 의 데모와 정확히 일치.
 */
@Configuration
public class SecurityConfig {

    /** PasswordEncoder — 두 프로파일 공용. BCrypt 강력 해시. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ────────────────────────────────────────────────────────────────
    // 23-1: In-memory user details service
    // ────────────────────────────────────────────────────────────────
    @Profile("inmemory")
    @Bean
    public UserDetailsService inMemoryUserDetailsService(PasswordEncoder encoder) {
        List<UserDetails> usersList = Arrays.asList(
                new User("buzz",
                         encoder.encode("infinity"),
                         Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))),
                new User("woody",
                         encoder.encode("bullseye"),
                         Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
        return new InMemoryUserDetailsManager(usersList);
    }

    // ────────────────────────────────────────────────────────────────
    // 23-2: JPA-backed custom user details service
    // ────────────────────────────────────────────────────────────────
    @Profile("!inmemory")
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            tacos.User user = userRepo.findByUsername(username);
            if (user != null) {
                return user;        // tacos.User 가 UserDetails 를 직접 구현하고 있음
            }
            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

    /**
     * 인증/인가 정책.
     *  - /design 및 /orders 는 ROLE_USER 로 인증된 사용자만
     *  - 그 외(/, /register, /login, /h2-console, 정적 리소스) 는 모두 허용
     *  - 폼 로그인 / 폼 로그아웃 + login 성공 시 /design 으로 리다이렉트
     *  - H2 콘솔이 iframe 으로 동작하므로 X-Frame-Options 를 SAMEORIGIN 으로 완화
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/design", "/orders").hasRole("USER")
                        .requestMatchers("/", "/register", "/login",
                                         "/styles.css", "/images/**",
                                         "/h2-console/**").permitAll()
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/design", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll())
                .csrf(csrf -> csrf
                        // H2 콘솔이 CSRF 토큰 없이 form POST 를 하므로 콘솔만 예외
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .build();
    }
}
