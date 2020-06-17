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
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.CommentoService;
import it.uniroma3.siw.progetto.service.TaskService;
import it.uniroma3.siw.progetto.service.UtenteService;

@Controller
public class CommentoController {
	
	@Autowired
	private CommentoService commentoService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private SessionData sessionData;
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private CommentoValidator commentoValidator;
	
	@RequestMapping(value="/commento/{id}", method = RequestMethod.POST)
	public String aggiungiCommento(@PathVariable("id")Long id, @ModelAttribute("commento")Commento commento, 
			Model model, BindingResult bindingResult) {
		Task task = taskService.getTask(id);
		Progetto p = task.getProgetto();
		Utente proprietario = p.getProprietario();
		List<Utente> membri = utenteService.trovaMembri(p);
		Utente utenteLoggato = sessionData.getLoggedUser();
		this.commentoValidator.validate(commento, bindingResult);
		if((utenteLoggato.equals(proprietario) || membri.contains(utenteLoggato) && !bindingResult.hasErrors())) {
			commento.setTask(task);
			commento = commentoService.saveCommento(commento);
			task = taskService.aggiungiCommento(task, commento);
		}
		model.addAttribute("credentials", new Credentials());
		model.addAttribute("task",task);
		boolean assegnato = (task.getAssegnatario()==null);
		model.addAttribute("assegnato", assegnato);
		model.addAttribute("assegnatario",task.getAssegnatario());
		model.addAttribute("progetto", task.getProgetto());
		Commento comm = new Commento();
		model.addAttribute("commento", comm);
		List<Commento> commenti = commentoService.findByTask(task);
		model.addAttribute("commenti", commenti);
		return "task.html";
	}
}
