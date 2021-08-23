package curso.springboot.controller;

import curso.springboot.model.Pessoa;
import curso.springboot.model.Telefone;
import curso.springboot.repository.PessoaRepository;
import curso.springboot.repository.TelefoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
    public ModelAndView inicio() {

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
        modelAndView.addObject("pessoaobj", new Pessoa());
        Iterable<Pessoa> pessoasIt = pessoaRepository.findAll(); // para carrega a lista direto
        modelAndView.addObject("pessoas", pessoasIt);

        return modelAndView;
    }

    @PostMapping(value = "**/salvarpessoa") // os dois ** faz ignorar tudo que vem antes da url /salvarpessoa
    public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult) {

        pessoa.setTelefones(telefoneRepository.getTelefones(pessoa.getId()));

        //validações @Valid pra usar as validações do model
        //bindingResult objetos de erro
        if (bindingResult.hasErrors()) { //se tiver erro vai entrar aqui dentro

            ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
            Iterable<Pessoa> pessoasIterator = pessoaRepository.findAll();
            modelAndView.addObject("pessoas", pessoasIterator);

            //passa o objeto que ta vindo da view vai continuar os dados da pessoa
            //fazer a validação e mostrar os erros que tá tendo
            modelAndView.addObject("pessoaobj", pessoa);


            List<String> msgValidacao = new ArrayList<String>();
            //varre os erros encontrados pelo objetos de erros e a lista do binding
            for (ObjectError objectError : bindingResult.getAllErrors()) {

                //getDefaultMessage() vem das anotações da model NotBlank e outras
                msgValidacao.add(objectError.getDefaultMessage());
            }

            modelAndView.addObject("msg", msgValidacao);

            return modelAndView;
        }

        pessoaRepository.save(pessoa);

        //Após o cadastro irá listas as pessoas já cadastradas
        ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
        Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
        andView.addObject("pessoas", pessoasIt);
        andView.addObject("pessoaobj", new Pessoa());

        return andView;
    }

    @GetMapping(value = "/listapessoas")
    public ModelAndView pessoas() {
        ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
        Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
        andView.addObject("pessoas", pessoasIt);
        andView.addObject("pessoaobj", new Pessoa());
        return andView;
    }

    @GetMapping("/editarpessoa/{idpessoa}")
    public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa) {

        Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
        modelAndView.addObject("pessoaobj", pessoa.get());
        return modelAndView;
    }

    @GetMapping("**/removerpessoa/{idpessoa}")
    public ModelAndView remover(@PathVariable("idpessoa") Long idpessoa) {

        pessoaRepository.deleteById(idpessoa);

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
        modelAndView.addObject("pessoas", pessoaRepository.findAll());
        modelAndView.addObject("pessoaobj", new Pessoa());
        return modelAndView;
    }

    @PostMapping("**/pesquisarpessoa")
    public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa,
                                  @RequestParam("pesqsexo") String pesqsexo) {

        List<Pessoa> pessoas = new ArrayList<Pessoa>();

        //Filtro de busca por sexo caso tenha selecionado
        if(pesqsexo != null && !pesqsexo.isEmpty()){
            pessoas = pessoaRepository.findPessoaByNameSexo(nomepesquisa,pesqsexo);

        }else{//Caso não tenha selecionado a opção do sexo irá só  pesquisar por nome
            pessoas = pessoaRepository.findPessoaByName(nomepesquisa);
        }

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
        modelAndView.addObject("pessoas", pessoas); //vai passar pela lista
        modelAndView.addObject("pessoaobj", new Pessoa());

        return modelAndView;
    }

    @GetMapping("**/telefones/{idpessoa}")
    public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa) {

        Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);

        ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
        modelAndView.addObject("pessoaobj", pessoa.get());
        modelAndView.addObject("telefones", telefoneRepository.getTelefones(idpessoa));
        return modelAndView;
    }

    @PostMapping("**/addfonePessoa/{pessoaid}")
    public ModelAndView addFonePessoa(Telefone telefone, @PathVariable("pessoaid") Long pessoaid) {

        Pessoa pessoa = pessoaRepository.findById(pessoaid).get();

        //Validação manual dos campos do telefone sem afetar outras partes do sistema
        if (telefone != null && telefone.getNumero().isEmpty() || telefone.getTipo().isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
            modelAndView.addObject("pessoaobj", pessoa);
            modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));

            List<String> msg = new ArrayList<String>();
            if (telefone.getNumero().isEmpty()) {
                msg.add("Número deve ser informado");
            }
            if (telefone.getTipo().isEmpty()) {
                msg.add("Tipo deve ser informado");
            }


            modelAndView.addObject("msg", msg);

            return modelAndView;
        }

        telefone.setPessoa(pessoa);
        telefoneRepository.save(telefone);

        ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
        modelAndView.addObject("pessoaobj", pessoa);
        modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
        return modelAndView;
    }

    @GetMapping("**/removertelefone/{idtelefone}")
    public ModelAndView removertelefone(@PathVariable("idtelefone") Long idtelefone) {

        Pessoa pessoa = telefoneRepository.findById(idtelefone).get().getPessoa();

        telefoneRepository.deleteById(idtelefone);

        ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
        modelAndView.addObject("pessoaobj", pessoa);
        modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoa.getId()));
        return modelAndView;
    }

}
