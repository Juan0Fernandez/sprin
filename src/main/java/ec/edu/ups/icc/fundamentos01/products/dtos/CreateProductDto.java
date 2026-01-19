package ec.edu.ups.icc.fundamentos01.products.dtos;

import jakarta.validation.constraints.*;
import java.util.List; // <--- ¡No olvides este import!

public class CreateProductDto {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotNull(message = "El precio es obligatorio")
    @Min(0)
    private Double price;

    @Size(max = 500)
    private String description;

    @NotNull(message = "El stock es obligatorio")
    @Min(0)
    private Integer stock;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    // --- AQUÍ ESTABA EL ERROR ---
    // Usamos @NotEmpty para listas. El nombre DEBE ser 'categoryIds' (plural)
    @NotEmpty(message = "Debe especificar al menos una categoría")
    private List<Long> categoryIds; 

    

    // --- GETTERS Y SETTERS (Obligatorios para que Spring funcione) ---

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<Long> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(List<Long> categoryIds) { this.categoryIds = categoryIds; }
}