package com.example.financas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.financas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
