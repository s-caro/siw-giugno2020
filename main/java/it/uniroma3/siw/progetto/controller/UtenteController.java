package it.uniroma3.siw.progetto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.progetto.controller.session.SessionData;
import it.uniroma3.siw.progetto.model.Credentials;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.CredentialsService;
import it.uniroma3.siw.progetto.service.UtenteService;


@Controller
public class UtenteController {
	
	@Autowired
	private UtenteService utenteService;

	
	@Autowired
	private SessionData sessionData;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private UtenteValidator utenteValidator;
	
	@Autowired
	private CredentialsValidator credentialsValidator;

    
    @RequestMapping(value = {"/home"}, method = RequestMethod.GET)
    public String home(Model model) {
    	model.addAttribute("credentials", sessionData.getLoggedCredentials());
    	model.addAttribute("utente", sessionData.getLoggedUser());
    	return "home.html";
    }


    
    @RequestMapping(value="/infoUtente", method = RequestMethod.GET)
    public String infoUtente( Model model) {
    	Utente u = sessionData.getLoggedUser();
    	Credentials credentials = sessionData.getLoggedCredentials();
    	model.addAttribute("utente", u);
    	model.addAttribute("credentials", credentials);
    	return "infoUtente.html";
    }
    
    @RequestMapping(value= {"/admin"}, method = RequestMethod.GET)
    public String admin(Model model) {
    	Utente utenteLoggato = sessionData.getLoggedUser();
    	model.addAttribute("utente", utenteLoggato);
    	return "admin";
    }
    
    @RequestMapping(value="/utente/modifica", method = RequestMethod.GET)
    public String modifica(Model model) {
		model.addAttribute("userForm", new Utente());
		model.addAttribute("credentialsForm", new Credentials());
    	return "modificaUtente.html";
    }
    
    @RequestMapping(value="/modifica/utenteFatta", method = RequestMethod.POST)
    public String modificaFatta(@ModelAttribute("userForm")Utente utente, BindingResult bindingUtente,
    		@ModelAttribute("credentialsForm")Credentials credenziali, BindingResult bindinCredentials, Model model) {
    		Utente utenteLoggato = this.sessionData.getLoggedUser();
    		Credentials credentials = this.sessionData.getLoggedCredentials();
    		this.utenteValidator.validate(utente, bindingUtente);
    		this.credentialsValidator.validateModifica(credenziali, bindinCredentials);
    		if(!bindingUtente.hasErrors() && !bindinCredentials.hasErrors()) {
        		utenteLoggato.setNome(utente.getNome());
        		utenteLoggato.setCognome(utente.getCognome());
        		credentials.setPassword(credenziali.getPassword());
	    		this.utenteService.saveUtente(utenteLoggato);
	    		this.credentialsService.saveCredentials(credentials);
	        	model.addAttribute("utente", utenteLoggato);
	        	model.addAttribute("credentials", credentials);
	        	return "infoUtente.html";
    		}
        	return "modificaUtente.html";
    }
}
