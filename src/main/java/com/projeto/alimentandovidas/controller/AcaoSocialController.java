package com.projeto.alimentandovidas.controller;

import com.projeto.alimentandovidas.model.AcaoSocial;
import com.projeto.alimentandovidas.model.Organizacao;
import com.projeto.alimentandovidas.repository.AcaoSocialRepository;
import com.projeto.alimentandovidas.repository.OrganizacaoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/alimentandovidas/api/acao-social")
public class AcaoSocialController {
    @Autowired
    AcaoSocialRepository acaoSocialRepository;

    @Autowired
    OrganizacaoRepository organizacaoRepository;

    @Autowired
    PagedResourcesAssembler<Object> assembler;

    @GetMapping("organizacoes")
    @Operation(
            summary = "Lista de organizações",
            description = "Retorna uma lista paginada de todas organizações, ou apenas com mesmo estado"
    )
    public PagedModel<EntityModel<Object>> indexOrganizacoes(@RequestParam(required = false) String estado, @ParameterObject @PageableDefault(size = 5) Pageable pageable){
        Page<Organizacao> organizacoes = (estado == null)?
                organizacaoRepository.findAll(pageable):
                organizacaoRepository.findByEstadoContaining(estado, pageable);

        return assembler.toModel(organizacoes.map(Organizacao::toModel));
    }

    @GetMapping("acoes-sociais/{id}")
    @Operation(
            summary = "Lista de ações sociais de uma organização",
            description = "Retorna todas as ações sociais de uma organização específica"
    )
    public List<AcaoSocial> indexAcoesSociais(@PathVariable Long id) {
        Organizacao organizacao = organizacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organização não encontrada"));

        List<AcaoSocial> acoesSociais = acaoSocialRepository.findByOrganizacaoId(id);

        return acoesSociais;
    }


    @GetMapping("{id}")
    @Operation(
            summary = "Detalhes da ação social",
            description = "Retorna os dados de uma ação social com o ID especificado"
    )
    public EntityModel<AcaoSocial> show(@PathVariable Long id) {
        log.info("Buscando ação social com id " + id);
        AcaoSocial acaoSocial = acaoSocialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ação social não encontrada"));

        return acaoSocial.toModel();
    }


    //TODO POSSO PENSAR EM RECEBER APENAS O ID E DEIXAR ORGANIZAÇÃO NULL VINDO DO FRONT, PRA VALIDAR PELO ID
    @PostMapping
    @Operation(
            summary = "Cadastro de ação social",
            description = "Realiza o cadastro de uma nova ação social"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ação social cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na validação dos dados da requisição")
    })
    public ResponseEntity<Object> create(@RequestBody @Valid AcaoSocial acaoSocial) {
        log.info("Realizando cadastro da ação social: " + acaoSocial);

        Organizacao organizacao = organizacaoRepository.findById(acaoSocial.getOrganizacao().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organização não encontrada"));

        acaoSocial.setOrganizacao(organizacao);
        acaoSocial.setDataCadastro(LocalDateTime.now());
        acaoSocialRepository.save(acaoSocial);

        return ResponseEntity
                .created(acaoSocial.toModel().getRequiredLink("self").toUri())
                .body(acaoSocial.toModel());
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Atualização da ação social",
            description = "Atualiza os dados de uma ação social com o Id especificado"
    )
    public EntityModel<AcaoSocial> update(@PathVariable Long id, @RequestBody @Valid AcaoSocial acaoSocial) {
        log.info("Alterando ação social com id " + id);

        AcaoSocial acaoSocialAnterior = acaoSocialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ação social não encontrada"));

        acaoSocial.setId(id);
        acaoSocial.setDataCadastro(acaoSocialAnterior.getDataCadastro());
        acaoSocialRepository.save(acaoSocial);

        return acaoSocial.toModel();
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Exclusão da ação social",
            description = "Exclui uma ação social com o Id especificado"
    )
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        log.info("Excluindo ação social com id " + id);

        AcaoSocial acaoSocial = acaoSocialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ação social não encontrada"));

        acaoSocialRepository.delete(acaoSocial);

        return ResponseEntity.noContent().build();
    }

}
