package it.uniroma3.siw.progetto.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Tag;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.ProgettoRepository;

@Service
public class ProgettoService {
	
	@Autowired
	private ProgettoRepository progettoRepository;

	
	@Transactional
	public Progetto getProgetto(Long id) {
		Optional<Progetto> result = this.progettoRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Progetto saveProgetto(Progetto progetto) {
		progetto.setDataInizio(LocalDateTime.now());
		return this.progettoRepository.save(progetto);
	}

	@Transactional
	public void deleteProgetto(Progetto progetto) {
		progetto.setMembri(null);
		this.progettoRepository.delete(progetto);
	}
	
	@Transactional
	public Progetto condividiProgettoConUtente(Progetto progetto, Utente utente) {
		progetto.addMember(utente);
		return this.progettoRepository.save(progetto);
	}
	
	@Transactional
	public Progetto aggiungiTag(Progetto progetto, Tag tag) {
		progetto.addTag(tag);
		return this.progettoRepository.save(progetto);
	}
	
	@Transactional
	public Progetto aggiungiTask(Progetto progetto, Task task) {
		progetto.addTask(task);
		return this.progettoRepository.save(progetto);
	}
	
	@Transactional
	public List<Progetto> findAllProgetti(){
		Iterable<Progetto> i = this.progettoRepository.findAll();
		List<Progetto> lista = new ArrayList<>();
		for (Progetto u : i) {
			lista.add(u);
		}
		return lista;
	}
	
	@Transactional
	public List<Progetto> progettiPerProprietario(Utente utente){
		return this.progettoRepository.findByProprietario(utente);
	}
	
	@Transactional
	public List<Progetto> progettiPerMembro(Utente utente){
		return this.progettoRepository.findByMembri(utente);
	}
	
	
}
