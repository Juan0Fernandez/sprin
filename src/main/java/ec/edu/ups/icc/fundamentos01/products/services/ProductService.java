package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import java.util.List;

public interface ProductService {
    // CRUD Básico
    ProductResponseDto create(CreateProductDto dto);
    List<ProductResponseDto> findAll();
    ProductResponseDto findOne(int id);
    ProductResponseDto update(int id, UpdateProductDto dto);
    void delete(int id);

    // --- ESTOS SON LOS QUE TE FALTABAN Y CAUSABAN EL ERROR ---
    ProductResponseDto partialUpdate(int id, PartialUpdateProductDto dto);
    boolean validateName(Integer id, String name);

    // Búsquedas
    List<ProductResponseDto> findByUserId(Long userId);
    List<ProductResponseDto> findByCategoryId(Long categoryId);
}