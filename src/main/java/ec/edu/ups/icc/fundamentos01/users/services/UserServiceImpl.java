package ec.edu.ups.icc.fundamentos01.users.services;

import ec.edu.ups.icc.fundamentos01.exception.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.exception.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.users.dtos.*;
import ec.edu.ups.icc.fundamentos01.users.models.User;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepo.findAll()
                .stream()
                .map(User::fromEntity)
                .map(User::toResponseDto)
                .toList();
    }

    @Override
    public UserResponseDto findOne(int id) {
        return userRepo.findById((long) id)
                .map(User::fromEntity)
                .map(User::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public UserResponseDto create(CreateUserDto dto) {
        // 1. VALIDACIÓN PREVIA : Verificar duplicados
        if (userRepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new ConflictException("El email " + dto.getEmail() + " ya está registrado");
        }

        // 2. CREACIÓN (Estilo Funcional): Guardar y mapear
        return Optional.of(dto)
                .map(User::fromDto)
                .map(User::toEntity)
                .map(userRepo::save)
                .map(User::fromEntity)
                .map(User::toResponseDto)
                .orElseThrow(() -> new RuntimeException("Error interno al crear el usuario"));
    }

    @Override
    public UserResponseDto update(int id, UpdateUserDto dto) {
        return userRepo.findById((long) id)
                .map(entity -> {
                    entity.setName(dto.getName());
                    entity.setEmail(dto.getEmail());
                    entity.setPassword(dto.getPassword());
                    return userRepo.save(entity);
                })
                .map(User::fromEntity)
                .map(User::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public UserResponseDto partialUpdate(int id, PartialUpdateUserDto dto) {
        return userRepo.findById((long) id)
                .map(entity -> {
                    if (dto.getName() != null) entity.setName(dto.getName());
                    if (dto.getEmail() != null) entity.setEmail(dto.getEmail());
                    if (dto.getPassword() != null) entity.setPassword(dto.getPassword());
                    return userRepo.save(entity);
                })
                .map(User::fromEntity)
                .map(User::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public void delete(int id) {
        if (!userRepo.existsById((long) id)) {
            throw new NotFoundException("No se puede eliminar. Usuario no encontrado con ID: " + id);
        }
        userRepo.deleteById((long) id);
    }
}