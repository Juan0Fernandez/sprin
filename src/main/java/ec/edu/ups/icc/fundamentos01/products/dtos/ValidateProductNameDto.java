package ec.edu.ups.icc.fundamentos01.products.dtos;
import jakarta.validation.constraints.NotBlank; 

public class ValidateProductNameDto {

    private int id;
    
    @NotBlank(message = "El nombre es obligatorio") 
    private String name;

    public ValidateProductNameDto() {
    }

    public ValidateProductNameDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}