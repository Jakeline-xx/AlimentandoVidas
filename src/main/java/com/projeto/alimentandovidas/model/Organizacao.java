package com.projeto.alimentandovidas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name = "organizacao")
public class Organizacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "organizacao")
    @Fetch(FetchMode.JOIN)
    @Column(name = "acao_social")
    private List<AcaoSocial> acaoSocialList;

    @Column(name = "status")
    private String status;

    //TODO AO REALIZAR UM PUT, VALIDAR SE O CNPJ DO ID RECEBIDO É O MESMO DO CNPJ RECEBIDO PELO FRONT
    @NotEmpty(message = "O campo cnpj é obrigatório")
    @Column(name = "cnpj")
    private String cnpj;

    @NotEmpty(message = "O campo nome fantasia é obrigatório")
    @Column(name = "nome_fantasia")
    private String nomeFantasia;

    @NotEmpty(message = "O campo estado é obrigatório")
    @Column(name = "estado")
    private String estado;

    @NotEmpty(message = "O campo cidade é obrigatório")
    @Column(name = "cidade")
    private String cidade;

    @NotEmpty(message = "O campo telefone é obrigatório")
    @Column(name = "telefone")
    private String telefone;

//    @Column(name = "email")
//    private String email;
//
//    @Column(name = "senha")
//    private String senha;

    @NotEmpty(message = "O campo descricao é obrigatório")
    @Column(name = "descricao")
    private String descricao;

    @Column(name = "site")
    private String site;

    @Column(name = "chave_pix")
    private String chavePix;


    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;
}