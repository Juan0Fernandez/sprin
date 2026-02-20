package ec.edu.ups.icc.fundamentos01.products.specifications;

import ec.edu.ups.icc.fundamentos01.products.dtos.ProductFilterDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<ProductEntity> filtrar(ProductFilterDto filtro, Long userId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filtro por Usuario (Si nos pasan un ID)
            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("owner").get("id"), userId));
            }

            // 2. Filtro por Nombre (LIKE %name%)
            if (filtro.getName() != null && !filtro.getName().isBlank()) {
                // Convertimos todo a minúsculas para evitar problemas
                String nombreLower = "%" + filtro.getName().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), nombreLower));
            }

            // 3. Filtro por Rango de Precios
            if (filtro.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), filtro.getMinPrice()));
            }
            if (filtro.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), filtro.getMaxPrice()));
            }

            // 4. Filtro por Categoría (JOIN)
            if (filtro.getCategoryId() != null) {
                // Hacemos join con la tabla de categorías
                Join<Object, Object> categorias = root.join("categories");
                predicates.add(criteriaBuilder.equal(categorias.get("id"), filtro.getCategoryId()));
            }

            // Combinamos todos los filtros con AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}