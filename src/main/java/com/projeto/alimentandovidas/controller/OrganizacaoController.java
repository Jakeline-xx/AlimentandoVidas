package com.projeto.alimentandovidas.controller;

import com.projeto.alimentandovidas.exception.RestNotFoundException;
import com.projeto.alimentandovidas.model.Organizacao;
import com.projeto.alimentandovidas.repository.OrganizacaoRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Slf4j
@RequestMapping("/alimentandovidas/api/organizacao")
public class OrganizacaoController {
    @Autowired
    OrganizacaoRepository organizacaoRepository;

    @Autowired
    PagedResourcesAssembler<Object> assembler;

    //METODO PARA BUSCAR TODAS AS ORGANIZACOES DO MESMO ESTADO UTILIZANDO PAGINACAO
    @GetMapping
    public Page<Organizacao> index(@RequestParam(required = false) String estado, @PageableDefault(size = 10) Pageable pageable){
        if (estado == null) return organizacaoRepository.findAll(pageable);
        return organizacaoRepository.findByEstadoContaining(estado, pageable);
    }

    @GetMapping("{id}")
    public EntityModel<Organizacao> show(@PathVariable Long id){
        log.info("buscando organizacao com id " + id);
        var organizacao = organizacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "organizacao não encontrada"));

        return organizacao.toModel();
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid Organizacao organizacao){
        log.info("cadastrando organizacao: " + organizacao);
        organizacao.setStatus("ATIVO");
        organizacaoRepository.save(organizacao);
        return ResponseEntity
                .created(organizacao.toModel().getRequiredLink("self").toUri())
                .body(organizacao.toModel());
    }

    @PutMapping("{id}")
    public EntityModel<Organizacao> update(@PathVariable Long id, @RequestBody @Valid Organizacao organizacao){
        log.info("alterando organizacao com id " + id);
        organizacaoRepository.findById(id)
                .orElseThrow(() -> new RestNotFoundException("organizacao não encontrada"));

        //organizacao.setStatus("ATIVO");
        organizacao.setId(id);
        organizacaoRepository.save(organizacao);

        return organizacao.toModel();
    }
}