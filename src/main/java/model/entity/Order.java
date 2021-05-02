package model.entity;

import lombok.Getter;
import lombok.Setter;
import org.graalvm.compiler.lir.LIRInstruction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="ORDERS")
public class Order {

    @Id
    @GeneratedValue
    @Column(name="ORDER_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;      //주문 회원


    @OneToOne
    @JoinColumn(name = "DELIVERY_ID")
    private Delivery delivery;  //배송 정보

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();


    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;     //주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문 상태

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
