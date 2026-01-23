package ec.edu.ups.icc.fundamentos01.products.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class PageableDto {
    
    @PositiveOrZero(message = "La página no puede ser negativa")
    private int page = 0;

    @Min(value = 1, message = "El tamaño mínimo es 1")
    @Max(value = 50, message = "El tamaño máximo es 50")
    private int size = 10;

    private String sort = "id,asc";

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public String getSort() { return sort; }
    public void setSort(String sort) { this.sort = sort; }
}