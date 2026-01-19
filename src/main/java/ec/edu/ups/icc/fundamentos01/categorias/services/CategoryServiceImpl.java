package ec.edu.ups.icc.fundamentos01.categorias.services;

import ec.edu.ups.icc.fundamentos01.categorias.dtos.*; // Importa todos los DTOs
import ec.edu.ups.icc.fundamentos01.categorias.entity.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categorias.Repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.exception.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.exception.domain.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public CategoryResponseDto create(CreateCategoryDto dto) {
        if (repository.existsByName(dto.getName())) {
            throw new ConflictException("La categoría ya existe: " + dto.getName());
        }
        CategoryEntity entity = new CategoryEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return toDto(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto findOne(int id) {
        return repository.findById((long) id).map(this::toDto)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada: " + id));
    }

    @Override
    @Transactional
    public CategoryResponseDto update(int id, UpdateCategoryDto dto) {
        CategoryEntity entity = repository.findById((long) id)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada: " + id));
        
        if (!entity.getName().equalsIgnoreCase(dto.getName()) && repository.existsByName(dto.getName())) {
             throw new ConflictException("Nombre ya en uso: " + dto.getName());
        }

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public CategoryResponseDto partialUpdate(int id, PartialUpdateCategoryDto dto) {
        CategoryEntity entity = repository.findById((long) id)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada: " + id));

        if (dto.getName() != null) {
            if (!entity.getName().equalsIgnoreCase(dto.getName()) && repository.existsByName(dto.getName())) {
                throw new ConflictException("Nombre ya en uso: " + dto.getName());
            }
            entity.setName(dto.getName());
        }
        
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        
        return toDto(repository.save(entity));
    }

    // CORRECCIÓN: Cambiado a 'int'
    @Override
    @Transactional
    public void delete(int id) {
        if (!repository.existsById((long) id)) {
            throw new NotFoundException("Categoría no encontrada: " + id);
        }
        repository.deleteById((long) id);
    }

    private CategoryResponseDto toDto(CategoryEntity entity) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }
}