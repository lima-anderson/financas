package com.lima.financas.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lima.financas.exceptions.RegraNegocioException;
import com.lima.financas.models.Lancamento;
import com.lima.financas.models.Usuario;
import com.lima.financas.models.enums.StatusLancamento;
import com.lima.financas.models.enums.TipoLancamento;
import com.lima.financas.repositories.LancamentoRepository;
import com.lima.financas.repositories.UsuarioRepository;

@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	public Lancamento buscarPorId(Long id) {
		return lancamentoRepository.findById(id)
				.orElseThrow(() -> new RegraNegocioException("Lancamento não encontrado"));
	}

	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);

		lancamento.setStatus(StatusLancamento.PENDENTE);
		return lancamentoRepository.save(lancamento);
	}

	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validar(lancamento);
		return lancamentoRepository.save(lancamento);
	}

	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		lancamentoRepository.delete(lancamento);
	}

	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		Example example = Example.of(lancamentoFiltro,
				ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));

		return lancamentoRepository.findAll(example);
	}

	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);

	}

	public void validar(Lancamento lancamento) {
		if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Descrição inválida");
		}

		if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Mês inválido");
		}

		if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
			throw new RegraNegocioException("Ano inválido");
		}

		if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
			throw new RegraNegocioException("Usuário inválido");
		}

		if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Valor inválido");
		}

		if (lancamento.getTipo() == null) {
			throw new RegraNegocioException("Tipo inválido");
		}
	}

	@Transactional(readOnly = true)
	public BigDecimal tirarSaldoPorUsuario(Long id) {
		BigDecimal receita = lancamentoRepository.tirarSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.RECEITA);
		BigDecimal despesa = lancamentoRepository.tirarSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.DESPESA);

		if (receita == null) {
			receita = BigDecimal.ZERO;
		}

		if (despesa == null) {
			despesa = BigDecimal.ZERO;
		}

		return receita.subtract(despesa);
	}
}
