package it.uniroma3.siw.progetto.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.progetto.controller.session.SessionData;
import it.uniroma3.siw.progetto.model.Credentials;
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.CredentialsService;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.UtenteService;

@Controller
public class ProgettoController {
	
	@Autowired
	private ProgettoService progettoService;
	
	
	@Autowired
	private ProgettoValidator progettoValidator;
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private SessionData sessionData;
	
	@Autowired
	private CredentialsService credentialsService;
	
	
	
	@RequestMapping(value="/progetti", method = RequestMethod.GET)
	public String visualizzaProgettiPosseduti(Model model) {
		Utente utenteLoggato = sessionData.getLoggedUser();
		List<Progetto> progetti = progettoService.progettiPerProprietario(utenteLoggato);
		model.addAttribute("progetti", progetti);
		model.addAttribute("utente", utenteLoggato);
		return "progetti";
	}
	
	
	@RequestMapping(value="/progetti/condivisi", method = RequestMethod.GET)
	public String progettiCondivisiConMe(Model model) {
		Utente utenteLoggato = sessionData.getLoggedUser();
		List<Progetto> progetti = progettoService.progettiPerMembro(utenteLoggato);
		model.addAttribute("progetti", progetti);
		model.addAttribute("utente",utenteLoggato);
		return "progetti";
	}
	
	@RequestMapping(value="/progetti/aggiungi", method = RequestMethod.GET)
	public String addProgetto(Model model) {
    	Utente u = sessionData.getLoggedUser();
    	model.addAttribute("progetto", new Progetto());
    	model.addAttribute("utente", u);
        return "nuovoProgetto.html";
    }
	
	
	@RequestMapping(value="/progetti/aggiungi", method = RequestMethod.POST)
	public String nuovoProgetto(@Valid @ModelAttribute("progetto") Progetto progetto,
			Model model, BindingResult bindingResult) {
		this.progettoValidator.validate(progetto, bindingResult);
		Utente u = sessionData.getLoggedUser();
		if(!bindingResult.hasErrors()) {
			progetto.setProprietario(u);
			progetto.setDataInizio(LocalDateTime.now());
			progetto = this.progettoService.saveProgetto(progetto);
			return "redirect:/progetto/" + progetto.getId();
		}
		model.addAttribute("utente", u);
		return "nuovoProgetto.html";
		
	}
	
	@RequestMapping(value="/progetto/{id}", method = RequestMethod.GET)
	public String progetto(@PathVariable("id") Long id, Model model) {
		Progetto p = this.progettoService.getProgetto(id);
		if(p == null)
			return "redirect:/progetti";
		List<Utente> membri = utenteService.trovaMembri(p);
		Utente u = sessionData.getLoggedUser();
		if(!p.getProprietario().equals(u) && !membri.contains(u))
			return "redirect:/progetti";
		model.addAttribute("utente", u);
		model.addAttribute("progetto", p);
		model.addAttribute("membri", membri);
		return "progetto.html";
	}
	
	@RequestMapping(value="/aggiungiMembro/{id}", method = RequestMethod.GET)
	public String aggiungi(Model model, @PathVariable Long id) {
    	Utente u = sessionData.getLoggedUser();
    	Progetto p = this.progettoService.getProgetto(id);
    	model.addAttribute("progetto", p);
    	if(u.equals(p.getProprietario())) {
	        model.addAttribute("nuovoMembro", new Credentials());
    		return "nuovoMembro.html";
    	}
    	model.addAttribute("utente", u);
    	List<Utente> membri = utenteService.trovaMembri(p);
		model.addAttribute("membri", membri);
    	return "progetto.html";
    }
	
	
	@RequestMapping(value="/aggiungiMembro/{id}", method = RequestMethod.POST)
	public String aggiungiMembro(@PathVariable("id")Long id, @ModelAttribute("nuovoMembro") Credentials credentials,
			Model model) {
		Progetto p = this.progettoService.getProgetto(id);
    	Utente u = sessionData.getLoggedUser();
    	Credentials cred = this.credentialsService.getCredentials(credentials.getUsername());
    	if(cred!=null) {
    		Utente membro = cred.getUtente();
			this.progettoService.condividiProgettoConUtente(p, membro);
		}
    	List<Utente> membri = utenteService.trovaMembri(p);
		model.addAttribute("membri", membri);
		model.addAttribute("progetto",p);
    	model.addAttribute("utente", u);
		return "progetto.html";
	}
	
	@RequestMapping(value="/progetto/{id}/delete", method = RequestMethod.POST)
	public String eliminaProgetto(@PathVariable("id")Long id, Model model) {
		Utente u = sessionData.getLoggedUser();
		Progetto p = this.progettoService.getProgetto(id);
		if(u.equals(p.getProprietario())) {
			this.progettoService.deleteProgetto(p);
			return "redirect:/progetti";
		}
		return "redirect:/progetto/" + p.getId();
		
	}
	
	@RequestMapping(value="/progetto/modifica/{id}", method = RequestMethod.GET)
	public String modificaProgetto(@PathVariable("id")Long id, Model model) {
		Progetto p = progettoService.getProgetto(id);
		Utente u = sessionData.getLoggedUser();
		if(!(u.equals(p.getProprietario()))) {
			return "redirect:/progetto/" + p.getId();
		}
		model.addAttribute("idProgetto", id);
		Progetto progettoNuovo = new Progetto();
		model.addAttribute("progettoNuovo", progettoNuovo);
		return "modificaProgetto.html";
	}
	
	@RequestMapping(value="/progetto/modifiche/{idProgetto}", method = RequestMethod.POST)
	public String salvaModifica(@ModelAttribute("progettoNuovo")Progetto progetto, 
			@PathVariable("idProgetto")Long id, Model model, BindingResult bindingResult) {
		Progetto progettoVecchio = this.progettoService.getProgetto(id);
    	Utente u = sessionData.getLoggedUser();
		this.progettoValidator.validate(progetto, bindingResult);
		if(!bindingResult.hasErrors() && (u.equals(progettoVecchio.getProprietario()))) {
			progettoVecchio.setNome(progetto.getNome());
			progettoVecchio.setDescrizione(progetto.getDescrizione());
			progettoVecchio  = this.progettoService.saveProgetto(progettoVecchio);
	    	List<Utente> membri = utenteService.trovaMembri(progettoVecchio);
			model.addAttribute("membri", membri);
			model.addAttribute("progetto", progettoVecchio);
	    	model.addAttribute("utente", u);
			return "progetto.html";
		}
		else {
			model.addAttribute("idProgetto", id);
			Progetto progettoNuovo = new Progetto();
			model.addAttribute("progettoNuovo", progettoNuovo);
			return "modificaProgetto.html";
		}
	}
}
