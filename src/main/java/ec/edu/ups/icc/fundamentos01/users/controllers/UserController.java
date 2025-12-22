package ec.edu.ups.icc.fundamentos01.users.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import ec.edu.ups.icc.fundamentos01.users.dtos.*;
import ec.edu.ups.icc.fundamentos01.users.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service; 
    
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserResponseDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Object findOne(@PathVariable int id) {
        return service.findOne(id);
    }

    @PostMapping
    public UserResponseDto create(@RequestBody CreateUserDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable int id, @RequestBody UpdateUserDto dto) {
        return service.update(id, dto);
    }

    @PatchMapping("/{id}")
    public Object partialUpdate(@PathVariable int id, @RequestBody PartialUpdateUserDto dto) {
        return service.partialUpdate(id, dto);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable int id) {
        return service.delete(id);
    }
}