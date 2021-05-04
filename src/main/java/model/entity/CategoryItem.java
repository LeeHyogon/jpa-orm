package model.entity;

import model.entity.item.Item;

import javax.persistence.*;

@Entity
public class CategoryItem {
    @Id
    @GeneratedValue
    @Column(name = "CATEGORYITEM_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

}
