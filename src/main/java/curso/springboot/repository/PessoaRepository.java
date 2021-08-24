package curso.springboot.repository;

import curso.springboot.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository // declara a classe como um repository
@Transactional // para controlar toda parte de persistencia
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    @Query("select p from Pessoa p where p.nome like %?1%")
    List<Pessoa> findPessoaByName(String nome);

    @Query("select p from Pessoa p where p.sexopessoa = ?1")
    List<Pessoa> findPessoaBySexo(String sexo);

    @Query("select p from Pessoa p where p.nome like %?1% and p.sexopessoa = ?2")
    List<Pessoa> findPessoaByNameSexo(String nome, String sexopessoa);


}
