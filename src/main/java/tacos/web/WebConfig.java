package tacos.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/about").setViewName("about");
        // 실습과제 23 — 로그인 폼은 정적 뷰이므로 view controller 만으로 충분하다.
        // Spring Security 의 formLogin().loginPage("/login") 가 이 매핑을 사용한다.
        registry.addViewController("/login").setViewName("login");
    }
}
