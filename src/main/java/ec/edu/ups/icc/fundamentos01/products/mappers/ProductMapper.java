package ec.edu.ups.icc.fundamentos01.products.mappers;

import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductResponseDto toResponseDto(ProductEntity entity) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.price = entity.getPrice();
        dto.stock = entity.getStock();
        dto.description = entity.getDescription();
        dto.createdAt = entity.getCreatedAt();
        dto.updatedAt = entity.getUpdatedAt();

        if (entity.getOwner() != null) {
            ProductResponseDto.UserSummaryDto userDto = new ProductResponseDto.UserSummaryDto();
            userDto.id = entity.getOwner().getId();
            userDto.name = entity.getOwner().getName();
            userDto.email = entity.getOwner().getEmail();
            dto.user = userDto;
        }

        if (entity.getCategories() != null) {
            dto.categories = entity.getCategories().stream().map(cat -> {
                ProductResponseDto.CategorySummaryDto cDto = new ProductResponseDto.CategorySummaryDto();
                cDto.id = cat.getId();
                cDto.name = cat.getName();
                cDto.description = cat.getDescription();
                return cDto;
            }).collect(Collectors.toList());
        } else {
            dto.categories = new ArrayList<>();
        }
        return dto;
    }
}