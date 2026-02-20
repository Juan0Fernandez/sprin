package ec.edu.ups.icc.fundamentos01.products.controllers;

import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;
import ec.edu.ups.icc.fundamentos01.security.services.UserDetailsImpl; 
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal; 
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<ProductResponseDto>> findAllPaginado(@Valid @ModelAttribute PageableDto pageable) {
        return ResponseEntity.ok(service.findAll(pageable.getPage(), pageable.getSize(), pageable.getSort()));
    }

    @GetMapping("/slice")
    public ResponseEntity<Slice<ProductResponseDto>> findAllSlice(@Valid @ModelAttribute PageableDto pageable) {
        return ResponseEntity.ok(service.findAllSlice(pageable.getPage(), pageable.getSize(), pageable.getSort()));
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findOne(@PathVariable("id") int id) {
        return ResponseEntity.ok(service.findOne(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody CreateProductDto dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(
            @PathVariable("id") int id, 
            @Valid @RequestBody UpdateProductDto dto,
            @AuthenticationPrincipal UserDetailsImpl currentUser) { 
        return ResponseEntity.ok(service.update(id, dto, currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") int id,
            @AuthenticationPrincipal UserDetailsImpl currentUser) { 
        service.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}