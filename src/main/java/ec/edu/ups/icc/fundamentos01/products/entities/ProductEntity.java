package ec.edu.ups.icc.fundamentos01.products.entities;

import ec.edu.ups.icc.fundamentos01.core.entities.BaseModel;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class ProductEntity extends BaseModel {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    public ProductEntity() {}

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}