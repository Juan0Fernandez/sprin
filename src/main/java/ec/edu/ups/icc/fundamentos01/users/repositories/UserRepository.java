package ec.edu.ups.icc.fundamentos01.users.repositories;

import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // <--- Importante

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT DISTINCT+ p FROM ProductEntity p"+
        "lEFT JOIN p.categorias  c "+
        "WHERE p.owner.id = :userId "+
        "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) "+
        "AND (:minPrice IS NULL OR p.price >= :minPrice) "+ 
        "AND (:maxPrice IS NULL OR p.price <= :maxPrice) "+
        "AND (:categoryId IS NULL OR c.id = :categoryId)"
     )
    List<ProductEntity> findByOwnerWhitFilter(
        @Param("userId") Long userId,
        @Param("name") String name,    
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        @Param("categoryId") Long categoryId
    );
}