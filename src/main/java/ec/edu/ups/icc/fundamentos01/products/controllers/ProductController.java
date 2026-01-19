package ec.edu.ups.icc.fundamentos01.products.controllers;

import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // 1. LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // 2. BUSCAR UNO 
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findOne(@PathVariable("id") int id) {
        return ResponseEntity.ok(service.findOne(id));
    }

    // 3. CREAR
    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody CreateProductDto dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }

    // 4. ACTUALIZAR COMPLETO
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable("id") int id, @Valid @RequestBody UpdateProductDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // 5. ACTUALIZAR PARCIAL 
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDto> partialUpdate(@PathVariable("id") int id, @RequestBody PartialUpdateProductDto dto) {
        return ResponseEntity.ok(service.partialUpdate(id, dto));
    }

    // 6. ELIMINAR 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProductResponseDto>> findByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDto>> findByCategoryId(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ok(service.findByCategoryId(categoryId));
    }

    // 9. VALIDACIÓN DE NOMBRE
    @PostMapping("/validate-name")
    public ResponseEntity<Boolean> validateName(@RequestBody ValidateProductNameDto dto) {
        boolean isValid = service.validateName(dto.getId(), dto.getName());
        return ResponseEntity.ok(isValid);
    }
}