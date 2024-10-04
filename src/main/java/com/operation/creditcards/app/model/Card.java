package com.operation.creditcards.app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.Date;

/**
 * Modelo correspondiente a tarjeta de credito.
 */
@Entity
@Table(name = "tarjetas")
public class Card {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String marca;
	private String numero;

	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date fechaVencimiento;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "persona_id")
	private Person titular;

	private String cvv;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Person getTitular() {
		return titular;
	}

	public void setTitular(Person titular) {
		this.titular = titular;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	@Override
	public String toString() {
		return "Card{" +
				"marca='" + marca + '\'' +
				", numero='" + numero + '\'' +
				", fechaVencimiento=" + fechaVencimiento +
				", titular=" + titular +
				", cvv='" + cvv + '\'' +
				'}';
	}
}
