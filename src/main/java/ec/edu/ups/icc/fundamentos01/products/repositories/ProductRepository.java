package ec.edu.ups.icc.fundamentos01.products.repositories;

import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByName(String name);
    List<ProductEntity> findByOwnerId(Long userId);
    List<ProductEntity> findByCategoriesId(Long categoryId);

    // CORRECCIÓN: Quitamos LOWER() alrededor de :name
    @Query("SELECT p FROM ProductEntity p " +
           "WHERE (:name IS NULL OR LOWER(p.name) LIKE :name) " + // <--- MIRA AQUÍ: Solo :name
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "AND (:categoryId IS NULL OR EXISTS (SELECT c FROM p.categories c WHERE c.id = :categoryId))")
    Page<ProductEntity> findWithFilters(
            @Param("name") String name,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    // CORRECCIÓN: Quitamos LOWER() alrededor de :name
    @Query("SELECT p FROM ProductEntity p " +
           "WHERE p.owner.id = :userId " +
           "AND (:name IS NULL OR LOWER(p.name) LIKE :name) " + // <--- MIRA AQUÍ: Solo :name
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "AND (:categoryId IS NULL OR EXISTS (SELECT c FROM p.categories c WHERE c.id = :categoryId))")
    Page<ProductEntity> findByUserIdWithFilters(
            @Param("userId") Long userId,
            @Param("name") String name,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );
}