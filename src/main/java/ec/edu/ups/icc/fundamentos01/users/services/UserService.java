package ec.edu.ups.icc.fundamentos01.users.services;

import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.*;
import org.springframework.data.domain.Page; // Importante para Práctica 10
import java.util.List;

public interface UserService {

    // --- CRUD DE USUARIOS (Lo que ya tenías) ---
    List<UserResponseDto> findAll();
    UserResponseDto findOne(int id);
    UserResponseDto create(CreateUserDto dto);
    UserResponseDto update(int id, UpdateUserDto dto);
    UserResponseDto partialUpdate(int id, PartialUpdateUserDto dto);
    void delete(int id);

    // --- CONSULTAS DE PRODUCTOS DEL USUARIO (Práctica 09 + 10) ---
    
    // Método V1: Simple (Lista)
    List<ProductResponseDto> getProductsByUserId(Long userId);

    // Método V2: Avanzado (Filtros + Paginación)
    Page<ProductResponseDto> getProductsByUserIdWithFilters(
            Long userId, String name, Double minPrice, Double maxPrice, Long categoryId, 
            int page, int size, String sort
    );
}