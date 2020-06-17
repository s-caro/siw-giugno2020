package it.uniroma3.siw.progetto.model;

import javax.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Progetto {

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private Long Id;
	
	private String nome;
	
	private LocalDateTime dataInizio;
	
	private String descrizione;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Utente proprietario;
	
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Utente> membri;
	
	@OneToMany(mappedBy = "progetto", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Task> task;
	
	@OneToMany(mappedBy="progetto", cascade = {CascadeType.REMOVE})
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Tag> tag;
	
	public Progetto() {
		
	}

	public Progetto(String nome, String descrizione) {
		this.nome = nome;
		this.descrizione = descrizione;
		this.membri = new ArrayList<>();
		this.task = new ArrayList<>();
		this.tag = new ArrayList<>();
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public void addMember(Utente utente) {
		this.membri.add(utente);
	}
	
	public void addTag(Tag tag) {
		this.tag.add(tag);
	}
	
	public void addTask(Task task) {
		this.task.add(task);
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDateTime getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(LocalDateTime dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Utente getProprietario() {
		return proprietario;
	}

	public void setProprietario(Utente proprietario) {
		this.proprietario = proprietario;
	}

	public List<Utente> getMembri() {
		return membri;
	}

	public void setMembri(List<Utente> membri) {
		this.membri = membri;
	}

	public List<Task> getTask() {
		return task;
	}

	public void setTask(List<Task> task) {
		this.task = task;
	}



	public List<Tag> getTag() {
		return tag;
	}

	public void setTag(List<Tag> tag) {
		this.tag = tag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Progetto other = (Progetto) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
	
	
	
}
