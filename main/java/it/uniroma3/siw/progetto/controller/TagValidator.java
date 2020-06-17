package it.uniroma3.siw.progetto.controller;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.progetto.model.Tag;

@Component
public class TagValidator implements Validator{

	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 2;
	final Integer MAX_DESCRIPTION_LENGTH = 1000;

	
	@Override
	public boolean supports(Class<?> clazz) {
		return Tag.class.equals(clazz);
	}



	@Override
	public void validate(Object o, Errors errors) {
		Tag tag = (Tag) o;
		String nome = tag.getNome().trim();
		String colore = tag.getColore().trim();
		String descrizione = tag.getDescrizione().trim();
		
		if(nome.isEmpty()){
			errors.rejectValue("nome", "required");
		}
		else if (nome.length() < MIN_NAME_LENGTH || nome.length() > MAX_NAME_LENGTH)
			errors.rejectValue("nome", "size");
		
		if(colore.isEmpty()){
			errors.rejectValue("nome", "required");
		}
		
		if(descrizione.length() > MAX_DESCRIPTION_LENGTH)
			errors.reject("descrizione", "size");

	}

}
