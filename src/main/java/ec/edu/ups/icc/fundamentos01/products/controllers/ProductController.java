package ec.edu.ups.icc.fundamentos01.products.controllers;

import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductResponseDto> findAll() {
        return service.findAll();
    }

    // 👇 AQUÍ AGREGAMOS ("id")
    @GetMapping("/{id}")
    public ProductResponseDto findOne(@PathVariable("id") int id) {
        return service.findOne(id);
    }

    @PostMapping
    public ProductResponseDto create(@Valid @RequestBody CreateProductDto dto) {
        return service.create(dto);
    }

    // 👇 AQUÍ TAMBIÉN
    @PutMapping("/{id}")
    public ProductResponseDto update(@PathVariable("id") int id, @Valid @RequestBody UpdateProductDto dto) {
        return service.update(id, dto);
    }

    // 👇 Y AQUÍ
    @PatchMapping("/{id}")
    public ProductResponseDto partialUpdate(@PathVariable("id") int id, @RequestBody PartialUpdateProductDto dto) {
        return service.partialUpdate(id, dto);
    }

    // 👇 Y FINALMENTE AQUÍ
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        service.delete(id);
    }
}