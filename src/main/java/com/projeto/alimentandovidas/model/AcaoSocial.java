package com.projeto.alimentandovidas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;

@Entity
public class AcaoSocial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_acao_social")
    private Long idAcaoSocial;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id")
    @Fetch(FetchMode.JOIN)
    private Organizacao organizacao;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;
}
