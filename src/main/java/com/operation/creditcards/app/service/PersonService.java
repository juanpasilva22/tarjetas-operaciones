package com.operation.creditcards.app.service;

import com.operation.creditcards.app.exception.CustomException;
import com.operation.creditcards.app.model.Person;

import java.util.List;

public interface PersonService {
	List<Person> findAll();
	Person registerPerson(Person person) throws CustomException;
	Person updatePerson(Person person) throws CustomException;
	void deletePerson(Long id) throws CustomException;
}
