package model.service;

import model.domain.Address;
import model.domain.Member;
import model.domain.Order;
import model.domain.OrderStatus;
import model.domain.exception.NotEnoughStockException;
import model.domain.item.Book;
import model.domain.item.Item;
import model.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:appConfig.xml")
@Transactional
public class OrderServiceTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {

        //Given
        Member member = createMember();
        Item item = createBook("서울 JPA", 10000, 10); //이름, 가격, 재고
        int orderCount = 2;

        //When
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //Then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 주문(ORDER)이다.", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * 2, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, item.getStockQuantity());
    }
    @Test(expected= NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception{
        //Given
        Member member=createMember();
        Item item = createBook("서울 JPA",10000,10);
        int orderCount=11;

        //When
        orderService.order(member.getId(),item.getId(),orderCount);

        //
        //Then
        fail("재고 수량 부족 예외발생한다.");
    }
    @Test
    public void 주문취소(){
        //Given
        Member member =createMember();
        Item item = createBook("서울 JPA",10000,10);
        int orderCount=2;

        Long orderId=orderService.order(member.getId(),item.getId(),orderCount);

        //When
        orderService.cancelOrder(orderId);

        //Then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소 상태는 CANCEL",OrderStatus.CANCEL,getOrder.getStatus());
        assertEquals("주문 취소상품은 그만큼 재고 증가함",10,item.getStockQuantity());
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }
    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }
}
