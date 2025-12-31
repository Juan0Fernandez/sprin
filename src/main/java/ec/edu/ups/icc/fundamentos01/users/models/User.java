package ec.edu.ups.icc.fundamentos01.users.models;

import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import java.time.LocalDateTime;

public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;

    public User() {}

    // Constructor con Reglas de Negocio (MD Punto 6)
    public User(Long id, String name, String email, String password) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Nombre inválido");

        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Email inválido");

        if (password == null || password.length() < 8)
            throw new IllegalArgumentException("Password inválido: Mínimo 8 caracteres");

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now(); // Asignamos fecha al crear
    }

    // Constructor completo para cuando viene de la Base de Datos
    public User(Long id, String name, String email, String password, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    // 1. DTO -> Modelo
    public static User fromDto(CreateUserDto dto) {
        // Usamos el constructor que valida
        return new User(null, dto.getName(), dto.getEmail(), dto.getPassword());
    }

    // 2. Entity -> Modelo
    public static User fromEntity(UserEntity entity) {
        return new User(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getCreatedAt()
        );
    }

    // 3. Modelo -> Entity
    public UserEntity toEntity() {
        UserEntity entity = new UserEntity();
        if (this.id != null && this.id > 0) {
            entity.setId(this.id);
        }
        entity.setName(this.name);
        entity.setEmail(this.email);
        entity.setPassword(this.password);
        // createdAt lo maneja la BD usualmente, pero se puede setear si es necesario
        return entity;
    }

    // 4. Modelo -> Respuesta JSON
    public UserResponseDto toResponseDto() {
        return new UserResponseDto(this.id, this.name, this.email);
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}