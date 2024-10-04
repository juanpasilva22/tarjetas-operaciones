package com.operation.creditcards.app.repository;

import com.operation.creditcards.app.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
	List<Card> findByTitularDni(String dni);
}

