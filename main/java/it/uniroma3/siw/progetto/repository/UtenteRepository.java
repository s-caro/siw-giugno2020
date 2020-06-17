package it.uniroma3.siw.progetto.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Utente;

@Repository
public interface UtenteRepository extends CrudRepository<Utente, Long> {

	
	public List<Utente> findByProgettiVisibili(Progetto progetto);
}
