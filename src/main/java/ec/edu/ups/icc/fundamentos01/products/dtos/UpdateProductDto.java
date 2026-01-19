package ec.edu.ups.icc.fundamentos01.products.dtos;

import jakarta.validation.constraints.*;
import java.util.Set;

public class UpdateProductDto {
    @NotBlank private String name;
    @NotNull private Double price;
    private String description;
    @NotNull private Integer stock;
    
    // Opcional: Para actualizar categorías
    private Set<Long> categoryIds;

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Set<Long> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(Set<Long> categoryIds) { this.categoryIds = categoryIds; }
}