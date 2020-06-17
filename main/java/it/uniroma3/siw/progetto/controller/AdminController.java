package it.uniroma3.siw.progetto.controller;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.progetto.controller.session.SessionData;
import it.uniroma3.siw.progetto.model.Credentials;
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.CredentialsService;
import it.uniroma3.siw.progetto.service.ProgettoService;

@Controller
public class AdminController {
				
		@Autowired
		private SessionData sessionData;
		
		@Autowired
		private CredentialsService credentialsService;
		
		@Autowired
		private ProgettoService progettoService;
		
		
		@RequestMapping (value = "/admin/utenti", method = RequestMethod.GET)
		public String listaUtenti(Model model) {
			Utente u = sessionData.getLoggedUser();
			List<Credentials> allCredentials = this.credentialsService.getAllCredentials();
			model.addAttribute("utente", u);
			model.addAttribute("credentialsList", allCredentials);
			return "tuttiUtenti";
		}
		
		@RequestMapping(value = "/admin/utenti/{username}/delete", method = RequestMethod.POST)
		public String rimuoviUtente(Model model, @PathVariable String username) {
			this.credentialsService.cancellaCredenziali(username);
			return "redirect:/admin/utenti";
		}
		
		@RequestMapping (value = "/admin/progetti", method = RequestMethod.GET)
		public String listaProgetti(Model model) {
			Utente u = sessionData.getLoggedUser();
			List<Progetto> tuttiProgetti = this.progettoService.findAllProgetti();
			model.addAttribute("utente", u);
			model.addAttribute("tuttiProgetti", tuttiProgetti);
			return "tuttiProgetti";
		}
		
		@RequestMapping(value = "admin/progetti/{id}/delete", method = RequestMethod.POST)
		public String rimuoviProgetto(Model model, @PathVariable Long id) {
			Progetto p = this.progettoService.getProgetto(id);
			progettoService.deleteProgetto(p);
			return "redirect:/admin/progetti";
		}
}
