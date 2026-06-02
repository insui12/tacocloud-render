package tacos;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 실습과제 25 — MongoDB 도큐먼트로 영속화하는 User.
 *
 *   ▪ @Entity → @Document 로 변경                                (강의 슬라이드 3)
 *   ▪ Long id → String id (MongoDB ObjectId 와 매핑)              (강의 슬라이드 7)
 *   ▪ 모든 필드의 final 제거                                       (강의 슬라이드 4)
 *      → MongoDB 가 Document 를 deserialize 할 때 reflection 으로 필드를
 *        채워야 하므로 final 이면 Java 21 환경에서 login 시
 *        PasswordEncoder.matches 가 false 가 되는 등의 미묘한 실패가 발생한다.
 *   ▪ Spring Security 의 UserDetails 구현은 그대로 유지.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document       // 컬렉션 이름은 기본적으로 "user"
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String username;
    private String password;
    private String fullname;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired()      { return true; }
    @Override
    public boolean isAccountNonLocked()       { return true; }
    @Override
    public boolean isCredentialsNonExpired()  { return true; }
    @Override
    public boolean isEnabled()                { return true; }
}
