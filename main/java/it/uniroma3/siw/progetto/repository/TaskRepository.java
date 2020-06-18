package it.uniroma3.siw.progetto.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Task;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
	List<Task> findByProgetto (Progetto progetto);
	List<Task> findByTag(Tag tag);
}
