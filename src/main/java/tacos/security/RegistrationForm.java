package tacos.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;
import tacos.User;

/**
 * 실습과제 23-2 — 등록 폼이 바인딩되는 DTO.
 * toUser() 가 PasswordEncoder 를 받아 평문 비밀번호를 BCrypt 해시로 한 번에 변환하여
 * User 도메인 엔티티를 만든다.
 */
@Data
public class RegistrationForm {

    private String username;
    private String password;
    private String fullname;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;

    public User toUser(PasswordEncoder passwordEncoder) {
        // 실습과제 25: User 의 final 제거로 인해 @AllArgsConstructor 가 id 까지 받는다.
        // 신규 가입 시 id 는 MongoDB 가 자동 채번하므로 null 로 둔다.
        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(password));
        u.setFullname(fullname);
        u.setStreet(street);
        u.setCity(city);
        u.setState(state);
        u.setZip(zip);
        u.setPhoneNumber(phone);
        return u;
    }
}
