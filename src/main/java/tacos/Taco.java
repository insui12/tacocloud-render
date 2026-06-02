package tacos;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * 실습과제 25 — Taco 는 TacoOrder.tacos 배열의 임베디드 도큐먼트로 직렬화된다.
 * 별도 컬렉션이 필요 없으므로 @Document 를 붙이지 않는다.
 */
@Data
public class Taco {

    private String id;

    private Date createdAt = new Date();

    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @NotNull
    @Size(min = 1, message = "You must choose at least 1 ingredient")
    private List<Ingredient> ingredients;
}
