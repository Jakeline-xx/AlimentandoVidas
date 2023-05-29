package com.projeto.alimentandovidas.repository;

import com.projeto.alimentandovidas.model.Organizacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizacaoRepository extends JpaRepository<Organizacao, Long> {
    Page<Organizacao> findByEstadoContaining(String estado, Pageable pageable);
}