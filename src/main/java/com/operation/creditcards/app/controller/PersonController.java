package com.operation.creditcards.app.controller;

import com.operation.creditcards.app.exception.CustomException;
import com.operation.creditcards.app.model.Person;
import com.operation.creditcards.app.service.PersonService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonController {
	private PersonService personService;

	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@PostMapping("/alta")
	public Person registerPerson(@RequestBody @Validated Person person) throws CustomException {
		return personService.registerPerson(person);
	}

	@PutMapping("/update")
	public Person updatePerson(@RequestBody @Validated Person person) throws CustomException {
		return personService.updatePerson(person);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deletePerson(@PathVariable(name = "id") Long id) throws CustomException {
		personService.deletePerson(id);
	}

	@GetMapping("/listar")
	public List<Person> findAll() {
		return personService.findAll();
	}
}
