package curso.springboot.repository;

import curso.springboot.model.Pessoa;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    default Page<Pessoa> findPessoaByNamePage(String nome, Pageable pageable) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nome);

        //se vai conter um valor , estamos configurando a pesquisa para consultar por partes do nome no banco de dados
        //igual a like com sql,  ignoreCase() vai ignorar maiusculo e minusculo e vai ser o nome
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        //fazendo a união do objeto como o valor e a configuração para consultar
        Example<Pessoa> example = Example.of(pessoa, exampleMatcher);

        Page<Pessoa> pessoas = findAll(example, pageable);

        return pessoas;
    }

    default Page<Pessoa> findPessoaByNameSexoPage(String nome, String sexo, Pageable pageable) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nome);
        pessoa.setSexopessoa(sexo);

        //se vai conter um valor , estamos configurando a pesquisa para consultar por partes do nome no banco de dados
        //igual a like com sql,  ignoreCase() vai ignorar maiusculo e minusculo e vai ser o nome
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("sexopessoaq", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        //fazendo a união do objeto como o valor e a configuração para consultar
        Example<Pessoa> example = Example.of(pessoa, exampleMatcher);

        Page<Pessoa> pessoas = findAll(example, pageable);

        return pessoas;
    }

}
