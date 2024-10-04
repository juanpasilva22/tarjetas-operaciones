package com.operation.creditcards.app.repository;

import com.operation.creditcards.app.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
	Optional<Person> findByDni(String dni);
}

