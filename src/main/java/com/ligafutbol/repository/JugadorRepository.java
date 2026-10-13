package com.ligafutbol.repository;

import com.ligafutbol.model.Jugador;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JugadorRepository extends MongoRepository<Jugador, String> {
}
