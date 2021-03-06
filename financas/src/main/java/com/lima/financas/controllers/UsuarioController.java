package com.lima.financas.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lima.financas.exceptions.ErroAutenticacao;
import com.lima.financas.exceptions.RegraNegocioException;
import com.lima.financas.models.Usuario;
import com.lima.financas.models.dtos.UsuarioDTO;
import com.lima.financas.services.LancamentoService;
import com.lima.financas.services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService service;

	@Autowired
	private LancamentoService lancamentoService;

	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		} catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
		Usuario usuario = new Usuario(dto.getNome(), dto.getEmail(), dto.getSenha());

		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("{id}/saldo")
	public ResponseEntity tirarSaldo(@PathVariable Long id) {

		Usuario usuario = service.buscarPorId(id);

		if (usuario == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		BigDecimal saldo = lancamentoService.tirarSaldoPorUsuario(id);

		return ResponseEntity.ok(saldo);
	}
	
//	@GetMapping
//	public ResponseEntity buscarUsarios() {
//		List<Usuario> usuarios = service.buscar();
//		return ResponseEntity.ok(usuarios);
//	}

}
