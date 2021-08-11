package com.example.financas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.financas.model.entity.Lancamento;
import com.example.financas.model.enums.StatusLancamento;
import com.example.financas.model.enums.TipoLancamento;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

	@Autowired
	LancamentoRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	
	public static Lancamento criarLancamento() {
		return Lancamento.builder()
							.ano(2021)
							.mes(1)
							.descricao("lancamento qualquer")
							.valor(BigDecimal.valueOf(10))
							.tipo(TipoLancamento.RECEITA)
							.status(StatusLancamento.PENDENTE)
							.dataCadastro(LocalDate.now())
							.build();
	}
	
	
	@Test
	public void deveSalvarUmLancamento () {
		Lancamento lancamento = criarLancamento();
		
		lancamento = repository.save(lancamento);
		
		Assertions.assertThat(lancamento.getId()).isNotNull();
	}

	
	@Test 
	public void deveDeletarUmLancamento() {
		Lancamento lancamento = criarLancamento();
		
		entityManager.persist(lancamento);
		
		lancamento = entityManager.find(Lancamento.class, lancamento.getId());
		
		repository.delete(lancamento);
		
		Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
		Assertions.assertThat(lancamentoInexistente).isNull();
	}
	
	@Test
	public void deveAtualizarUmLancamento () {
		Lancamento lancamento = criarLancamento();
		entityManager.persist(lancamento);
		
		lancamento.setAno(2018);
		lancamento.setDescricao("teste ");
		lancamento.setStatus(StatusLancamento.CANCELADO);
		
		repository.save(lancamento);
		
		Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());
		
		Assertions.assertThat(lancamentoAtualizado.getAno()).isEqualTo(2018);
		Assertions.assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("teste ");
		Assertions.assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
	}
	
	@Test
	public void deveBuscarUmLancamentoPorId () {
		Lancamento lancamento = criarLancamento();
		entityManager.persist(lancamento);
		
		Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());
		
		Assertions.assertThat(lancamentoEncontrado.isPresent()).isTrue();
	}
}
