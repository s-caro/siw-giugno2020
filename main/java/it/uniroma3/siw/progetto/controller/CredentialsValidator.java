package it.uniroma3.siw.progetto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.progetto.model.Credentials;
import it.uniroma3.siw.progetto.service.CredentialsService;

@Component
public class CredentialsValidator implements Validator{

	@Autowired
	CredentialsService credentialsService;
	
	final Integer MAX_USERNAME_LENGTH = 20;
	final Integer MIN_USERNAME_LENGTH = 4;
	final Integer MAX_PASSWRD_LENGTH = 20;
	final Integer MIN_PASSWORD_LENGTH = 6;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Credentials.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors errors) {
		Credentials credentials = (Credentials) o;
		String username = credentials.getUsername().trim();
		String password = credentials.getPassword().trim();
		
		if(username.isEmpty()) {
			errors.rejectValue("username", "required");
		}
		else if(username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH) {
			errors.rejectValue("username", "size");
		}
        else if (this.credentialsService.getCredentials(username) != null)
            errors.rejectValue("username", "duplicate");
		
		if(password.isEmpty()) {
			errors.rejectValue("password", "required");
		}
		else if(password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWRD_LENGTH) {
			errors.rejectValue("password", "size");
		}
	}
	
	public void validateModifica(Object o, Errors errors) {
		
		Credentials credentials = (Credentials) o;
		String password = credentials.getPassword().trim();
		if(password.isEmpty()) {
			errors.rejectValue("password", "required");
		}
		else if(password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWRD_LENGTH) {
			errors.rejectValue("password", "size");
		}
	}

}
