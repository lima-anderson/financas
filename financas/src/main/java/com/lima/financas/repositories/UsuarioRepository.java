package com.lima.financas.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lima.financas.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	boolean existsByEmail(String email);

	Optional<Usuario> findByEmail(String email);

}
