package com.projeto.alimentandovidas.repository;

import com.projeto.alimentandovidas.model.AcaoSocial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcaoSocialRepository extends JpaRepository<AcaoSocial, Long> {

}