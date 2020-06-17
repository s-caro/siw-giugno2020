package it.uniroma3.siw.progetto.model;

import javax.persistence.*;

@Entity
public class Commento {

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	private Long Id;
	
	private String contenuto;
	
	@ManyToOne
	private Task task;
	
	public Commento() {
		
	}

	
	public Commento(String contenuto) {
		this.contenuto = contenuto;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getContenuto() {
		return contenuto;
	}

	public void setContenuto(String contenuto) {
		this.contenuto = contenuto;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contenuto == null) ? 0 : contenuto.hashCode());
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
		Commento other = (Commento) obj;
		if (contenuto == null) {
			if (other.contenuto != null)
				return false;
		} else if (!contenuto.equals(other.contenuto))
			return false;
		return true;
	}
	
	
	
	
}
