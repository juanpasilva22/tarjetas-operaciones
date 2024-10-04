package com.operation.creditcards.app.repository;

import com.operation.creditcards.app.model.Buys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuysRepository extends JpaRepository<Buys, Long> {
	List<Buys> findByTarjetaTitularDni(String dni);

	@Query(value = "SELECT sum(c.monto) FROM compras AS c " +
			"JOIN tarjetas t ON t.id = c.tarjeta_id " +
			"JOIN personas p ON p.id = t.persona_id " +
			"WHERE p.dni = :dni", nativeQuery = true)
	double getExpensesByPerson(@Param("dni") String dni);
}
