package curso.springboot.controller;

import curso.springboot.model.Pessoa;
import curso.springboot.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @RequestMapping(method = RequestMethod.GET, value="/cadastropessoa")
    public String inicio(){
        return "cadastro/cadastropessoa";
    }

    @PostMapping(value="/salvarpessoa")
    public ModelAndView salvar(Pessoa pessoa){
        pessoaRepository.save(pessoa);

        //Após o cadastro irá listas as pessoas já cadastradas
        ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
        Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
        andView.addObject("pessoas", pessoasIt);

        return andView;
    }

    @GetMapping(value="/listapessoas")
    public ModelAndView pessoas(){
            ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
            Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
            andView.addObject("pessoas", pessoasIt);

            return andView;
    }

}
