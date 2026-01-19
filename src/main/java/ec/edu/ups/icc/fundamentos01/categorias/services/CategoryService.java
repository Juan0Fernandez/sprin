package ec.edu.ups.icc.fundamentos01.categorias.services;

import ec.edu.ups.icc.fundamentos01.categorias.dtos.CategoryResponseDto;
import ec.edu.ups.icc.fundamentos01.categorias.dtos.CreateCategoryDto;
import ec.edu.ups.icc.fundamentos01.categorias.dtos.UpdateCategoryDto;
import ec.edu.ups.icc.fundamentos01.categorias.dtos.PartialUpdateCategoryDto; // <--- ¡IMPORTANTE!
import java.util.List;

public interface CategoryService {
    
    CategoryResponseDto create(CreateCategoryDto dto);
    
    List<CategoryResponseDto> findAll();
    
    CategoryResponseDto findOne(int id);
    
    CategoryResponseDto update(int id, UpdateCategoryDto dto);
    
    CategoryResponseDto partialUpdate(int id, PartialUpdateCategoryDto dto);
    
    void delete(int id);
}