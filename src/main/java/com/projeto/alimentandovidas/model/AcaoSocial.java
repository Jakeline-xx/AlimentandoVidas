package com.projeto.alimentandovidas.model;

import com.projeto.alimentandovidas.controller.AcaoSocialController;
import com.projeto.alimentandovidas.controller.OrganizacaoController;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;

import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name = "acao_social")
public class AcaoSocial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    @Fetch(FetchMode.JOIN)
    private Organizacao organizacao;

    @NotEmpty(message = "O campo descricao é obrigatório")
    @Column(name = "descricao_completa")
    private String descricaoCompleta;

    @NotEmpty(message = "O campo local é obrigatório")
    @Column(name = "local")
    private String local;

    @NotEmpty(message = "O campo horário de funcionamento é obrigatório")
    @Column(name = "horario_funcionamento")
    private String horarioFuncionamento;

    @NotEmpty(message = "O campo data inicio é obrigatório")
    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @NotEmpty(message = "O campo data fim é obrigatório")
    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @NotEmpty(message = "é necessário informar o público permitido")
    @Column(name = "publicoPermitido")
    private String publicoPermitido;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    public EntityModel<AcaoSocial> toModel() {
        return EntityModel.of(
                this,
                linkTo(methodOn(AcaoSocialController.class).show(id)).withSelfRel(),
                linkTo(methodOn(AcaoSocialController.class).destroy(id)).withRel("delete"),
                linkTo(methodOn(AcaoSocialController.class).indexAcoesSociais(id, Pageable.unpaged())).withRel("acoesSociais")
        );
    }
}
