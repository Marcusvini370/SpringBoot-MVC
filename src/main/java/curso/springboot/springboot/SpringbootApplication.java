package curso.springboot.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = ("curso.springboot.model")) // faz o escaneamento das entidades do projeto
@ComponentScan(basePackages = {"curso.*"}) // mapeia todos os pacotes como os controllers
@EnableJpaRepositories(basePackages = {"curso.springboot.repository"}) // para reconhecer os repositorys
@EnableTransactionManagement // habilita a parte de transações do banco de dados
public class SpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

}
