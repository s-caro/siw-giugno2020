package it.uniroma3.siw.progetto.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Tag;
import it.uniroma3.siw.progetto.model.Task;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {
		public List<Tag> findByProgetto(Progetto progetto);
		public List<Tag> findByTask(Task task);
}
