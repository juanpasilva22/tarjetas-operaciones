package com.operation.creditcards.app.services;

import com.operation.creditcards.app.exception.CustomException;
import com.operation.creditcards.app.model.Card;
import com.operation.creditcards.app.model.Person;
import com.operation.creditcards.app.repository.CardRepository;
import com.operation.creditcards.app.service.CardService;
import com.operation.creditcards.app.serviceImpl.CardServiceImpl;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {
	@Mock
	private CardRepository cardRepository;
	@InjectMocks
	private CardService cardService = new CardServiceImpl();

	/**
	 * Test para registrar una tarjeta
	 */
	@Test
	void registerCard() throws CustomException, MessagingException, ParseException {
		Person person = new Person();
		person.setNombre("Juan");
		person.setApellido("Salvo");
		person.setEmail("juansalvotest@info.com");
		person.setDni("30500501");
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Date fechaNacimiento = simpleDateFormat.parse("1982-06-01");
		Date fechaVencimiento = simpleDateFormat.parse("2030-05-01");
		person.setFechaNacimiento(fechaNacimiento);

		Card card = new Card();
		card.setMarca("NARA");
		card.setNumero("371111111111114");
		card.setCvv("110");
		card.setFechaVencimiento(fechaVencimiento);
		card.setTitular(person);

		lenient().when(cardService.registerCard(card)).thenReturn(card);

		assertThat(card.getMarca()).isEqualTo("NARA");
		assertThat(card.getNumero()).isEqualTo("371111111111114");
		assertThat(card.getCvv()).isEqualTo("110");
		assertThat(card.getFechaVencimiento()).isEqualTo(fechaVencimiento);

	}
}
