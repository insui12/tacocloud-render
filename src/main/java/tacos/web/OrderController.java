package tacos.web;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.extern.slf4j.Slf4j;
import tacos.TacoOrder;
import tacos.User;
import tacos.data.OrderRepository;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {

    private final OrderRepository orderRepo;

    @Autowired
    public OrderController(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    /**
     * 실습과제 25-2: /orders/current 로 바로 진입했을 때도 인증된 사용자 정보로
     * 배송지를 pre-fill 한다 (강의 슬라이드 11).
     */
    @ModelAttribute("tacoOrder")
    public TacoOrder order(@AuthenticationPrincipal User user) {
        TacoOrder order = new TacoOrder();
        if (user != null) {
            order.setDeliveryName(user.getFullname());
            order.setDeliveryStreet(user.getStreet());
            order.setDeliveryCity(user.getCity());
            order.setDeliveryState(user.getState());
            order.setDeliveryZip(user.getZip());
        }
        return order;
    }

    @GetMapping("/current")
    public String orderForm() {
        return "orderForm";
    }

    /**
     * 실습과제 25-2: @AuthenticationPrincipal 로 현재 인증된 User 를 주입받고,
     * TacoOrder.user 필드에 set 하여 어떤 사용자가 어떤 주문을 만들었는지 영속화한다.
     */
    @PostMapping
    public String processOrder(@Valid @ModelAttribute TacoOrder tacoOrder,
                               Errors errors,
                               SessionStatus sessionStatus,
                               @AuthenticationPrincipal User user) {
        if (errors.hasErrors()) {
            return "orderForm";
        }
        tacoOrder.setUser(user);
        TacoOrder saved = orderRepo.save(tacoOrder);
        log.info("Order saved (id={}, user={}) — tacos={}",
                saved.getId(),
                user != null ? user.getUsername() : "anonymous",
                saved.getTacos() == null ? 0 : saved.getTacos().size());
        sessionStatus.setComplete();
        return "redirect:/";
    }
}
