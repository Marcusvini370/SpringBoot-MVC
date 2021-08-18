package curso.springboot.controller;

import curso.springboot.model.Pessoa;
import curso.springboot.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @RequestMapping(method = RequestMethod.GET, value="/cadastropessoa")
    public String inicio(){
        return "cadastro/cadastropessoa";
    }

    @PostMapping(value="/salvarpessoa")
    public String salvar(Pessoa pessoa){
        pessoaRepository.save(pessoa);
        return "cadastro/cadastropessoa";
    }


}
