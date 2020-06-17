package it.uniroma3.siw.progetto.controller;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.progetto.model.Credentials;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.CredentialsService;

@Controller
public class AuthenticationController {
	
	@Autowired
	CredentialsService credentialsService;
	
	@Autowired
	UtenteValidator utenteValidator;
	
	@Autowired
	CredentialsValidator credentialsValidator;
	
	@RequestMapping(value = {"/utente/registrati"}, method = RequestMethod.GET)
	public String mostraFormRegistrazione(Model model) {
		model.addAttribute("userForm", new Utente());
		model.addAttribute("credentialsForm", new Credentials());
		
		return "registraUtente";
	}
	
	@RequestMapping(value = {"/utente/registrati"}, method = RequestMethod.POST)
	public String registraUtente(@Valid @ModelAttribute("userForm")Utente utente,
			BindingResult utenteBindingResult,
			@Valid @ModelAttribute("credentialsForm")Credentials credentials,
			BindingResult credentialsBindingResult, Model model) {
		this.utenteValidator.validate(utente, utenteBindingResult);
		this.credentialsValidator.validate(credentials, credentialsBindingResult);
		if(!utenteBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			utente.setDataCreazione(LocalDateTime.now());
			credentials.setUtente(utente);
			credentialsService.saveCredentials(credentials);
			return "registrazioneCorretta";
		}
		return "registraUtente";
	}
	
	
}
