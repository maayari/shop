package com.alten.shop.model;
import java.util.List;
import com.alten.shop.enums.CatogoryEnum;
import com.alten.shop.enums.InventoryStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
    private long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private long price ;

    @Enumerated(EnumType.STRING)
    @Column(name="category")
    private CatogoryEnum category;
    
    @Enumerated(EnumType.STRING)
    @Column(name="inventory_status")
    private InventoryStatus inventoryStatus;

    @Column(name = "quantity")
    private long quantity ;

    @Column(name = "rating")
    private long rating ;

    @OneToMany(mappedBy="productId",fetch=FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Image> images;

	public Product(String code, String name, String description, long price, CatogoryEnum category,
			InventoryStatus inventoryStatus, long quantity, long rating) {
		super();
		this.code = code;
		this.name = name;
		this.description = description;
		this.price = price;
		this.category = category;
		this.inventoryStatus = inventoryStatus;
		this.quantity = quantity;
		this.rating = rating;
	}
}
