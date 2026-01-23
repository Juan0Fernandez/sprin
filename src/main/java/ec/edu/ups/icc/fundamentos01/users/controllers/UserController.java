package ec.edu.ups.icc.fundamentos01.users.controllers;

import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.*;
import ec.edu.ups.icc.fundamentos01.users.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // Solo inyectamos UserService, él se encarga de hablar con los repositorios necesarios
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // --- CRUD USUARIOS ---
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findOne(@PathVariable("id") int id) {
        return ResponseEntity.ok(userService.findOne(id));
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody CreateUserDto dto) {
        return ResponseEntity.status(201).body(userService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable("id") int id, @Valid @RequestBody UpdateUserDto dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> partialUpdate(@PathVariable("id") int id, @RequestBody PartialUpdateUserDto dto) {
        return ResponseEntity.ok(userService.partialUpdate(id, dto));
    }

    // --- ENDPOINTS DE PRODUCTOS DEL USUARIO ---

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductResponseDto>> getProductsByUserId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getProductsByUserId(id));
    }

    @GetMapping("/{id}/products-v2")
    public ResponseEntity<Page<ProductResponseDto>> getProductsByUserIdWithFilters(
            @PathVariable("id") Long id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "id") String sort
        ) {
        // Llamamos a userService, que ya tiene la lógica paginada lista
        return ResponseEntity.ok(
            userService.getProductsByUserIdWithFilters(id, name, minPrice, maxPrice, categoryId, page, size, sort)
        );
    }
}