package it.uniroma3.siw.progetto.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Task {
	
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private Long Id;
	
	private String nome;
	
	private String descrizione;
	
	private LocalDateTime dataCreazione;
	
	private Boolean completato;


	@ManyToOne
	private Progetto progetto;
	
	@ManyToOne
	private Utente assegnatario;
	
	@ManyToMany(mappedBy = "task")
	private List<Tag> tag;
	
	@OneToMany(mappedBy = "task", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Commento> commenti;
	
	public Task() {
		this.commenti = new ArrayList<>();
		this.tag = new ArrayList<>();
	}

	public Utente getAssegnatario() {
		return assegnatario;
	}

	public void setAssegnatario(Utente assegnatario) {
		this.assegnatario = assegnatario;
	}

	public Task(String nome, String descrizione) {
		this.nome = nome;
		this.descrizione = descrizione;
		this.commenti = new ArrayList<>();
		this.tag = new ArrayList<>();
		this.dataCreazione = LocalDateTime.now();
	}
	
	public void addTag(Tag tag){
		this.tag.add(tag);
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

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public LocalDateTime getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(LocalDateTime dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Boolean getCompletato() {
		return completato;
	}

	public void setCompletato(Boolean completato) {
		this.completato = completato;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
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
		Task other = (Task) obj;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	public Progetto getProgetto() {
		return progetto;
	}

	public void setProgetto(Progetto progetto) {
		this.progetto = progetto;
	}

	public List<Tag> getTag() {
		return tag;
	}

	public void setTag(List<Tag> tag) {
		this.tag = tag;
	}

	public List<Commento> getCommenti() {
		return commenti;
	}

	public void setCommenti(List<Commento> commenti) {
		this.commenti = commenti;
	}
	
	public void addCommento(Commento commento) {
		this.commenti.add(commento);
	}
	
}
