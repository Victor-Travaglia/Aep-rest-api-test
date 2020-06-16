package unicesumar.segundoBimestre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    @Autowired
    private PessoaRepository repository;

    @GetMapping
    public ResponseEntity<List<Pessoa>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> findById(@PathVariable String id) {
        return ResponseEntity.ok(repository.findById(id).get());
    }

    @PostMapping
    public ResponseEntity<Pessoa> post(@RequestBody Pessoa pessoa) {
        var pessoaSalva = repository.save(pessoa);

        return new ResponseEntity<>(pessoaSalva, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> put(@PathVariable String id, @RequestBody Pessoa pessoa) {
        var pessoaParaAtualizar = repository.findById(id);

        if (pessoaParaAtualizar.isPresent()) {
            var pessoaAtualizada = pessoaParaAtualizar.get();

            pessoaAtualizada.setNome(pessoa.getNome());
            pessoaAtualizada.setIdade(pessoa.getIdade());

            return ResponseEntity.ok(pessoaAtualizada);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        var pessoaParaDeletar = repository.findById(id);

        if (pessoaParaDeletar.isPresent()) {
            repository.deleteById(id);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
