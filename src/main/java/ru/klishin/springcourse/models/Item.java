package ru.klishin.springcourse.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @NotEmpty(message = "Item name should not be empty")
    @Column(name = "item_name")
    private String itemName;

    public Item(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        return "Item : " + itemName;
    }
}
