package it.uniroma3.siw.progetto.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.UtenteRepository;


@Service
public class UtenteService {
	
	@Autowired
	private UtenteRepository utenteRepository;
	

	
	@Transactional
	public Utente getUtente(Long id) {
		Optional<Utente> result = this.utenteRepository.findById(id);
		return result.orElse(null);
	}

	@Transactional
	public void deleteUtente(Utente utente) {
		this.utenteRepository.delete(utente);
	}
	
	@Transactional
	public Utente saveUtente(Utente utente) {
		return this.utenteRepository.save(utente);
	}
	
	
	
	@Transactional
	public List<Utente> findAllUtenti(){
		Iterable<Utente> i = this.utenteRepository.findAll();
		List<Utente> lista = new ArrayList<>();
		for (Utente u : i) {
			lista.add(u);
		}
		return lista;
	}
	
	@Transactional
	public List<Utente> trovaMembri(Progetto progetto){
		return this.utenteRepository.findByProgettiVisibili(progetto);
	}
	
}
