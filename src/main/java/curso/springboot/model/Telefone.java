package curso.springboot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String tipo;
    private String numero;

    @ManyToOne
    @org.hibernate.annotations.ForeignKey(name = "pessoa_id")
    private Pessoa pessoa;


}
