package it.uniroma3.siw.progetto.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.progetto.model.Credentials;
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.CredentialsRepository;

@Service
public class CredentialsService {
	
	@Autowired
	private CredentialsRepository credentialsRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	public Credentials getCredentials(Long id) {
		Optional<Credentials> result = this.credentialsRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Credentials getCredentials(String username) {
		Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
		return result.orElse(null);
	}
	
	@Transactional
	public Credentials saveCredentials(Credentials credentials) {
		credentials.setRole(Credentials.DEFAULT_ROLE);
		credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
		return this.credentialsRepository.save(credentials);
	}
	
	@Transactional
	public Credentials saveAdminCredentials(Credentials credentials) {
		credentials.setRole(Credentials.ADMIN_ROLE);
		credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
		return this.credentialsRepository.save(credentials);
	}
	
	@Transactional
	public List<Credentials> getAllCredentials(){
		Iterable<Credentials> credentials = this.credentialsRepository.findAll();
		List<Credentials> lista = new ArrayList<>();
		for (Credentials c : credentials) {
			lista.add(c);
		}
		return lista;
	}
	
	@Transactional
	public void cancellaCredenziali(String username) {
		Credentials c = this.credentialsRepository.findByUsername(username).get();
		Utente u = c.getUtente();
		for (Progetto p : u.getProgettiVisibili()){
			p.getMembri().remove(u);
			for(Task t : p.getTask()) {
				if(u.equals(t.getAssegnatario()))
					t.setAssegnatario(null);
			}
		}
		this.credentialsRepository.delete(c);
	}
	
	@Transactional
	public List<Credentials> findByRole(String role){
		return this.credentialsRepository.findByRole(role);
	}
	
}
