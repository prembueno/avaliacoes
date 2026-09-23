package br.insper.avaliacoes.auditoria;

import br.insper.avaliacoes.avaliacao.TipoOperacao;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoOperacao tipoOperacao;

    private Long avaliacaoId;

    private LocalDateTime timestamp;

    public Auditoria() {
    }

    public Auditoria(TipoOperacao tipoOperacao, Long avaliacaoId, LocalDateTime timestamp) {
        this.tipoOperacao = tipoOperacao;
        this.avaliacaoId = avaliacaoId;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public TipoOperacao getTipoOperacao() {
        return tipoOperacao;
    }

    public Long getAvaliacaoId() {
        return avaliacaoId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
