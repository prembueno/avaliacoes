package br.insper.avaliacoes.auditoria;

import br.insper.avaliacoes.avaliacao.Avaliacao;
import br.insper.avaliacoes.avaliacao.AvaliacaoObserver;
import br.insper.avaliacoes.avaliacao.TipoOperacao;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuditoriaObserver implements AvaliacaoObserver {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaObserver(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    @Override
    public void notificar(TipoOperacao tipoOperacao, Avaliacao avaliacao) {
        auditoriaRepository.save(new Auditoria(tipoOperacao, avaliacao.getId(), LocalDateTime.now()));
    }
}
