package com.blogpessoal.blogpessoal.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.blogpessoal.blogpessoal.model.Postagens;
import com.blogpessoal.blogpessoal.repository.PostagensRepository;

@RestController
@RequestMapping("/postagens")
@CrossOrigin("*")
public class PostagensController {
	
	@Autowired
	private PostagensRepository repository;
	
	@GetMapping
	public ResponseEntity<List<Postagens>> getAll(){
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Postagens> getById(@PathVariable long id){
		return repository.findById(id).map(resp -> ResponseEntity.ok(resp)).orElse(ResponseEntity.notFound().build());
		
	}
	
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagens>> getByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(repository.findAllByTituloContainingIgnoreCase(titulo));
	}

	@PostMapping
	public ResponseEntity<Postagens> post(@Valid @RequestBody Postagens postagens) {
		if (repository.existsById(postagens.getTema().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(postagens));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@PutMapping
	public ResponseEntity<Postagens> put(@Valid @RequestBody Postagens postagens) {
		if (repository.existsById(postagens.getId())) {
			if (repository.existsById(postagens.getTema().getId()))
				return ResponseEntity.status(HttpStatus.OK).body(repository.save(postagens));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		java.util.Optional<Postagens> postagem = repository.findById(id); 

		if (postagem.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		repository.deleteById(id);
	}
	
}
