package curso.springboot.controller;

import curso.springboot.model.Pessoa;
import curso.springboot.repository.PessoaRepository;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @RequestMapping(method = RequestMethod.GET, value="/cadastropessoa")
    public ModelAndView inicio(){

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
        modelAndView.addObject("pessoaobj", new Pessoa());

        return modelAndView;
    }

    @PostMapping(value="**/salvarpessoa") // os dois ** faz ignorar tudo que vem antes da url /salvarpessoa
    public ModelAndView salvar(Pessoa pessoa){
        pessoaRepository.save(pessoa);

        //Após o cadastro irá listas as pessoas já cadastradas
        ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
        Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
        andView.addObject("pessoas", pessoasIt);
        andView.addObject("pessoaobj", new Pessoa());

        return andView;
    }

    @GetMapping(value="/listapessoas")
    public ModelAndView pessoas(){
            ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
            Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
            andView.addObject("pessoas", pessoasIt);
            andView.addObject("pessoaobj", new Pessoa());
            return andView;
    }

    @GetMapping("/editarpessoa/{idpessoa}")
    public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa){

        Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
        modelAndView.addObject("pessoaobj", pessoa.get());
        return modelAndView;
    }

    @GetMapping("**/removerpessoa/{idpessoa}")
    public ModelAndView remover(@PathVariable("idpessoa") Long idpessoa){

        pessoaRepository.deleteById(idpessoa);

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
        modelAndView.addObject("pessoas", pessoaRepository.findAll());
        modelAndView.addObject("pessoaobj", new Pessoa());
        return modelAndView;
    }

    @PostMapping("**/pesquisarpessoa")
    public  ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa){
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
        modelAndView.addObject("pessoas", pessoaRepository.findPessoaByName(nomepesquisa));
        modelAndView.addObject("pessoaobj", new Pessoa());

        return modelAndView;
    }

}
