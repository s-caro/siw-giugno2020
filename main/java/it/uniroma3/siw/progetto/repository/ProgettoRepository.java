package it.uniroma3.siw.progetto.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Utente;

@Repository
public interface ProgettoRepository extends CrudRepository<Progetto, Long> {
	
	public List<Progetto> findByMembri(Utente utente);
	public List<Progetto> findByProprietario(Utente utente);
}
