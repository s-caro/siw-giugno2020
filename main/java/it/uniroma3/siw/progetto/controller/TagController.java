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
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.TagService;
import it.uniroma3.siw.progetto.service.TaskService;
import it.uniroma3.siw.progetto.service.UtenteService;

@Controller
public class TagController {
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private SessionData sessionData;
	
	@Autowired
	private ProgettoService progettoService;
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private TagValidator tagValidator;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private CommentoService commentoService;
	
	
	@RequestMapping(value="/tag/{tagid}/{progettoid}", method = RequestMethod.GET)
	public String visualizzaTag(@PathVariable("tagid")Long tagId, @PathVariable("progettoid")Long progettoId,
			Model model) {
		Tag t = this.tagService.getTag(tagId);
		Progetto p = this.progettoService.getProgetto(progettoId);
		model.addAttribute("progetto", p);
		model.addAttribute("tag",t);
		return "tag.html";
	}
	
	@RequestMapping(value="/progetto/tag/aggiungi/{id}", method = RequestMethod.GET)
	public String aggiungiTag(@PathVariable("id") Long id, Model model) {
		Progetto p = progettoService.getProgetto(id);
		Utente u = sessionData.getLoggedUser();
		Tag tag= new Tag();
		if(u.equals(p.getProprietario())) {
			model.addAttribute("progetto", p);
			model.addAttribute("tag", tag);
			return "nuovoTag.html";
		}
		List<Utente> membri = utenteService.trovaMembri(p);
		model.addAttribute("utente", u);
		model.addAttribute("progetto",p);
		model.addAttribute("membri", membri);
		return "progetto.html";
		
	}
	
	@RequestMapping(value="/progetto/tag/nuovo/{id}", method = RequestMethod.POST)
	public String nuovoTag(@PathVariable("id")Long id, @ModelAttribute("tag") Tag tag,
			Model model, BindingResult bindingResult) {
		Progetto p = this.progettoService.getProgetto(id);
		List<Utente> membri = utenteService.trovaMembri(p);
		Utente u = sessionData.getLoggedUser();
		this.tagValidator.validate(tag, bindingResult);
		if(!bindingResult.hasErrors()) {
				tag.setProgetto(p);
				tag = this.tagService.saveTag(tag);
				p = this.progettoService.aggiungiTag(p, tag);
				model.addAttribute("utente", u);
				model.addAttribute("progetto",p);
				model.addAttribute("membri", membri);
				return "progetto.html";
		}
		model.addAttribute("progetto", p);
		model.addAttribute("tag", tag);
		return "nuovoTag.html";
	}

	
	@RequestMapping(value="/task/tag/aggiungi/{id}", method = RequestMethod.GET)
	public String scegliTag(@PathVariable("id")Long id, Model model) {
		Task task = taskService.getTask(id);
		Progetto p = task.getProgetto();
		List<Tag> tagProgetto = tagService.findByProgetto(p);
		Utente utenteLoggado = sessionData.getLoggedUser();
		if(utenteLoggado.equals(p.getProprietario())) {
			model.addAttribute("tag", tagProgetto);
			model.addAttribute("task", task);
			model.addAttribute("progetto", p);
			return "tagPerProgetto.html";
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
	
	@RequestMapping(value="/tag/{id}/delete", method = RequestMethod.POST)
	public String eliminaTag(@PathVariable("id")Long id, Model model) {
		Utente u = sessionData.getLoggedUser();
		Tag t = this.tagService.getTag(id);
		Progetto p = t.getProgetto();
		if(u.equals(p.getProprietario())) {
			this.tagService.deleteTag(t);
		}
		return "redirect:/progetto/" + p.getId();
		
	}
}
