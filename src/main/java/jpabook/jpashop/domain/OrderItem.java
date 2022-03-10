package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne // 하나의 상품은 여러 번 주문될 수 있으므로 주문아이템이 다, 아이템이 일 관계이다.
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne  // 하나의 주문은 여러 주문상품을 가질 수 있으므로 주문아이템이 다, 주문이 일 관계이다.
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량
}
