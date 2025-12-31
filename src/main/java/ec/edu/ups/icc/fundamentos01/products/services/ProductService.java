package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import java.util.List;

public interface ProductService {
    List<ProductResponseDto> findAll();
    ProductResponseDto findOne(int id);
    ProductResponseDto create(CreateProductDto dto);
    ProductResponseDto update(int id, UpdateProductDto dto);
    ProductResponseDto partialUpdate(int id, PartialUpdateProductDto dto);
    void delete(int id);
}