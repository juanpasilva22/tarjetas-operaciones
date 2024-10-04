package com.operation.creditcards.app.controller;

import com.operation.creditcards.app.exception.CustomException;
import com.operation.creditcards.app.model.*;
import com.operation.creditcards.app.service.CardService;
import jakarta.mail.MessagingException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/tarjetas")
public class CardController {

	private CardService cardService;

	public CardController(CardService cardService) {
		this.cardService = cardService;
	}

	@PostMapping("/tarjeta")
	public Card registerCard(@RequestBody @Validated Card card) throws CustomException, MessagingException {
		return cardService.registerCard(card);
	}

	@GetMapping("/listar")
	public List<Card> getCardsByDni(@RequestParam(name = "dni") String dni) {
		return cardService.getCardsByDni(dni);
	}

	@GetMapping("/tasas")
	public List<TasasDTO> getCardsByDni(@RequestParam (required = false)
										@DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha) {
		return cardService.calculateRate(fecha);
	}

	@PostMapping("/compra")
	public Buys registerBuys(@RequestBody @Validated Buys buys) throws CustomException, MessagingException {
		return cardService.registerBuys(buys);
	}

	@GetMapping("/tasa-operacion")
	public TasaOperacionDTO getRateByOperation(@RequestParam(name = "id") Long id) throws CustomException {
		return cardService.getRateByOperation(id);
	}
}
