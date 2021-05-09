package model.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="ORDERS")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name="ORDER_ID")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;      //주문 회원


    @OneToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinColumn(name = "DELIVERY_ID")
    private Delivery delivery;  //배송 정보

    @OneToMany(cascade=CascadeType.ALL,mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();


    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;     //주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문 상태

    //== 생성 메서드==//
    public static Order createOrder(Member member,Delivery delivery,
                                    OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(new Date());
        return order;
    }

    //==비즈니스 로직==//
    /**주문 취소 **/
    public void cancel() {
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new RuntimeException(
                "이미 배송완료된 상품은 취소 불가능!");
        }
        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }
    //==조회 로직==//
    /**전체 주문가격 조회**/
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice +=orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    //==연관관계 메서드==//
    public void setMember(Member member) {
        //기존 관계 제거
        if (this.member != null) {
            this.member.getOrders().remove(this);
        }
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery){
        this.delivery=delivery;
        delivery.setOrder(this);
    }
}
