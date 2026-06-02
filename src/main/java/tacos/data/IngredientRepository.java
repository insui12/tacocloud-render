package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.Ingredient;

/**
 * Spring Data JPA 가 런타임에 구현체를 자동 생성한다.
 * CrudRepository<엔티티, ID 타입> 만 확장하면 findAll/findById/save/deleteById 등
 * 기본 CRUD 메소드가 모두 제공되며, JdbcXxxRepository 처럼 직접 구현 클래스를 작성할 필요가 없다.
 */
public interface IngredientRepository extends CrudRepository<Ingredient, String> {
}
