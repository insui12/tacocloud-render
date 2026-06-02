package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.User;

/**
 * 실습과제 25 — Spring Data MongoDB 버전.
 * 강의 슬라이드 7 의 지시: ID 타입을 Long → String 으로 변경.
 */
public interface UserRepository extends CrudRepository<User, String> {

    User findByUsername(String username);
}
