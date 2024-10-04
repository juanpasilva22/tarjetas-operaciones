package com.operation.creditcards.app.serviceImpl;

import com.operation.creditcards.app.exception.CustomException;
import com.operation.creditcards.app.model.Person;
import com.operation.creditcards.app.repository.PersonRepository;
import com.operation.creditcards.app.service.PersonService;
import com.operation.creditcards.app.utils.Validations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {
	private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);
	@Autowired
	private PersonRepository personRepository;

	/**
	 * Lista todas las personas registradas en el sistema.
	 * @return
	 */
	@Override
	public List<Person> findAll() {
		return personRepository.findAll();
	}

	/**
	 * Registra persona/usuario con datos suministrados.
	 * @param person
	 * @return Person
	 */
	@Override
	public Person registerPerson(Person person) throws CustomException {
		logger.info("Dar de alta/mod usuario con los siguientes datos: {}", person.toString());
		Validations validations = new Validations();
		// Validamos la entrada de datos.
		if (!Validations.validateNameOrLastName(person.getNombre())) {
			throw new CustomException("Nombre de Persona no válido");
		}
		if (!Validations.validateNameOrLastName(person.getApellido())) {
			throw new CustomException("Apellido de Persona no válido");
		}
		if (!Validations.validateEmail(person.getEmail())) {
			throw new CustomException("Email ingresado no válido");
		}
		if (!validations.validateDNI(person.getDni())) {
			throw new CustomException("DNI ingresado no válido");
		}
		/*
		if (!Validations.validateDateWithFormat(person.getFechaNacimiento().toString())) {
			throw new CustomException("La fecha de nacimiento no contiene el formato: dd-MM-yyyy");
		}
		*/
		return personRepository.save(person);
	}

	@Override
	public Person updatePerson(Person person) throws CustomException {
		Person personFinded = personRepository.findById(person.getId())
				.orElseThrow(() -> new CustomException("Persona no existe. ID=" + person.getId()));
		// Los unicos datos que son modificables es el nombre, apellido e email.
		personFinded.setNombre(person.getNombre());
		personFinded.setApellido(person.getApellido());
		personFinded.setEmail(person.getEmail());
		// llamamos el siguiente metodo para realizar las validaciones correspondientes.
		return this.registerPerson(personFinded);
	}

	@Override
	public void deletePerson(Long id) throws CustomException {
		Person personFinded = personRepository.findById(id)
				.orElseThrow(() -> new CustomException("Persona no existe. ID=" + id));
		personRepository.delete(personFinded);
	}
}
