package curso.springboot.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Pessoa implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotBlank(message = "Nome não pode ser vazio") //não pode cadastrar sem conteúdo
    private String nome;

    @NotNull(message = "Sobrenome não pode ser nulo") // podser ser cadastrado com espaços em branco
    @NotEmpty(message = "Sobrenome não pode ser vazio") // podser ser cadastrado com espaços em branco
    private String sobrenome;

    @Min(value = 18, message = "Idade inválida")
    private int idade;

    @OneToMany(mappedBy = "pessoa", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Telefone> telefones;

    @ManyToOne
    private Profissao profissao;

    @Enumerated(EnumType.STRING)
    private Cargo cargo;

    private String sexopessoa;

    private String cep;
    private String rua;
    private String bairro;
    private String cidade;
    private String uf;
    private String ibge;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @Lob
    private byte[] curriculo;

    private String nomeFileCurriculo;
    private String tipoFileCurriculo;

}
