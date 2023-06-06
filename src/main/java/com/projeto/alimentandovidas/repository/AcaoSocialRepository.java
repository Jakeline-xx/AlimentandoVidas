package com.projeto.alimentandovidas.repository;

import com.projeto.alimentandovidas.model.AcaoSocial;
import com.projeto.alimentandovidas.model.Organizacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcaoSocialRepository extends JpaRepository<AcaoSocial, Long> {
    List<AcaoSocial> findByOrganizacaoId(Long organizacaoId);
}