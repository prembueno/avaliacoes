package br.insper.avaliacoes.avaliacao;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final List<AvaliacaoObserver> observers = new ArrayList<>();

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository, List<AvaliacaoObserver> observers) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.observers.addAll(observers);
    }

    public void adicionarObserver(AvaliacaoObserver observer) {
        observers.add(observer);
    }

    public void removerObserver(AvaliacaoObserver observer) {
        observers.remove(observer);
    }

    public List<AvaliacaoObserver> getObservers() {
        return List.copyOf(observers);
    }

    private void notificarObservers(TipoOperacao tipoOperacao, Avaliacao avaliacao) {
        for (AvaliacaoObserver observer : observers) {
            observer.notificar(tipoOperacao, avaliacao);
        }
    }

    public Avaliacao criar(Avaliacao avaliacao) {
        avaliacao.setId(null);
        avaliacao.setDataAvaliacao(LocalDateTime.now());
        Avaliacao salva = avaliacaoRepository.save(avaliacao);
        notificarObservers(TipoOperacao.CREATE, salva);
        return salva;
    }

    public List<Avaliacao> listar() {
        return avaliacaoRepository.findAll();
    }

    public Avaliacao buscarPorId(Long id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Avaliação não encontrada"));
    }

    public void excluir(Long id) {
        Avaliacao avaliacao = buscarPorId(id);
        avaliacaoRepository.delete(avaliacao);
        notificarObservers(TipoOperacao.DELETE, avaliacao);
    }
}
