package it.uniroma3.siw.progetto.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.progetto.controller.session.SessionData;
import it.uniroma3.siw.progetto.model.Commento;
import it.uniroma3.siw.progetto.model.Credentials;
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Tag;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.CommentoService;
import it.uniroma3.siw.progetto.service.CredentialsService;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.TagService;
import it.uniroma3.siw.progetto.service.TaskService;
import it.uniroma3.siw.progetto.service.UtenteService;

@Controller
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private ProgettoService progettoService;
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private TaskValidator taskValidator;
	
	@Autowired
	private SessionData sessionData;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private CommentoService commentoService;
	
	@Autowired
	private TagService tagService;
	
	@RequestMapping(value="/progetto/task/aggiungi/{id}", method = RequestMethod.GET)
	public String aggiungiTask(@PathVariable("id")Long id, Model model) {
		Progetto p = progettoService.getProgetto(id);
		Utente u = sessionData.getLoggedUser();
		Task task = new Task();
		if(u.equals(p.getProprietario())) {
			model.addAttribute("progetto", p);
			model.addAttribute("task", task);
			return "nuovoTask.html";
		}
		List<Utente> membri = utenteService.trovaMembri(p);
		model.addAttribute("utente", u);
		model.addAttribute("progetto",p);
		model.addAttribute("membri", membri);
		return "progetto.html";
		
	}
	
	@RequestMapping(value="/task/nuovo/{id}", method = RequestMethod.POST)
	public String nuovoTask(@PathVariable("id")Long id, @ModelAttribute("task") Task task,
			Model model, BindingResult bindingResult) {
		Progetto p = this.progettoService.getProgetto(id);
		List<Utente> membri = utenteService.trovaMembri(p);
		Utente u = sessionData.getLoggedUser();
		this.taskValidator.validate(task, bindingResult);
		if(!bindingResult.hasErrors()) {
				task.setProgetto(p);
				task = taskService.saveTask(task);
				p = this.progettoService.aggiungiTask(p, task);
				model.addAttribute("utente", u);
				model.addAttribute("progetto",p);
				model.addAttribute("membri", membri);
				return "progetto.html";
		}
		model.addAttribute("progetto", p);
		model.addAttribute("task", task);
		return "nuovoTask.html";
	}


	
	@RequestMapping(value="/task/{id}", method = RequestMethod.GET)
	public String task(@PathVariable("id")Long id, Model model) {
		Task task = taskService.getTask(id);
		model.addAttribute("credentials", new Credentials());
		model.addAttribute("task",task);
		boolean assegnato = (task.getAssegnatario()==null);
		model.addAttribute("assegnato", assegnato);
		model.addAttribute("assegnatario",task.getAssegnatario());
		model.addAttribute("progetto", task.getProgetto());
		Commento commento = new Commento();
		model.addAttribute("commento", commento);
		List<Commento> commenti = commentoService.findByTask(task);
		model.addAttribute("commenti", commenti);
		return "task.html";
	}
	
	@RequestMapping(value="/task/assegnaTask/{id}", method = RequestMethod.POST)
	public String assegnaTask(@PathVariable("id")Long id, @ModelAttribute("credentials")Credentials credentials,
			Model model) {
		Task task = this.taskService.getTask(id);
		Utente u = sessionData.getLoggedUser();
    	Credentials cred = this.credentialsService.getCredentials(credentials.getUsername());
		model.addAttribute("progetto", task.getProgetto());
		Commento commento = new Commento();
		model.addAttribute("commento", commento);
		List<Commento> commenti = commentoService.findByTask(task);
		model.addAttribute("commenti", commenti);
    	if(cred != null) {
    		Utente membro = cred.getUtente();
			if(u.equals(task.getProgetto().getProprietario())) {
				task = this.taskService.assegnaUtente(task, membro);
			}
			boolean assegnato = (task.getAssegnatario()==null);
			model.addAttribute("assegnatario", membro);
			model.addAttribute("task", task);
			model.addAttribute("assegnato",assegnato);
			model.addAttribute("progetto", task.getProgetto());
			return "task.html";
    	}
    	else {
    		boolean assegnato = (task.getAssegnatario()==null);
    		model.addAttribute("task", task);
			model.addAttribute("assegnato",assegnato);
    		model.addAttribute("assegnatario",task.getAssegnatario());
    		return "task.html";
    	}
    		

	}
	
	@RequestMapping(value="/task/{id}/delete", method = RequestMethod.POST)
	public String eliminaTask(@PathVariable("id") Long id, Model model) {
		Utente u = sessionData.getLoggedUser();
		Task task = this.taskService.getTask(id);
		Progetto p = task.getProgetto();
		if(u.equals(p.getProprietario())) {
			this.taskService.deleteTask(task);
			return "redirect:/progetto/" + p.getId();
		}
		
		return "redirect:/task/" + task.getId();
	}
	
	@RequestMapping(value="/task/{id}/completato", method= RequestMethod.POST)
	public String completaTask(@PathVariable("id")Long id, Model model) {
		Utente u = sessionData.getLoggedUser();
		Task task = this.taskService.getTask(id);
		Progetto p = task.getProgetto();
		if(u.equals(p.getProprietario()) || u.equals(task.getAssegnatario())) {
			task.setCompletato(true);
			task = taskService.saveTask(task);
		}
		return "redirect:/progetto/" + p.getId();
	}
	
	@RequestMapping(value="/task/assegnatags/{tagId}/{taskId}", method = RequestMethod.GET)
	public String assegnaTag(@PathVariable("taskId")Long taskId, @PathVariable("tagId")Long tagId,
			Model model) {
		Task task = taskService.getTask(taskId);
		Tag tag = this.tagService.getTag(tagId);
		task = this.taskService.addTag(task, tag);
		tag = this.tagService.aggiungiTask(tag, task);
		System.out.println(tagService.findByTask(task).size());
		model.addAttribute("tags", tagService.findByTask(task));
		model.addAttribute("credentials", new Credentials());
		model.addAttribute("task",task);
		boolean assegnato = (task.getAssegnatario()==null);
		model.addAttribute("assegnato", assegnato);
		model.addAttribute("assegnatario",task.getAssegnatario());
		model.addAttribute("progetto", task.getProgetto());
		Commento commento = new Commento();
		model.addAttribute("commento", commento);
		List<Commento> commenti = commentoService.findByTask(task);
		model.addAttribute("commenti", commenti);
		return "task.html";
	}
	
	@RequestMapping(value="/task/modifica/{id}", method = RequestMethod.GET)
	public String modificaTask(@PathVariable("id")Long id, Model model) {
		model.addAttribute("idTask", id);
		Task taskNuovo = new Task();
		model.addAttribute("taskNuovo", taskNuovo);
		return "modificaTask.html";
	}
	
	@RequestMapping(value="/task/modifiche/{idTask}", method = RequestMethod.POST)
	public String salvaModifica(@ModelAttribute("taskNuovo")Task task, 
			@PathVariable("idTask")Long id, Model model, BindingResult bindingResult) {
		Task taskVecchio = this.taskService.getTask(id);
		this.taskValidator.validate(task, bindingResult);
		if(!bindingResult.hasErrors()) {
			taskVecchio.setNome(task.getNome());
			taskVecchio.setDescrizione(task.getDescrizione());
			taskVecchio  = this.taskService.saveTask(taskVecchio);
			model.addAttribute("credentials", new Credentials());
			model.addAttribute("task",taskVecchio);
			boolean assegnato = (taskVecchio.getAssegnatario()==null);
			model.addAttribute("assegnato", assegnato);
			model.addAttribute("assegnatario",taskVecchio.getAssegnatario());
			model.addAttribute("progetto", taskVecchio.getProgetto());
			Commento commento = new Commento();
			model.addAttribute("commento", commento);
			List<Commento> commenti = commentoService.findByTask(taskVecchio);
			model.addAttribute("commenti", commenti);
			return "task.html";
		}
		else {
			model.addAttribute("idTask", id);
			Task taskNuovo = new Task();
			model.addAttribute("taskNuovo", taskNuovo);
			return "modificaTask.html";
		}
	}
}
