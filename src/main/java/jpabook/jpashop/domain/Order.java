package jpabook.jpashop.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 서비스에서 생성하는 것을 막기 위해서
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 한 멤버가 여러 주문을 가지기 때문에 연관관계의 주인은 주문이다.
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // 한 주문이 여러 아이템을 가지기 때문에 연관관계의 주인은 아이템이다.
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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

    // 생성 메서드
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) { // ...  연산자는 OrderItem 여러개를 받는다는 뜻
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems) {
            // 먼저 사용자가 OrderItem 들을 생성한 후 여기에서는 주문을 처리한다.
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        System.out.println(order);
        return order;
    }

    // 비즈니스 로직
    public void cancel() {
        if(delivery.getStatus() == DeliveryStatus.COMP) {
            // 배송이 완료되었으면
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다");
        }

        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem: orderItems) {
            orderItem.cancel(); // 재고 수량을 복원해주는 과정
        }
    }

    // 조회 로직
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
        return totalPrice;
    }
}
