package it.uniroma3.siw.progetto.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Utente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;
	
	@Column(nullable = false, length = 100)
	private String nome;
	
	@Column(nullable = false, length = 100)
	private String cognome;
	
	private LocalDateTime dataCreazione;
	
	@OneToMany(mappedBy = "proprietario", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Progetto> progettiPosseduti;
	
	@ManyToMany(mappedBy = "membri") 
	private List<Progetto> progettiVisibili;
	

	public Utente() {

	}

	public Utente(String nome, String cognome) {
		this.nome = nome;
		this.cognome = cognome;
		this.progettiPosseduti = new ArrayList<>();
		this.progettiVisibili = new ArrayList<>();
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

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public LocalDateTime getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(LocalDateTime dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public List<Progetto> getProgettiPosseduti() {
		return progettiPosseduti;
	}

	public void setProgettiPosseduti(List<Progetto> progettiPosseduti) {
		this.progettiPosseduti = progettiPosseduti;
	}

	public List<Progetto> getProgettiVisibili() {
		return progettiVisibili;
	}

	public void setProgettiVisibili(List<Progetto> progettiVisibili) {
		this.progettiVisibili = progettiVisibili;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cognome == null) ? 0 : cognome.hashCode());
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
		Utente other = (Utente) obj;
		if (cognome == null) {
			if (other.cognome != null)
				return false;
		} else if (!cognome.equals(other.cognome))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;

		return true;
	}
	
	
}
