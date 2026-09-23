package br.insper.avaliacoes.avaliacao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvaliacaoServiceTest {

    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    @Mock
    private AvaliacaoObserver observer;

    private AvaliacaoService avaliacaoService;

    @BeforeEach
    void setUp() {
        avaliacaoService = new AvaliacaoService(avaliacaoRepository, List.of(observer));
    }

    @Test
    void criarDeveSalvarDefinirDataENotificarCreate() {
        Avaliacao avaliacao = new Avaliacao("Prem", "Muito bom", 5);
        avaliacao.setId(99L);
        when(avaliacaoRepository.save(any(Avaliacao.class))).thenAnswer(invocacao -> {
            Avaliacao recebida = invocacao.getArgument(0);
            assertNull(recebida.getId());
            recebida.setId(1L);
            return recebida;
        });

        Avaliacao resultado = avaliacaoService.criar(avaliacao);

        assertEquals(1L, resultado.getId());
        assertEquals("Prem", resultado.getAutor());
        assertEquals("Muito bom", resultado.getConteudo());
        assertEquals(5, resultado.getNota());
        assertNotNull(resultado.getDataAvaliacao());
        verify(observer).notificar(TipoOperacao.CREATE, resultado);
    }

    @Test
    void listarDeveRetornarTodas() {
        List<Avaliacao> avaliacoes = List.of(new Avaliacao("A", "x", 3), new Avaliacao("B", "y", 4));
        when(avaliacaoRepository.findAll()).thenReturn(avaliacoes);

        List<Avaliacao> resultado = avaliacaoService.listar();

        assertEquals(2, resultado.size());
        assertEquals(avaliacoes, resultado);
    }

    @Test
    void buscarPorIdDeveRetornarAvaliacao() {
        Avaliacao avaliacao = new Avaliacao("Prem", "Ok", 4);
        avaliacao.setId(1L);
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacao));

        Avaliacao resultado = avaliacaoService.buscarPorId(1L);

        assertEquals(avaliacao, resultado);
    }

    @Test
    void buscarPorIdInexistenteDeveLancar404() {
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> avaliacaoService.buscarPorId(1L));

        assertEquals(404, erro.getStatusCode().value());
    }

    @Test
    void excluirDeveRemoverENotificarDelete() {
        Avaliacao avaliacao = new Avaliacao("Prem", "Ruim", 1);
        avaliacao.setId(1L);
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacao));

        avaliacaoService.excluir(1L);

        verify(avaliacaoRepository).delete(avaliacao);
        verify(observer).notificar(TipoOperacao.DELETE, avaliacao);
    }

    @Test
    void excluirInexistenteDeveLancar404ENaoNotificar() {
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> avaliacaoService.excluir(1L));

        verify(avaliacaoRepository, never()).delete(any());
        verify(observer, never()).notificar(any(), any());
    }

    @Test
    void adicionarERemoverObserver() {
        AvaliacaoObserver outro = (tipo, avaliacao) -> { };

        avaliacaoService.adicionarObserver(outro);
        assertTrue(avaliacaoService.getObservers().contains(outro));
        assertEquals(2, avaliacaoService.getObservers().size());

        avaliacaoService.removerObserver(outro);
        assertFalse(avaliacaoService.getObservers().contains(outro));
        assertEquals(1, avaliacaoService.getObservers().size());
    }
}
