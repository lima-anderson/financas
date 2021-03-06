package com.lima.financas.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lima.financas.exceptions.RegraNegocioException;
import com.lima.financas.models.Lancamento;
import com.lima.financas.models.Usuario;
import com.lima.financas.models.dtos.AtualizaStatusDTO;
import com.lima.financas.models.dtos.LancamentoDTO;
import com.lima.financas.models.enums.StatusLancamento;
import com.lima.financas.models.enums.TipoLancamento;
import com.lima.financas.services.LancamentoService;
import com.lima.financas.services.UsuarioService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public ResponseEntity buscar(@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano, @RequestParam("usuario") Long idUsuario) {

		Lancamento lancamentoFiltro = new Lancamento();

		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);

		Usuario usuario = usuarioService.buscarPorId(idUsuario);

		lancamentoFiltro.setUsuario(usuario);

		List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);

		return ResponseEntity.ok(lancamentos);
	}

	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		try {
			Lancamento lancamento = converter(dto);
			lancamentoService.salvar(lancamento);
			return new ResponseEntity(lancamento, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
		try {
			Lancamento entidade = lancamentoService.buscarPorId(id);
			Lancamento lancamento = converter(dto);
			lancamento.setId(entidade.getId());
			lancamentoService.atualizar(lancamento);
			return ResponseEntity.ok(lancamento);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto) {
		try {
			Lancamento lancamento = lancamentoService.buscarPorId(id);
			StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus().toString());

			if (statusSelecionado == null) {
				return ResponseEntity.badRequest().body("Status Inv??lido");
			}

			lancamento.setStatus(statusSelecionado);

			lancamentoService.atualizar(lancamento);
			return ResponseEntity.ok(lancamento);

		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		try {
			Lancamento lancamento = lancamentoService.buscarPorId(id);
			lancamentoService.deletar(lancamento);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	private Lancamento converter(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setMes(dto.getMes());
		lancamento.setAno(dto.getAno());
		lancamento.setValor(dto.getValor());

		Usuario usuario = usuarioService.buscarPorId(dto.getUsuario());
		lancamento.setUsuario(usuario);

		if (dto.getTipo() != null) {
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo().toString()));
		}

		if (dto.getStatus() != null) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus().toString()));
		}
		return lancamento;
	}

}