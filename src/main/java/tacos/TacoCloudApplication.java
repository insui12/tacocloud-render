package tacos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import tacos.Ingredient.Type;
import tacos.data.IngredientRepository;

@SpringBootApplication
public class TacoCloudApplication {

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("  2022810073 최서진 - Taco Cloud (Spring Data JPA)");
        System.out.println("==============================================");
        SpringApplication.run(TacoCloudApplication.class, args);
    }

    /**
     * 실습과제 21 — data.sql 대신 CommandLineRunner 로 Ingredient 카탈로그 preload.
     * 애플리케이션 컨텍스트 기동이 완료된 직후 한 번 호출된다.
     */
    @Bean
    public CommandLineRunner dataLoader(IngredientRepository repo) {
        return args -> {
            repo.save(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP));
            repo.save(new Ingredient("COTO", "Corn Tortilla",  Type.WRAP));
            repo.save(new Ingredient("GRBF", "Ground Beef",    Type.PROTEIN));
            repo.save(new Ingredient("CARN", "Carnitas",       Type.PROTEIN));
            repo.save(new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES));
            repo.save(new Ingredient("LETC", "Lettuce",        Type.VEGGIES));
            repo.save(new Ingredient("CHED", "Cheddar",        Type.CHEESE));
            repo.save(new Ingredient("JACK", "Monterrey Jack", Type.CHEESE));
            repo.save(new Ingredient("SLSA", "Salsa",          Type.SAUCE));
            repo.save(new Ingredient("SRCR", "Sour Cream",     Type.SAUCE));
        };
    }
}
