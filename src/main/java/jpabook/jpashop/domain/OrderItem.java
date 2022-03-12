package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

//    protected OrderItem() {
//        // 이렇게 하면 서비스에서 생성하는 것을 막을 수 있다.
//    }

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 하나의 상품은 여러 번 주문될 수 있으므로 주문아이템이 다, 아이템이 일 관계이다.
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)  // 하나의 주문은 여러 주문상품을 가질 수 있으므로 주문아이템이 다, 주문이 일 관계이다.
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

    // 생성 메서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        // 물건을 하나하나 장바구니에 넣는 과정. 이것을 완료하면 Order로 주문을 만든다.
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); // 재고를 감소시킨다.

        return orderItem;
    }

    // 비즈니스 로직
    public void cancel() {
        getItem().addStock(count);
        // 주문을 취소했으므로 주문한 수량인 count 만큼 재고 수량을 다시 늘려줘야 한다.
    }

    // 조회 로직
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
