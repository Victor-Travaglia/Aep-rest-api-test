package unicesumar.segundoBimestre;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PessoaController controller;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testFindAllTest() throws Exception {
        var pessoa1 = new Pessoa("pessoa1", 10);
        var id1 = pessoa1.getId();
        var pessoa2 = new Pessoa("pessoa2", 20);
        var id2 = pessoa2.getId();
        var pessoa3 = new Pessoa("pessoa3", 30);
        var id3 = pessoa3.getId();

        when(controller.findAll()).thenReturn(ResponseEntity.ok(Arrays.asList(pessoa1, pessoa2, pessoa3)));

        mockMvc.perform(get("/api/pessoas"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].id").value(id1))
                .andExpect(jsonPath("$.[1].id").value(id2))
                .andExpect(jsonPath("$.[2].id").value(id3))
                .andExpect(jsonPath("$.[0].nome").value("pessoa1"))
                .andExpect(jsonPath("$.[1].nome").value("pessoa2"))
                .andExpect(jsonPath("$.[2].nome").value("pessoa3"))
                .andExpect(jsonPath("$.[0].idade").value("10"))
                .andExpect(jsonPath("$.[1].idade").value("20"))
                .andExpect(jsonPath("$.[2].idade").value("30"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindById() throws Exception {
        var pessoa = new Pessoa("pessoa", 25);

        when(controller.findById(pessoa.getId())).thenReturn(ResponseEntity.ok(pessoa));

        mockMvc.perform(get("/api/pessoas/" + pessoa.getId()))
                .andExpect(jsonPath("$.id").value(pessoa.getId()))
                .andExpect(jsonPath("$.nome").value("pessoa"))
                .andExpect(jsonPath("$.idade").value(25))
                .andExpect(status().isOk());
    }

    @Test
    public void testPost() throws Exception {
        var pessoa = new Pessoa("pessoa", 25);
        var json = objectMapper.writeValueAsString(pessoa);

        when(controller.post(ArgumentMatchers.any(Pessoa.class))).thenReturn(new ResponseEntity<>(pessoa, HttpStatus.CREATED));

        mockMvc.perform(post("/api/pessoas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.id").value(pessoa.getId()))
                .andExpect(jsonPath("$.nome").value("pessoa"))
                .andExpect(jsonPath("$.idade").value(25))
                .andExpect(status().isCreated());
    }

    @Test
    public void testPut() throws Exception {
        var pessoa = new Pessoa("pessoa", 25);
        var pessoaAtualizada = pessoa;
        var json = objectMapper.writeValueAsString(pessoaAtualizada);

        pessoaAtualizada.setNome("atualizada");
        pessoaAtualizada.setIdade(30);

        when(controller.put(pessoa.getId(), pessoaAtualizada)).thenReturn(ResponseEntity.ok(pessoaAtualizada));

        mockMvc.perform(put("/api/pessoas")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", pessoa.getId())
                .content(json))
                .andExpect(jsonPath("$.id").value(pessoaAtualizada.getId()))
                .andExpect(jsonPath("$.nome").value("atualizada"))
                .andExpect(jsonPath("$.idade").value(30));
    }

    @Test
    public void testDelete() throws Exception {
        var pessoa = new Pessoa("pessoa", 25);
        when(controller.delete(pessoa.getId())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(delete("/api/pessoas")
                .param("id", pessoa.getId()))
                .andExpect(status().isOk());
    }
}
