package it.uniroma3.siw.progetto.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.progetto.model.Credentials;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.CredentialsService;

@Controller
public class MainController {
	
	@Autowired
	private CredentialsService credentialsService;
	
	public MainController() {
		
	}
	
	@RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET )
	public String index(Model model) {
		if(this.credentialsService.findByRole("ADMIN").size()==0) {
			Utente utente = new Utente("Admin","Admin");
			utente.setDataCreazione(LocalDateTime.now());
			Credentials credentials = new Credentials();
			credentials.setUsername("admin");
			credentials.setPassword("admin");
			credentials.setUtente(utente);
			this.credentialsService.saveAdminCredentials(credentials);
			return "index";
		}
		return "index";
	}
	
}
