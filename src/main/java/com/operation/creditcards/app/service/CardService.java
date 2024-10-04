package com.operation.creditcards.app.service;

import com.operation.creditcards.app.exception.CustomException;
import com.operation.creditcards.app.model.Buys;
import com.operation.creditcards.app.model.Card;
import com.operation.creditcards.app.model.TasaOperacionDTO;
import com.operation.creditcards.app.model.TasasDTO;
import jakarta.mail.MessagingException;

import java.util.Date;
import java.util.List;

public interface CardService {
	Card registerCard(Card card) throws CustomException, MessagingException;

	List<Card> getCardsByDni(String dni);

	List<TasasDTO> calculateRate(Date date);

	Buys registerBuys(Buys buys) throws CustomException, MessagingException;

	TasaOperacionDTO getRateByOperation(Long id) throws CustomException;
}
