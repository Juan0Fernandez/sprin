package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.categorias.entity.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categorias.Repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.exception.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.exception.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos01.products.models.Product;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection; 
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;
    private final ProductMapper mapper;

    public ProductServiceImpl(ProductRepository productRepo, UserRepository userRepo, CategoryRepository categoryRepo, ProductMapper mapper) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public ProductResponseDto create(CreateProductDto dto) {
        if (productRepo.findByName(dto.getName()).isPresent()) {
            throw new ConflictException("Ya existe el producto: " + dto.getName());
        }
        UserEntity owner = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        Set<CategoryEntity> categories = validateAndGetCategories(dto.getCategoryIds());

        Product product = Product.fromDto(dto);
        ProductEntity entity = product.toEntity(owner, categories);
        
        return mapper.toResponseDto(productRepo.save(entity));
    }

    @Override
    @Transactional
    public ProductResponseDto update(int id, UpdateProductDto dto) {
        ProductEntity existing = productRepo.findById((long) id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        
        Product product = Product.fromEntity(existing);
        product.update(dto);

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setStock(dto.getStock());
        
        if (dto.getCategoryIds() != null) {
            Set<CategoryEntity> newCategories = validateAndGetCategories(dto.getCategoryIds());
            existing.clearCategories();
            existing.setCategories(newCategories);
        }
        
        return mapper.toResponseDto(productRepo.save(existing));
    }

    @Override
    @Transactional
    public ProductResponseDto partialUpdate(int id, PartialUpdateProductDto dto) {
        ProductEntity existing = productRepo.findById((long) id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        
        if(dto.getName() != null) existing.setName(dto.getName());
        if(dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if(dto.getPrice() != null) existing.setPrice(dto.getPrice());
        if(dto.getStock() != null) existing.setStock(dto.getStock());
        
        return mapper.toResponseDto(productRepo.save(existing));
    }

    @Override
    @Transactional
    public void delete(int id) {
        if (!productRepo.existsById((long) id)) throw new NotFoundException("Producto no encontrado");
        productRepo.deleteById((long) id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findAll() {
        return productRepo.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto findOne(int id) {
        return productRepo.findById((long) id).map(mapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByUserId(Long userId) {
         if (!userRepo.existsById(userId)) throw new NotFoundException("Usuario no encontrado");
         return productRepo.findByOwnerId(userId).stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByCategoryId(Long categoryId) {
         if (!categoryRepo.existsById(categoryId)) throw new NotFoundException("Categoría no encontrada");
         return productRepo.findByCategoriesId(categoryId).stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public boolean validateName(Integer id, String name) {
        productRepo.findByName(name).ifPresent(existing -> {
             if (id == null || existing.getId() != id.longValue()) {
                throw new ConflictException("El nombre ya está en uso");
            }
        });
        return true;
    }

    private Set<CategoryEntity> validateAndGetCategories(Collection<Long> categoryIds) {
        Set<CategoryEntity> categories = new HashSet<>();
        if (categoryIds != null) {
            for (Long catId : categoryIds) {
                categories.add(categoryRepo.findById(catId)
                        .orElseThrow(() -> new NotFoundException("Categoría no encontrada: " + catId)));
            }
        }
        return categories;
    }
}