package curso.springboot.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = ("curso.springboot.model")) // faz o escaneamento das entidades do projeto
@ComponentScan(basePackages = {"curso.*"}) // mapeia todos os pacotes como os controllers
@EnableJpaRepositories(basePackages = {"curso.springboot.repository"}) // para reconhecer os repositorys
@EnableTransactionManagement // habilita a parte de transações do banco de dados
@EnableWebMvc //habilita todos recursos de mvc
public class SpringbootApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

    @Override // Configuração para interceptar o login do srping security e mandar para a tela de login personalizada
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("/login");
        registry.setOrder(Ordered.LOWEST_PRECEDENCE);
    }
}
