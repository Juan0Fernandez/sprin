package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import org.springframework.data.domain.Page;
import java.util.List;

public interface ProductService {
    ProductResponseDto create(CreateProductDto dto);
    ProductResponseDto findOne(int id);
    ProductResponseDto update(int id, UpdateProductDto dto);
    ProductResponseDto partialUpdate(int id, PartialUpdateProductDto dto);
    void delete(int id);
    boolean validateName(Integer id, String name);

    Page<ProductResponseDto> findAll(int page, int size, String sort);
    Page<ProductResponseDto> findWithFilters(String name, Double minPrice, Double maxPrice, Long categoryId, int page, int size, String sort);
    Page<ProductResponseDto> findByUserIdWithFilters(Long userId, String name, Double minPrice, Double maxPrice, Long categoryId, int page, int size, String sort);

    List<ProductResponseDto> findByCategoryId(Long categoryId);
}