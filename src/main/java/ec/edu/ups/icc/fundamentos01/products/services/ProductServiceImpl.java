package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.exception.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.exception.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.models.Product;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;

    public ProductServiceImpl(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public List<ProductResponseDto> findAll() {
        return productRepo.findAll()
                .stream()
                .map(Product::fromEntity)
                .map(Product::toResponseDto)
                .toList();
    }

    @Override
    public ProductResponseDto findOne(int id) {
        return productRepo.findById((long) id)
                .map(Product::fromEntity)
                .map(Product::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public ProductResponseDto create(CreateProductDto dto) {
        // 1. VALIDACIÓN DE NEGOCIO: Nombre único
        if (productRepo.findByName(dto.getName()).isPresent()) {
            throw new ConflictException("Ya existe un producto con el nombre: " + dto.getName());
        }

        // 2. CREACIÓN
        return Optional.of(dto)
                .map(Product::fromDto)
                .map(Product::toEntity)
                .map(productRepo::save)
                .map(Product::fromEntity)
                .map(Product::toResponseDto)
                .orElseThrow(() -> new RuntimeException("Error interno al crear el producto"));
    }

    @Override
    public ProductResponseDto update(int id, UpdateProductDto dto) {
        return productRepo.findById((long) id)
                .map(entity -> {
                    entity.setName(dto.getName());
                    entity.setDescription(dto.getDescription());
                    entity.setPrice(dto.getPrice());
                    entity.setStock(dto.getStock());
                    return productRepo.save(entity);
                })
                .map(Product::fromEntity)
                .map(Product::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public ProductResponseDto partialUpdate(int id, PartialUpdateProductDto dto) {
        return productRepo.findById((long) id)
                .map(entity -> {
                    if (dto.getName() != null) entity.setName(dto.getName());
                    if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
                    if (dto.getPrice() != null) entity.setPrice(dto.getPrice());
                    if (dto.getStock() != null) entity.setStock(dto.getStock());
                    return productRepo.save(entity);
                })
                .map(Product::fromEntity)
                .map(Product::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public void delete(int id) {
        if (!productRepo.existsById((long) id)) {
            throw new NotFoundException("No se puede eliminar. Producto no encontrado con ID: " + id);
        }
        productRepo.deleteById((long) id);
    }
}