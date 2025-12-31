package ec.edu.ups.icc.fundamentos01.products.models;

import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;

public class Product {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;

    public Product() {}

    // Constructor con Reglas de Negocio
    public Product(Long id, String name, String description, Double price, Integer stock) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (price == null || price < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public static Product fromDto(CreateProductDto dto) {
        return new Product(null, dto.getName(), dto.getDescription(), dto.getPrice(), dto.getStock());
    }

    public static Product fromEntity(ProductEntity entity) {
        return new Product(entity.getId(), entity.getName(), entity.getDescription(), entity.getPrice(), entity.getStock());
    }

    public ProductEntity toEntity() {
        ProductEntity entity = new ProductEntity();
        if (this.id != null && this.id > 0) entity.setId(this.id);
        entity.setName(this.name);
        entity.setDescription(this.description);
        entity.setPrice(this.price);
        entity.setStock(this.stock);
        return entity;
    }

    public ProductResponseDto toResponseDto() {
        return new ProductResponseDto(this.id, this.name, this.description, this.price, this.stock);
    }
    
    // Getters y Setters... (Son necesarios para la lógica)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}