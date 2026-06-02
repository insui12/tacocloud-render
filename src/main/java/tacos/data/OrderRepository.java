package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.TacoOrder;

/**
 * 실습과제 25 — Spring Data MongoDB 버전. ID 타입은 String.
 * tacos 자식 리스트는 임베디드 도큐먼트 배열로 함께 저장된다.
 */
public interface OrderRepository extends CrudRepository<TacoOrder, String> {
}
