package br.insper.avaliacoes.avaliacao;

public interface AvaliacaoObserver {

    void notificar(TipoOperacao tipoOperacao, Avaliacao avaliacao);
}
