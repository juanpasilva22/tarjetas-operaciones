package com.operation.creditcards.app.serviceImpl;

import com.operation.creditcards.app.exception.CustomException;
import com.operation.creditcards.app.model.*;
import com.operation.creditcards.app.repository.BuysRepository;
import com.operation.creditcards.app.repository.CardRepository;
import com.operation.creditcards.app.repository.PersonRepository;
import com.operation.creditcards.app.service.CardService;
import com.operation.creditcards.app.utils.EmailSender;
import com.operation.creditcards.app.utils.Validations;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {
	private static final Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);
	@Autowired
	private CardRepository cardRepository;
	@Autowired
	private BuysRepository buysRepository;

	/**
	 * Registra tarjeta de credito son datos suministrados.
	 * @param card
	 * @return Card
	 */
	@Override
	public Card registerCard(Card card) throws CustomException, MessagingException {
		logger.info("Registrar tarjeta con los siguientes datos: {}", card.toString());
		Validations validations = new Validations();
		if (card.getFechaVencimiento().before(new Date())) {
			throw new IllegalArgumentException("La tarjeta no es válida para operar.");
		}
		if (!Validations.containsCardBrand(card.getMarca())) {
			throw new CustomException("La tarjeta admitidas son las siguientes: VISA, AMEX, NARA");
		}
		if (!Validations.isValidCreditCard(card.getNumero())) {
			throw new CustomException("Número de tarjeta no válido.");
		}
		/*
		if (!Validations.validateDateWithFormat(card.getFechaVencimiento().toString())) {
			throw new CustomException("La fecha vencimiento no contiene el formato: yyyy-MM-dd");
		}
		*/
		if (!validations.validateCvv(card.getCvv())) {
			throw new CustomException("cvv no válido.");
		}
		// enviar al email datos cvv y PAN al titular de la tarjeta.
		sendEmail(card.getTitular().getEmail(),
				card.getTitular().getEmail(),
		 "****************************************<br/>"+
				"Datos sensibles de la tarjeta cvv y PAN:<br/>"+
				"cvv: XXX <br/>"+
				", PAN: "+ card.getNumero().substring(0, 13) + "XXXX",
		"Envio datos sensibles de Tarjeta de Crédito");
		return cardRepository.save(card);
	}

	/**
	 * Obtiene las tarjetas de credito asociadas a una persona
	 * por su DNI.
	 * @param dni
	 * @return {@code List<Card>}
	 */
	@Override
	public List<Card> getCardsByDni(String dni) {
		return cardRepository.findByTitularDni(dni);
	}

	/**
	 * Realiza el calculo de la tasa según la marca de la tarjeta.
	 * y devolvemos un listado
	 * @param date
	 * @return {@code TasasDTO}
	 */
	@Override
	public List<TasasDTO> calculateRate(Date date) {
		double rate = 0.0;
		int day = 0;
		int month = 0;
		int year = 0;
		/*
			Calculo de tasas:
			Tasa VISA = año / mes (Ejemplo: 20/12)
		 	Tasa NARA = dia del mes *0.5 (Ejemplo: 27 * 0.5)
		 	Tasa AMEX = mes*0.1 (Ejemplo: 9*0.1)
		*/
		List<TasasDTO> tasasDTOList = null;
		try {
			if (date == null) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
				LocalDate localDate = LocalDate.now();
				day = localDate.getDayOfMonth();
				month = localDate.getMonth().getValue();
				year = localDate.getYear();
			} else {
				day = date.toInstant()
						.atZone(ZoneId.systemDefault())
						.toLocalDate().getDayOfMonth();
				month = date.toInstant()
						.atZone(ZoneId.systemDefault())
						.toLocalDate().getMonth().getValue();
				year = date.toInstant()
						.atZone(ZoneId.systemDefault())
						.toLocalDate().getYear();
			}
			List<Card> cardList = cardRepository.findAll();
			tasasDTOList = new ArrayList<>();

			for (Card card : cardList) {
				TasasDTO tasasDTO = new TasasDTO();
				if ("VISA".equals(card.getMarca())) {
					rate = year / month;
					tasasDTO.setMarca("VISA");
				} else if ("NARA".equals(card.getMarca())) {
					tasasDTO.setMarca("NARA");
					rate = day * 0.5;
				} else if ("AMEX".equals(card.getMarca())) {
					tasasDTO.setMarca("AMEX");
					rate = month * 0.1;
				}
				tasasDTO.setTasa(rate);
				tasasDTOList.add(tasasDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tasasDTOList;
	}

	/**
	 * Registrar una compra con los datos suministrados.
	 * Una operación es válida en el sistema si la persona que opera
	 * en el mismo consume menos de 10.000 pesos.
	 * @param buys
	 * @return Buys
	 */
	@Override
	public Buys registerBuys(Buys buys) throws CustomException, MessagingException {
		Buys buysResult = null;
		String email = buys.getTarjeta().getTitular().getEmail();
		if (buys.getMonto() < 10000) {
			String dniTitular = buys.getTarjeta().getTitular().getDni();
			List<Buys> buysList = buysRepository.findByTarjetaTitularDni(dniTitular);

			// Comprobar que la suma de los consumos de tarjetas del titular
			// sea menor a 10000 pesos.
			if(!buysList.isEmpty()) {
				if (buysRepository.getExpensesByPerson(dniTitular) + buys.getMonto() < 10000) {
					buysResult = buysRepository.save(buys);
				} else {
					throw new CustomException("La suma de los consumos no puede superar los $10000");
				}
			} else {
				// Registrar primera compra.
				buysResult = buysRepository.save(buys);
			}
		} else {
			throw new CustomException("El monto del consumo no puede superar los $10000");
		}
		String msg = "Se ha realizado un consumo con la tarjeta: "+ buys.getTarjeta().getMarca() +
				", numero: "+ buys.getTarjeta().getNumero().substring(0, 13) + "XXXX <br/>" +
				" Titular: "+ buys.getTarjeta().getTitular().getNombre() + " "
				+ buys.getTarjeta().getTitular().getApellido() + "<br/>" +
				"Monto de la compra: "+ buys.getMonto() + "<br/>" +
				"Detalle: "+ buys.getDetalle();
		sendEmail(email, email, msg,"Nuevo consumo en tarjeta");
		return buysResult;
	}

	/**
	 * Consultar la tasa de una operación, informando marca de la
	 * tarjeta e importe.
	 * @param id
	 * @return TasaOperacionDTO
	 */
	@Override
	public TasaOperacionDTO getRateByOperation(Long id) throws CustomException {
		Buys buys = buysRepository.findById(id).
				orElseThrow(() -> new CustomException("No existe una compra registrada con ID=" + id));
		List<TasasDTO> tasasDTOS = calculateRate(null);
		double rate = tasasDTOS
				.stream()
				.filter(r -> buys.getTarjeta().getMarca().equals((r.getMarca())))
				.collect(Collectors.toList()).get(0).getTasa();
		TasaOperacionDTO tasaOperacionDTO = new TasaOperacionDTO();
		tasaOperacionDTO.setMonto(buys.getMonto());
		tasaOperacionDTO.setMarca(buys.getTarjeta().getMarca());
		tasaOperacionDTO.setTasa(rate);

		return tasaOperacionDTO;
	}

	private void sendEmail(String from, String to, String msg, String subject) throws MessagingException {
		EmailSender emailSender = new EmailSender();
		emailSender.sendEmail(from, to, msg, subject);
	}
}
