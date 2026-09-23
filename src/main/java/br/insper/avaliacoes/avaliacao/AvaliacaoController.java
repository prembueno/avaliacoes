package br.insper.avaliacoes.avaliacao;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Avaliacao criar(@RequestBody @Valid Avaliacao avaliacao) {
        return avaliacaoService.criar(avaliacao);
    }

    @GetMapping
    public List<Avaliacao> listar() {
        return avaliacaoService.listar();
    }

    @GetMapping("/{id}")
    public Avaliacao buscarPorId(@PathVariable Long id) {
        return avaliacaoService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        avaliacaoService.excluir(id);
    }
}
