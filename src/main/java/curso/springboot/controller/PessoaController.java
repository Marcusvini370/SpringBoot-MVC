package curso.springboot.controller;

import curso.springboot.model.Pessoa;
import curso.springboot.model.Telefone;
import curso.springboot.repository.PessoaRepository;
import curso.springboot.repository.ProfissaoRepository;
import curso.springboot.repository.TelefoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private ReportUtil reportUtil;

    @Autowired
    private ProfissaoRepository profissaoRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
    public ModelAndView inicio() {

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
        modelAndView.addObject("pessoaobj", new Pessoa());
        Iterable<Pessoa> pessoasIt = pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")));
        modelAndView.addObject("pessoas", pessoasIt);

        // Quando abrir a tela o sistema irá carregar as opções da profissão
        modelAndView.addObject("profissoes", profissaoRepository.findAll());

        return modelAndView;
    }
    // os dois ** faz ignorar tudo que vem antes da url /salvarpessoa
    @PostMapping(value = "**/salvarpessoa", consumes = {"multipart/form-data"}) // o consume é pra aceitar o upload no form
    public ModelAndView salvar(@Valid Pessoa pessoa,
                               BindingResult bindingResult, final MultipartFile file) throws IOException {

        pessoa.setTelefones(telefoneRepository.getTelefones(pessoa.getId()));

        //validações @Valid pra usar as validações do model
        //bindingResult objetos de erro
        if (bindingResult.hasErrors()) { //se tiver erro vai entrar aqui dentro

            ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
            modelAndView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));

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

        if(file.getSize() > 0){ //Cadastrando um currículo
            pessoa.setCurriculo(file.getBytes());
            pessoa.setTipoFileCurriculo(file.getContentType());
            pessoa.setNomeFileCurriculo(file.getOriginalFilename());
        }else{
            if(pessoa.getId() != null && pessoa.getId() >0){ //editando
                Pessoa pessoaTemp = pessoaRepository.findById(pessoa.getId()).get(); //carrega a pessoa do bd

                pessoa.setCurriculo(pessoaTemp.getCurriculo()); // mantém o curriculo

                pessoa.setTipoFileCurriculo(pessoaTemp.getTipoFileCurriculo());
                pessoa.setNomeFileCurriculo(pessoaTemp.getNomeFileCurriculo());
            }
        }

        pessoaRepository.save(pessoa);

        //Após o cadastro irá listas as pessoas já cadastradas
        ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
        andView.addObject("pessoas",pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
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

        // Quando abrir a tela o sistema irá carregar as opções da profissão
        modelAndView.addObject("profissoes", profissaoRepository.findAll());

        return modelAndView;
    }

    @GetMapping("**/removerpessoa/{idpessoa}")
    public ModelAndView remover(@PathVariable("idpessoa") Long idpessoa) {

        pessoaRepository.deleteById(idpessoa);

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
        modelAndView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
        modelAndView.addObject("pessoaobj", new Pessoa());
        return modelAndView;
    }

    @PostMapping("**/pesquisarpessoa")
    public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa,
                                  @RequestParam("pesqsexo") String pesqsexo) {

        List<Pessoa> pessoas = new ArrayList<Pessoa>();

        //Filtro de busca por sexo caso tenha selecionado
        if (pesqsexo != null && !pesqsexo.isEmpty()) {
            pessoas = pessoaRepository.findPessoaByNameSexo(nomepesquisa, pesqsexo);

        } else {//Caso não tenha selecionado a opção do sexo irá só  pesquisar por nome
            pessoas = pessoaRepository.findPessoaByName(nomepesquisa);
        }

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
        modelAndView.addObject("pessoas", pessoas); //vai passar pela lista
        modelAndView.addObject("pessoaobj", new Pessoa()) ;

        return modelAndView;
    }

    @GetMapping("**/baixarcurriculo/{idpessoa}")
        public void baixarcurriculo(@PathVariable("idpessoa") Long idpessoa,
        HttpServletResponse response) throws IOException {

        /* Consultar objeto pessoa no banco de dados */
        Pessoa pessoa = pessoaRepository.findById(idpessoa).get();
        if(pessoa.getCurriculo() != null){

            /* Setar tamanho da resposta*/
            response.setContentLength(pessoa.getCurriculo().length);

            // Tipo de arquivo para download ou pode ser generica application/octet-stream
            response.setContentType(pessoa.getTipoFileCurriculo());

            /* Define o cabeçalho da resposta */
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", pessoa.getNomeFileCurriculo());
                    response.setHeader(headerKey, headerValue);

                /* Finaliza a resposta passando o arquivo */
            response.getOutputStream().write(pessoa.getCurriculo());

        }

    }


    @GetMapping("**/pesquisarpessoa")
    public void imprimiPdf(@RequestParam("nomepesquisa") String nomepesquisa,
                           @RequestParam("pesqsexo") String pesqsexo,
                           HttpServletRequest request, HttpServletResponse response) throws Exception {

        //lista de pessoas o msm que foi criado com o jasper
        List<Pessoa> pessoas = new ArrayList<Pessoa>();

        if (pesqsexo != null && !pesqsexo.isEmpty()
                && nomepesquisa != null && !nomepesquisa.isEmpty()) { //busca por nome e sexo

            pessoas = pessoaRepository.findPessoaByNameSexo(nomepesquisa.trim().toUpperCase(), pesqsexo);

        } else if (nomepesquisa != null && !nomepesquisa.isEmpty()) { //busca somente por nome

            pessoas = pessoaRepository.findPessoaByName(nomepesquisa.trim().toUpperCase());

        } else if (pesqsexo != null && !pesqsexo.isEmpty()) { //busca somente por sexo

            pessoas = pessoaRepository.findPessoaBySexo(pesqsexo);

        } else {  // busca todos
            Iterable<Pessoa> iterator = pessoaRepository.findAll();

            for (Pessoa pessoa : iterator) {
                pessoas.add(pessoa); //vai adicionar as pessoa desse iterator pra lista de pessoas
            }
        }

        //chamar o  serviço que faz a geração do relatorio
        byte[] pdf = reportUtil.gerarRelatorio(pessoas, "pessoa", request.getServletContext());

        //tamanho da resposta
        response.setContentLength(pdf.length);

        //definir na resposta o tipo de arquivo aquivo pdf arquivo de midia
        response.setContentType("application/octet-stream");

        //Definir o cabeçalho da resposta
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
        response.setHeader(headerKey, headerValue);

        //finaliza a resposta pro navegador
        response.getOutputStream().write(pdf);
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
