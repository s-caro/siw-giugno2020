package it.uniroma3.siw.progetto.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.progetto.model.Commento;
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Tag;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.TaskRepository;

@Service
public class TaskService {
	
	@Autowired
	private TaskRepository taskRepository;
	
	
	@Transactional
	public Task getTask(Long id) {
		Optional<Task> result = this.taskRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Task saveTask(Task task) {
		task.setDataCreazione(LocalDateTime.now());
		return this.taskRepository.save(task);
	}
	
	@Transactional
	public void deleteTask(Task task) {
		Progetto p = task.getProgetto();
		p.getTask().remove(task);
		for (Tag t : task.getTag()) {
			t.getTask().remove(task);
		}
		this.taskRepository.delete(task);
	}
	
	@Transactional
	public void deleteTaskById(Long id) {
		this.taskRepository.deleteById(id);
	}
	
	@Transactional
	public Task addTag(Task task, Tag tag) {
		task.addTag(tag);
		return this.taskRepository.save(task);
	}
	
	@Transactional
	public Task assegnaUtente(Task task, Utente utente) {
		task.setAssegnatario(utente);
		return this.taskRepository.save(task);
	}
	
	@Transactional
	public Task aggiungiCommento(Task task, Commento commento) {
		task.addCommento(commento);
		return this.taskRepository.save(task);
	}
	
	@Transactional
	public List<Task> findAllTask(){
		Iterable<Task> i = this.taskRepository.findAll();
		List<Task> lista = new ArrayList<>();
		for (Task u : i) {
			lista.add(u);
		}
		return lista;
	}
}
