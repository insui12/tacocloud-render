package tacos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.CreditCardNumber;

import lombok.Data;

/**
 * 실습과제 25 — MongoDB 도큐먼트.
 * 25-2 의 "Knowing your user" 요건에 따라 User 참조 필드(@DBRef) 를 둔다.
 */
@Data
@Document
public class TacoOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private Date placedAt = new Date();

    @NotBlank(message = "Delivery name is required")
    private String deliveryName;

    @NotBlank(message = "Street is required")
    private String deliveryStreet;

    @NotBlank(message = "City is required")
    private String deliveryCity;

    @NotBlank(message = "State is required")
    private String deliveryState;

    @NotBlank(message = "Zip code is required")
    private String deliveryZip;

    @CreditCardNumber(message = "Not a valid credit card number")
    private String ccNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$",
            message = "Must be formatted MM/YY")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;

    // 임베디드 도큐먼트 배열 — JPA 의 @OneToMany cascade=ALL 과 동일한 효과
    private List<Taco> tacos = new ArrayList<>();

    // 25-2: 주문을 만든 사용자 참조. @DBRef 로 user 컬렉션의 _id 를 가리키는 외부 참조 보관.
    @DBRef
    private User user;

    public void addTaco(Taco taco) {
        this.tacos.add(taco);
    }
}
