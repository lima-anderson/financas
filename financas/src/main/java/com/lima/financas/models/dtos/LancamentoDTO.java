package com.lima.financas.models.dtos;

import java.math.BigDecimal;

import com.lima.financas.models.Usuario;
import com.lima.financas.models.enums.StatusLancamento;
import com.lima.financas.models.enums.TipoLancamento;

public class LancamentoDTO {

	private Long id;
	private String descricao;
	private Integer mes;
	private Integer ano;
	private Long usuario;
	private BigDecimal valor;
	private TipoLancamento tipo;
	private StatusLancamento status;

	public LancamentoDTO() {
	}

	public LancamentoDTO(String descricao, Integer mes, Integer ano, Long usuario, BigDecimal valor,
			TipoLancamento tipo, StatusLancamento status) {
		this.descricao = descricao;
		this.mes = mes;
		this.ano = ano;
		this.usuario = usuario;
		this.valor = valor;
		this.tipo = tipo;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Long getUsuario() {
		return usuario;
	}

	public void setUsuario(Long usuario) {
		this.usuario = usuario;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public TipoLancamento getTipo() {
		return tipo;
	}

	public void setTipo(TipoLancamento tipo) {
		this.tipo = tipo;
	}

	public StatusLancamento getStatus() {
		return status;
	}

	public void setStatus(StatusLancamento status) {
		this.status = status;
	}

}
