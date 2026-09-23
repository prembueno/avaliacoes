package br.insper.avaliacoes.avaliacao;

import br.insper.avaliacoes.auditoria.Auditoria;
import br.insper.avaliacoes.auditoria.AuditoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AvaliacaoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @BeforeEach
    void limparBanco() {
        auditoriaRepository.deleteAll();
        avaliacaoRepository.deleteAll();
    }

    @Test
    void postDeveCriarAvaliacaoERegistrarAuditoria() throws Exception {
        String json = """
                {
                  "autor": "Prem",
                  "conteudo": "Atendimento excelente",
                  "nota": 5
                }
                """;

        mockMvc.perform(post("/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.autor").value("Prem"))
                .andExpect(jsonPath("$.conteudo").value("Atendimento excelente"))
                .andExpect(jsonPath("$.nota").value(5))
                .andExpect(jsonPath("$.dataAvaliacao").exists());

        assertEquals(1, avaliacaoRepository.count());

        List<Auditoria> auditorias = auditoriaRepository.findAll();
        assertEquals(1, auditorias.size());
        assertEquals(TipoOperacao.CREATE, auditorias.get(0).getTipoOperacao());
    }

    @Test
    void postComNotaInvalidaDeveRetornar400() throws Exception {
        String json = """
                {
                  "autor": "Prem",
                  "conteudo": "Nota fora do intervalo",
                  "nota": 7
                }
                """;

        mockMvc.perform(post("/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        assertEquals(0, avaliacaoRepository.count());
        assertEquals(0, auditoriaRepository.count());
    }
}
