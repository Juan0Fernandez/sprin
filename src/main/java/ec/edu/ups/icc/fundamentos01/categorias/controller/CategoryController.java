package ec.edu.ups.icc.fundamentos01.categorias.controller;

import ec.edu.ups.icc.fundamentos01.categorias.dtos.*; // Importa todos los DTOs
import ec.edu.ups.icc.fundamentos01.categorias.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    // 1. Crear
    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@Valid @RequestBody CreateCategoryDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    // 2. Listar
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // 3. Buscar Uno 
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> findOne(@PathVariable("id") int id) {
        return ResponseEntity.ok(service.findOne(id));
    }

    // 4. Actualizar 
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> update(
            @PathVariable("id") int id, 
            @Valid @RequestBody UpdateCategoryDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // 5. Actualizar Parcial  
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> partialUpdate(
            @PathVariable("id") int id, 
            @RequestBody PartialUpdateCategoryDto dto) {
        return ResponseEntity.ok(service.partialUpdate(id, dto));
    }

    // 6. Eliminar 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}