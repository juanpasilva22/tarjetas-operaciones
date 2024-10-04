package com.operation.creditcards.app.model;

import jakarta.persistence.*;

/**
 * Entidad que correspnde a una compra.
 */
@Entity
@Table(name = "compras")
public class Buys {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private double monto;
	private String detalle;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "tarjeta_id")
	private Card tarjeta;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public Card getTarjeta() {
		return tarjeta;
	}

	public void setTarjeta(Card tarjeta) {
		this.tarjeta = tarjeta;
	}
}
