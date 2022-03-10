package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne // 한 멤버가 여러 주문을 가지기 때문에 연관관계의 주인은 주문이다.
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order") // 한 주문이 여러 아이템을 가지기 때문에 연관관계의 주인은 아이템이다.
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 [Order, Cancel]
}
