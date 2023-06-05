package com.projeto.alimentandovidas.controller;

import com.projeto.alimentandovidas.exception.RestNotFoundException;
import com.projeto.alimentandovidas.model.AcaoSocial;
import com.projeto.alimentandovidas.model.Organizacao;
import com.projeto.alimentandovidas.repository.AcaoSocialRepository;
import com.projeto.alimentandovidas.repository.OrganizacaoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/alimentandovidas/api/organizacao")
public class OrganizacaoController {
    @Autowired
    OrganizacaoRepository organizacaoRepository;

    @Autowired
    AcaoSocialRepository acaoSocialRepository;

    @Autowired
    PagedResourcesAssembler<AcaoSocial> assembler;

    @GetMapping
    @Operation(
            summary = "Lista de organizações",
            description = "Retorna uma lista paginada de todas organizações, ou apenas com mesmo estado"
    )
    public PagedModel<EntityModel<AcaoSocial>> indexOrganizacoes(@RequestParam(required = false) String estado, @ParameterObject @PageableDefault(size = 5) Pageable pageable){
        Page<Organizacao> organizacoes = (estado == null)?
                organizacaoRepository.findAll(pageable):
                organizacaoRepository.findByEstadoContaining(estado, pageable);

        return assembler.toModel(organizacoes.map(Organizacao::toModel));
    }

    @GetMapping
    @Operation(
            summary = "Lista de ações sociais",
            description = "Retorna uma lista paginada de todas ações sociais, ou de ações sociais de uma organização específica"
    )
    public PagedModel<EntityModel<AcaoSocial>> indexAcoesSociais(
            @RequestParam(required = false) Long idOrganizacao,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        Page<AcaoSocial> acoesSociais = (idOrganizacao == null)?
                acaoSocialRepository.findAll(pageable):
                organizacaoRepository.findById(idOrganizacao)
                        .map(organizacao -> acaoSocialRepository.findByOrganizacao(organizacao, pageable))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organização não encontrada"));

        return assembler.toModel(acoesSociais.map(AcaoSocial::toModel));
    }


    @GetMapping("{id}")
    @Operation(
            summary = "Detalhes da organização",
            description = "Retorna os dados de uma organização com o Id especificado"
    )
    public EntityModel<Organizacao> show(@PathVariable Long id){
        log.info("buscando organizacao com id " + id);
        var organizacao = organizacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "organizacao não encontrada"));

        return organizacao.toModel();
    }

    @PostMapping
    @Operation(
            summary = "Cadastro de organização",
            description = "Realiza o cadastro de uma nova organização"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "organização cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "erro na validação dos dados da requisição")
    })
    public ResponseEntity<Object> create(@RequestBody @Valid Organizacao organizacao){
        log.info("realizando cadastro da organizacao: " + organizacao);
        organizacao.setStatus("ATIVO");
        organizacao.setDataCadastro(LocalDateTime.now());
        organizacaoRepository.save(organizacao);
        return ResponseEntity
                .created(organizacao.toModel().getRequiredLink("self").toUri())
                .body(organizacao.toModel());
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Atualização da organização",
            description = "Atualiza os dados de uma organização com o ID especificado"
    )
    public EntityModel<Organizacao> update(@PathVariable Long id, @RequestBody @Valid Organizacao organizacao){
        log.info("alterando organizacao com id " + id);
        Organizacao organizacaoExistente = organizacaoRepository.findById(id)
                .orElseThrow(() -> new RestNotFoundException("organizacao não encontrada"));

        if (!organizacaoExistente.getCnpj().equals(organizacao.getCnpj())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O CNPJ não pode ser alterado");
        }

        organizacao.setStatus("ATIVO");
        organizacao.setDataCadastro(organizacaoExistente.getDataCadastro());
        organizacao.setId(id);
        organizacaoRepository.save(organizacao);

        return organizacao.toModel();
    }
}
