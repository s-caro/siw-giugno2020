package it.uniroma3.siw.progetto.controller;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.progetto.model.Utente;

@Component
public class UtenteValidator implements Validator {
	
	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 4;
	
	@Override
	public void validate(Object o, Errors errors) {
		Utente utente = (Utente) o;
		String firstName = utente.getNome().trim();
		String lastName = utente.getCognome().trim();
		
		if(firstName.isEmpty()) {
			errors.rejectValue("nome", "required");
		}
		else if(firstName.length() < MIN_NAME_LENGTH || firstName.length() > MAX_NAME_LENGTH) {
			errors.rejectValue("nome", "size");
		}
		
		if(lastName.isEmpty()) {
			errors.rejectValue("cognome", "required");
		}
		else if(lastName.length() < MIN_NAME_LENGTH || lastName.length() > MAX_NAME_LENGTH) {
			errors.rejectValue("cognome", "size");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Utente.class.equals(aClass);
	}
}
