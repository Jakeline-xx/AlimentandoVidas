package com.projeto.alimentandovidas.repository;

import com.projeto.alimentandovidas.model.AcaoSocial;
import com.projeto.alimentandovidas.model.Organizacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcaoSocialRepository extends JpaRepository<AcaoSocial, Long> {
    Page<AcaoSocial> findByOrganizacao(Organizacao organizacao, Pageable pageable);
}