package ec.edu.ups.icc.fundamentos01.products.dtos;

public class ProductFilterDto {
    private String name;
    private Double minPrice;
    private Double maxPrice;
    private Long categoryId;

    // Constructor vacío (obligatorio para Spring)
    public ProductFilterDto() {}

    // Constructor completo
    public ProductFilterDto(String name, Double minPrice, Double maxPrice, Long categoryId) {
        this.name = name;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.categoryId = categoryId;
    }

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getMinPrice() { return minPrice; }
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }

    public Double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(Double maxPrice) { this.maxPrice = maxPrice; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}