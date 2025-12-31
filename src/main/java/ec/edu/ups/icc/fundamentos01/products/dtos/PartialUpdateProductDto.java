package ec.edu.ups.icc.fundamentos01.products.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class PartialUpdateProductDto {
    
    @Size(min = 3, max = 100)
    private String name;

    private String description;

    @Min(value = 0)
    private Double price;

    @Min(value = 0)
    private Integer stock;

    public PartialUpdateProductDto() {}

    // Getters y Setters...
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}