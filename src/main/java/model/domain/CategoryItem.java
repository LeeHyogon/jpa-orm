package model.domain;

import model.domain.item.Item;

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
