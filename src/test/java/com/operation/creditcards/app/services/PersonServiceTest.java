package com.operation.creditcards.app.services;

import com.operation.creditcards.app.exception.CustomException;
import com.operation.creditcards.app.model.Person;
import com.operation.creditcards.app.repository.PersonRepository;
import com.operation.creditcards.app.service.PersonService;
import com.operation.creditcards.app.serviceImpl.PersonServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
	@Mock
	private PersonRepository personRepository;
	//Inyectar dependencias necesarias.
	@InjectMocks
	private PersonService personService = new PersonServiceImpl();

	@Test
	void getAllPerson() {
		//given
		Person person = new Person();
		person.setNombre("Juan");
		person.setApellido("Salvo");
		person.setEmail("juansalvotest@info.com");
		person.setDni("30500501");

		Person person2 = new Person();
		person.setNombre("Clara");
		person.setApellido("Tomaselli");
		person.setEmail("claratomaselli@info.com");
		person.setDni("31200200");
		//When
		given(personService.findAll())
				.willReturn(List.of(person, person2));
		var personList = personService.findAll();
		//Then
		//Make sure to import assertThat From org.assertj.core.api package
		assertThat(personList).isNotNull();
		assertThat(personList.size()).isEqualTo(2);
	}

	@Test
	void registerPerson() throws CustomException, ParseException {
		//given
		Person person = new Person();
		person.setNombre("Juan");
		person.setApellido("Salvo");
		person.setEmail("juansalvotest@info.com");
		person.setDni("30500501");
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Date fechaNacimiento = simpleDateFormat.parse("1982-06-01");
		person.setFechaNacimiento(fechaNacimiento);

		lenient().when(personService.registerPerson(person)).thenReturn(person);

		assertThat(person.getNombre()).isEqualTo("Juan");
		assertThat(person.getApellido()).isEqualTo("Salvo");
		assertThat(person.getEmail()).isEqualTo("juansalvotest@info.com");
		assertThat(person.getDni()).isEqualTo("30500501");
	}
}
