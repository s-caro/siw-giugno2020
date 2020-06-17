package it.uniroma3.siw.progetto.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Tag;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.repository.TagRepository;

@Service
public class TagService {
	
	@Autowired
	private TagRepository tagRepository;
	
	
	@Transactional
	public Tag saveTag(Tag tag) {
		return this.tagRepository.save(tag);
	}
	
	@Transactional
	public void deleteTag(Tag tag) {
		tag.setTask(null);
		this.tagRepository.delete(tag);
	}
	
	@Transactional
	public Tag getTag(Long Id) {
		Optional<Tag> result = this.tagRepository.findById(Id);
		return result.orElse(null);
	}
	
	@Transactional
	public Tag aggiungiTask(Tag tag, Task task) {
		tag.addTask(task);
		return this.tagRepository.save(tag);
	}
	
	
	@Transactional
	public List<Tag> findAllTag(){
		Iterable<Tag> i = this.tagRepository.findAll();
		List<Tag> lista = new ArrayList<>();
		for (Tag u : i) {
			lista.add(u);
		}
		return lista;
	}
	
	@Transactional
	public List<Tag> findByProgetto(Progetto progetto){
		return this.tagRepository.findByProgetto(progetto);
	}
	
	@Transactional
	public List<Tag> findByTask(Task task){
		return this.tagRepository.findByTask(task);
	}
	
}
