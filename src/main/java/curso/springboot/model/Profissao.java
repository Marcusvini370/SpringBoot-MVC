package curso.springboot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Profissao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

     private String nome;


}
