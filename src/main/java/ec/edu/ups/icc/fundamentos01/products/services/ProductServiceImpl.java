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
import ec.edu.ups.icc.fundamentos01.products.specifications.ProductSpecification; // <--- CUMPLE PUNTO 7.2
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findAll(int page, int size, String sort) {
        Pageable pageable = construirPageable(page, size, sort);
        return productRepo.findAll(pageable).map(mapper::toResponseDto);
    }

    // IMPLEMENTACIÓN DE SLICE (Punto 4)
    @Override
    @Transactional(readOnly = true)
    public Slice<ProductResponseDto> findAllSlice(int page, int size, String sort) {
        Pageable pageable = construirPageable(page, size, sort);
        return productRepo.findAll(pageable).map(mapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findWithFilters(String name, Double minPrice, Double maxPrice, Long categoryId, int page, int size, String sort) {
        Pageable pageable = construirPageable(page, size, sort);
        
        // 1. Creamos el DTO de filtro (Punto 5.2)
        ProductFilterDto filtro = new ProductFilterDto(name, minPrice, maxPrice, categoryId);
        
        // 2. Usamos la Specification (Punto 7.2)
        var spec = ProductSpecification.filtrar(filtro, null);

        return productRepo.findAll(spec, pageable).map(mapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findByUserIdWithFilters(Long userId, String name, Double minPrice, Double maxPrice, Long categoryId, int page, int size, String sort) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("Usuario no encontrado con ID: " + userId);
        }
        Pageable pageable = construirPageable(page, size, sort);

        // 1. Creamos el DTO de filtro
        ProductFilterDto filtro = new ProductFilterDto(name, minPrice, maxPrice, categoryId);

        var spec = ProductSpecification.filtrar(filtro, userId);

        // 3. Ejecutamos
        return productRepo.findAll(spec, pageable).map(mapper::toResponseDto);
    }

    private Pageable construirPageable(int page, int size, String sortStr) {
        String[] partes = sortStr.split(",");
        String propiedad = partes[0];
        if(propiedad == null || propiedad.isBlank()) propiedad = "id";
        
        Sort sortObj = Sort.by(propiedad);
        if (partes.length > 1 && "desc".equalsIgnoreCase(partes[1])) {
            sortObj = sortObj.descending();
        } else {
            sortObj = sortObj.ascending();
        }
        return PageRequest.of(page, size, sortObj);
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
    @Transactional(readOnly = true)
    public ProductResponseDto findOne(int id) {
        return productRepo.findById((long) id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public ProductResponseDto update(int id, UpdateProductDto dto) {
        ProductEntity existing = productRepo.findById((long) id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));
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
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));
        if(dto.getName() != null) existing.setName(dto.getName());
        if(dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if(dto.getPrice() != null) existing.setPrice(dto.getPrice());
        if(dto.getStock() != null) existing.setStock(dto.getStock());
        return mapper.toResponseDto(productRepo.save(existing));
    }

    @Override
    @Transactional
    public void delete(int id) {
        if (!productRepo.existsById((long) id)) {
            throw new NotFoundException("Producto no encontrado con ID: " + id);
        }
        productRepo.deleteById((long) id);
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

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByCategoryId(Long categoryId) {
         if (!categoryRepo.existsById(categoryId)) {
             throw new NotFoundException("Categoría no encontrada");
         }
         return productRepo.findByCategoriesId(categoryId)
                 .stream()
                 .map(mapper::toResponseDto)
                 .collect(Collectors.toList());
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