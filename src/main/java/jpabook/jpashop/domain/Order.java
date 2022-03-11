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

    @ManyToOne(fetch = FetchType.LAZY) // 한 멤버가 여러 주문을 가지기 때문에 연관관계의 주인은 주문이다.
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // 한 주문이 여러 아이템을 가지기 때문에 연관관계의 주인은 아이템이다.
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 [Order, Cancel]

    // 연관관계 메서드
    public void setMember(Member member) {
        // 멤버를 세팅할 때 양방향으로 매핑되어 있는 orders 까지 한번에 세팅해준다.
        // 이것은 다대일이므로 이렇게 세팅
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        // 마찬가지로 양방향으로 매핑되어 있는 item 까지 한번에 세팅해준다.
        // 이것은 일대다이므로 이렇게 세팅
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
