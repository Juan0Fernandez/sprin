package ec.edu.ups.icc.fundamentos01.categorias.Repositories;
import ec.edu.ups.icc.fundamentos01.categorias.entity.CategoryEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    // Validar unicidad 
    boolean existsByName(String name);

    // Buscar por nombre 
    Optional<CategoryEntity> findByNameIgnoreCase(String name);
}