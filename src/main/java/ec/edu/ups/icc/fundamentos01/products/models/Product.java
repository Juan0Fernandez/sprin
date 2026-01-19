package ec.edu.ups.icc.fundamentos01.products.models;

import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.categorias.entity.CategoryEntity;
import java.util.Set;

public class Product {
    private String name;
    private Double price;
    private String description;
    private Integer stock;

    public Product(String name, Double price, String description, Integer stock) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
    }

    public static Product fromDto(CreateProductDto dto) {
        return new Product(dto.getName(), dto.getPrice(), dto.getDescription(), dto.getStock());
    }

    public static Product fromEntity(ProductEntity entity) {
        return new Product(entity.getName(), entity.getPrice(), entity.getDescription(), entity.getStock());
    }

    // FASE 2: Recibe Set<CategoryEntity>
    public ProductEntity toEntity(UserEntity owner, Set<CategoryEntity> categories) {
        ProductEntity entity = new ProductEntity();
        entity.setName(this.name);
        entity.setPrice(this.price);
        entity.setDescription(this.description);
        entity.setStock(this.stock);
        entity.setOwner(owner);
        entity.setCategories(categories);
        return entity;
    }

    public void update(UpdateProductDto dto) {
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.description = dto.getDescription();
        this.stock = dto.getStock();
    }
}