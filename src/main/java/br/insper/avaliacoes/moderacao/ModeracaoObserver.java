package br.insper.avaliacoes.moderacao;

import br.insper.avaliacoes.avaliacao.Avaliacao;
import br.insper.avaliacoes.avaliacao.AvaliacaoObserver;
import br.insper.avaliacoes.avaliacao.TipoOperacao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ModeracaoObserver implements AvaliacaoObserver {

    private static final Logger log = LoggerFactory.getLogger(ModeracaoObserver.class);

    @Override
    public void notificar(TipoOperacao tipoOperacao, Avaliacao avaliacao) {
        if (tipoOperacao == TipoOperacao.CREATE && avaliacao.getNota() <= 2) {
            log.warn("Avaliação negativa feita! id={}, autor={}, nota={}",
                    avaliacao.getId(), avaliacao.getAutor(), avaliacao.getNota());
        }
    }
}
