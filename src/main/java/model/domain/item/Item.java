package model.domain.item;

import lombok.Getter;
import lombok.Setter;
import model.domain.Category;
import model.domain.exception.NotEnoughStockException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DTYPE")
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    @OneToMany(mappedBy="items")
    private List<Category> categories=new ArrayList<Category>();

    private String name;
    private int price;
    private int stockQuantity;

    // ==비즈니스 로직 == //
    public void addStock(int quantity){
        this.stockQuantity+=quantity;
    }
    public void removeStock(int quantity){
        int restStock = this. stockQuantity - quantity;
        if(restStock <0){
            throw new NotEnoughStockException("need more sotck");
        }
        this.stockQuantity = restStock;
    }
}

