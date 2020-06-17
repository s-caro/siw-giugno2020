package it.uniroma3.siw.progetto.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.progetto.model.Commento;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.repository.CommentoRepository;

@Service
public class CommentoService {
	
	@Autowired
	private CommentoRepository commentoRepository;
	
	@Transactional
	public Commento saveCommento(Commento commento) {
		return this.commentoRepository.save(commento);
	}
	
	@Transactional
	public void deleteCommento(Commento commento) {
		this.commentoRepository.delete(commento);
	}
	
	@Transactional
	public Commento getCommento(Long id) {
		Optional<Commento> result = this.commentoRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public List<Commento> findAllCommenti(){
		Iterable<Commento> i = this.commentoRepository.findAll();
		List<Commento> lista = new ArrayList<>();
		for (Commento u : i) {
			lista.add(u);
		}
		return lista;
	}
	
	@Transactional
	public List<Commento> findByTask(Task task){
		return this.commentoRepository.findByTask(task);
	}
	
}
