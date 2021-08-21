package curso.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private ImplementacaoUserDetailsService implementacaoUserDetailsService;


    @Override // Configura as solicitações de acesso por Http
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable() //Desabilita as configurações padrão de memória.
                .authorizeRequests() // Permiti Restringir os acessos.
                .antMatchers(HttpMethod.GET, "/").permitAll() // Qualquer usuário acessa a página inicial
                .antMatchers(HttpMethod.GET, "/cadastropessoa").hasAnyRole("ADMIN")// Restringe o acesso
                .anyRequest().authenticated()
                .and().formLogin().permitAll() // Permiti qualquer usuário
                .loginPage("/login")// página de login personalizada
                .defaultSuccessUrl("/cadastropessoa")// página que será acessada após logar com sucesso
                .failureUrl("/login?error=true")//caso deia erre no login
                .and().logout().logoutSuccessUrl("/login")//Mapeia URL de logout do sistema e invalida usuário n autenticado
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
    }

    @Override //Cria autenticação do usuário com banco de dados ou em memória
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(implementacaoUserDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());

       /* auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin")
                .password("$2a$10$ue4ZimMXaQMJbtkXcTTWdefG3mQkxRX42nrxfoQDgxFFakYw8AO0O")
                .roles("ADMIN");*/
    }

    @Override //ignora URL especificas
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/materialize/**");
    }
}
