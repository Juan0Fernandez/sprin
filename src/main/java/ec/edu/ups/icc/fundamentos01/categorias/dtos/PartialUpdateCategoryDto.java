package ec.edu.ups.icc.fundamentos01.categorias.dtos;

public class PartialUpdateCategoryDto {
    
    private String name;
    private String description;

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}