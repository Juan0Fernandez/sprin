package ec.edu.ups.icc.fundamentos01.users.services;

import ec.edu.ups.icc.fundamentos01.exception.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.exception.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.users.dtos.*;
import ec.edu.ups.icc.fundamentos01.users.models.User;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final ProductMapper productMapper;   

    public UserServiceImpl(UserRepository userRepo, ProductRepository productRepo, ProductMapper productMapper) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        return userRepo.findAll().stream()
                .map(User::fromEntity).map(User::toResponseDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findOne(int id) {
        return userRepo.findById((long) id)
                .map(User::fromEntity).map(User::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public UserResponseDto create(CreateUserDto dto) {
        if (userRepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new ConflictException("El email " + dto.getEmail() + " ya está registrado");
        }
        return Optional.of(dto).map(User::fromDto).map(User::toEntity)
                .map(userRepo::save).map(User::fromEntity).map(User::toResponseDto)
                .orElseThrow(() -> new RuntimeException("Error interno al crear el usuario"));
    }

    @Override
    @Transactional
    public UserResponseDto update(int id, UpdateUserDto dto) {
        return userRepo.findById((long) id).map(entity -> {
            entity.setName(dto.getName());
            entity.setEmail(dto.getEmail());
            entity.setPassword(dto.getPassword());
            return userRepo.save(entity);
        }).map(User::fromEntity).map(User::toResponseDto)
          .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public UserResponseDto partialUpdate(int id, PartialUpdateUserDto dto) {
        return userRepo.findById((long) id).map(entity -> {
            if (dto.getName() != null) entity.setName(dto.getName());
            if (dto.getEmail() != null) entity.setEmail(dto.getEmail());
            if (dto.getPassword() != null) entity.setPassword(dto.getPassword());
            return userRepo.save(entity);
        }).map(User::fromEntity).map(User::toResponseDto)
          .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public void delete(int id) {
        if (!userRepo.existsById((long) id)) {
            throw new NotFoundException("No se puede eliminar. Usuario no encontrado con ID: " + id);
        }
        userRepo.deleteById((long) id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByUserId(Long userId) {
        if (!userRepo.existsById(userId)) {
             throw new NotFoundException("Usuario no encontrado con ID: " + userId);
        }
        return productRepo.findByOwnerId(userId)
                .stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

}