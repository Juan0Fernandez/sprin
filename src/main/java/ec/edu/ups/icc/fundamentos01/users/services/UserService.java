package ec.edu.ups.icc.fundamentos01.users.services;

import ec.edu.ups.icc.fundamentos01.users.dtos.*;
import java.util.List;

public interface UserService {
    List<UserResponseDto> findAll();
    UserResponseDto findOne(int id);
    UserResponseDto create(CreateUserDto dto);
    UserResponseDto update(int id, UpdateUserDto dto);
    UserResponseDto partialUpdate(int id, PartialUpdateUserDto dto);
    void delete(int id); // <--- IMPORTANTE: Debe devolver void
}