package ec.edu.ups.icc.fundamentos01.products.controllers;

import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> findAll(@Valid @ModelAttribute PageableDto pageable) {
        return ResponseEntity.ok(service.findAll(pageable.getPage(), pageable.getSize(), pageable.getSort()));
    }

    // PRUEBA #4: Slice para performance
    @GetMapping("/slice")
    public ResponseEntity<Slice<ProductResponseDto>> findAllSlice(@Valid @ModelAttribute PageableDto pageable) {
        return ResponseEntity.ok(service.findAllSlice(pageable.getPage(), pageable.getSize(), pageable.getSort()));
    }

    // PRUEBA #5: Búsqueda con filtros (Search)
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDto>> search(
            @ModelAttribute ProductFilterDto filter, 
            @Valid @ModelAttribute PageableDto pageable
    ) {
        return ResponseEntity.ok(service.findWithFilters(
                filter.getName(), filter.getMinPrice(), filter.getMaxPrice(), filter.getCategoryId(),
                pageable.getPage(), pageable.getSize(), pageable.getSort()
        ));
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ProductResponseDto>> findByUser(
            @PathVariable Long userId,
            @Valid @ModelAttribute PageableDto pageable
    ) {
        return ResponseEntity.ok(service.findByUserIdWithFilters(
                userId, null, null, null, null,
                pageable.getPage(), pageable.getSize(), pageable.getSort()
        ));
    }

    // CRUD Básico necesario
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findOne(@PathVariable("id") int id) {
        return ResponseEntity.ok(service.findOne(id));
    }
    
    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody CreateProductDto dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}