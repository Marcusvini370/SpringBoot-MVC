package curso.springboot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
public class Pessoa implements Serializable {

  private static long  serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;
    private String sobrenome;
    private Integer idade;

    @OneToMany(mappedBy = "pessoa")
    private List<Telefone> telefones;

}
