package it.uniroma3.siw.progetto.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.progetto.model.Task;

@Component
public class TaskValidator implements Validator{

	private static final Logger logger = LoggerFactory.getLogger(TaskValidator.class);

	@Override
	public boolean supports(Class<?> aClass) {
		return Task.class.equals(aClass);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "descrizione", "required");
		
		if(!errors.hasErrors()) {
			logger.debug("nome e descrizione non nulli");
		}
	}

}
