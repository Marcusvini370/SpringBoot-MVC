package curso.springboot.repository;

import curso.springboot.model.Pessoa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository // declara a classe como um repository
@Transactional // para controlar toda parte de persistencia
public interface PessoaRepository extends CrudRepository<Pessoa, Long> {



}
