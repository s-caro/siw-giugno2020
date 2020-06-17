package it.uniroma3.siw.progetto.controller;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.progetto.model.Commento;

@Component
public class CommentoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Commento.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Commento commento = (Commento) target;
		String contenuto = commento.getContenuto().trim();
		
		if(contenuto.isEmpty()) {
			errors.rejectValue("contenuto", "required");
		}
	}

}
