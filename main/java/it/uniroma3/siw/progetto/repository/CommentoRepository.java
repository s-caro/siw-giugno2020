package it.uniroma3.siw.progetto.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.progetto.model.Commento;
import it.uniroma3.siw.progetto.model.Task;

@Repository
public interface CommentoRepository extends CrudRepository<Commento, Long> {
	public List<Commento> findByTask(Task task);
}
